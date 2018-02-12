package Services;

import Controllers.ControllerDB;
import Domains.*;
import Domains.Mark;
import Exceptions.MarkException;
import Observer.*;
import Utils.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class MarkService implements Observer
{
    private ControllerDB ctr;
    @FXML
    private TableColumn<MarkEplicit,Integer> c1;
    @FXML
    private TableColumn<MarkEplicit,Integer> c2;
    @FXML
    private TableColumn<MarkEplicit,Integer> c3;
    @FXML
    private TableColumn<MarkEplicit,Integer> c4;
    @FXML
    private TableColumn<MarkEplicit,String> c5;
    @FXML
    private TableView<MarkEplicit> tv;
    @FXML
    private TextField nota;
    @FXML
    private Pagination pag;
    @FXML
    private Label number;
    private Mark toAddEditMark;
    @FXML
    private TextField nume;
    /*
        sets the controller
     */
    public void setCtr(ControllerDB ctr)
    {
        this.ctr = ctr;
        ctr.addObserver(this);
    }
    /*
        on key release handler for text field
     */
    @FXML
    private void markChanged()
    {
        if(nota.getText().isEmpty())
        {
            try
            {
                setTableItems(ctr.fetchFindAllMarks());
            }
            catch (SQLException e)
            {
                showMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
            return;
        }
        try
        {
            float f = Float.parseFloat( nota.getText());
            Predicate<Mark> p= x->x.getValoare()>f;
            setTableItems(ctr.filterMarks(p));
        }
        catch (NumberFormatException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        sets the value of the labesl that shows the number of entities in database
    */
    private void setNumberOfMarks()
    {
        int nr =ctr.sizeMarks();
        if(nr!=1)
            number.setText(String.valueOf(nr)+" note");
        else
            number.setText(String.valueOf(nr)+" nota");
    }
    /*
        shows a message
     */
    private void showMessage(String msg, Alert.AlertType at)
    {
        Alert a = new Alert(at);
        a.setContentText(msg);
        a.showAndWait();
    }
    /*
        notifyes me was observer
     */
    @Override
    public void notifyMe()
    {

        try
        {
            setTableItems(ctr.fetchFindAllMarks());
            setNumberOfMarks();
        }
        catch (SQLException e )
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        returns my observer type
     */
    @Override
    public ObserverType getObserverType()
    {
        return ObserverType.Mark;
    }

    /*
        opens the add edit window
     */
    @FXML
    public void AddWindow()
    {
        try
        {
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(fxmlFiles.clasa.class.getResource("AddEditMark.fxml"));
            Parent p=loader.load();
            Stage st = new Stage();
            st.setScene(new Scene(p));
            st.initStyle(StageStyle.UNDECORATED);
            st.initModality(Modality.APPLICATION_MODAL);
            st.show();
            AddEditMarkService aen = loader.getController();
            try
            {
                ctr.ConnectToHomeworkDatabase(User.getUsername(), User.getPassword());
            }
            catch (SQLException e )
            {
                showMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
            toAddEditMark = new Mark(0,0,0,0);
            aen.setToAddEditMark(toAddEditMark);
            st.setOnCloseRequest(event ->
            {
                addMark(toAddEditMark);
            });
            aen.setControllers(ctr);
            aen.init();
            aen.setActionText("Adauga");
        }
        catch (IOException e )
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        adds mark
     */
    public void addMark(Mark n)
    {
        try
        {
            ctr.addMark(n);
            showMessage("Nota adaugata!", Alert.AlertType.INFORMATION);
        }
        catch (MarkException |SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        sets the table mark items
     */
    private void  setTableItems()
    {
        try
        {
            setTableItems(this.ctr.fetchFindAllMarks());
        }
        catch (SQLException e )
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        initialize pagination
     */
    private void initPagination()
    {
        pag.setPageCount(ctr.sizeMarks()/20+1);
        pag.setCurrentPageIndex(0);
        pag.currentPageIndexProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if(newValue.intValue()>oldValue.intValue())
                {
                    ctr.fetchForwardMarks();
                    setTableItems();
                }
                else
                {
                    ctr.fetchBackwardMarks();
                    setTableItems();
                }
            }
        });
    }
    /*
        init components
     */
    public void init()
    {
        initTable();
        ctr.fetchForwardMarks();
        initPagination();
        setTableItems();
        setNumberOfMarks();

    }
    /*
        sets the mark table items using a given iterator
     */
    private void setTableItems(Iterator<Mark> i)
    {
        ObservableList<MarkEplicit>lo= FXCollections.observableArrayList();
            i.forEachRemaining(x->
            {
                try
                {
                    Student s = ctr.findOneStudent(x.getIdStudent());
                    MarkEplicit ne = new MarkEplicit(x.getIdNota(), x.getIdStudent(), x.getNrTema(), x.getValoare(),s.getNume());
                    lo.add(ne);

                } catch (SQLException e)
                {
                    showMessage(e.getMessage()+"kh", Alert.AlertType.ERROR);
                }
            });
        tv.setItems(lo);
    }
    /*
        delete mark
     */
    private void deleteMark(Mark n)
    {
        try
        {
            ctr.removeMark(n);
            showMessage("Nota stearsa!", Alert.AlertType.INFORMATION);
        }
        catch (SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        modify mark
     */
    private void modifyaMark(Mark n, String obs)
    {
        try
        {
            ctr.UpdateMark(n,obs);
            showMessage("Nota modificata!", Alert.AlertType.INFORMATION);
        }
        catch (MarkException |SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        opend att edit mark window for modifying
     */
    private void modifyMarkWindow(Mark n)
    {
        try
        {
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(fxmlFiles.clasa.class.getResource("AddEditMark.fxml"));
            Parent p = loader.load();
            Stage st = new Stage();
            st.setScene(new Scene(p));
            try
            {
                ctr.ConnectToStudentDatabase(User.getUsername(), User.getPassword());
                ctr.ConnectToHomeworkDatabase(User.getUsername(),User.getPassword());
            }
            catch (SQLException e)
            {
                showMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
            st.initStyle(StageStyle.UNDECORATED);
            st.initModality(Modality.APPLICATION_MODAL);
            st.show();
            AddEditMarkService aen = loader.getController();
            aen.setControllers(ctr);
            toAddEditMark = new Mark(n.getIdNota(),n.getIdStudent(),n.getNrTema(),0);

            aen.setToAddEditMark(toAddEditMark);
            aen.setActionText("Modifica");

            String modif="";
            aen.init();
            aen.setAddEditMarkModify(modif);
            try
            {
                aen.selectStudentAndHomework(ctr.findOneStudent(n.getIdStudent()), ctr.findOneHomework(n.getNrTema()),n.getValoare());
            }
            catch (SQLException e)
            {
                showMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
            st.setOnCloseRequest(event ->{
                modifyaMark(toAddEditMark,modif);
                //showMessage(toAddEditMark.toString(), Alert.AlertType.ERROR);
            });
        }
        catch (IOException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        initialize table for marks
     */
    private void initTable()
    {
        c1.setCellValueFactory(new PropertyValueFactory<>("idNota"));
        c2.setCellValueFactory(new PropertyValueFactory<>("idStudent"));
        c3.setCellValueFactory(new PropertyValueFactory<>("nrTema"));
        c4.setCellValueFactory(new PropertyValueFactory<>("valoare"));
        c5.setCellValueFactory(new PropertyValueFactory<>("name"));

        tv.setRowFactory(param ->
                {
                    TableRow<MarkEplicit> tr = new TableRow<>();
                    tr.setOnMouseClicked(event ->
                    {
                    });
                    ContextMenu cm = new ContextMenu();
                    MenuItem add = new MenuItem();
                    add.setText("Adauga");
                    add.setOnAction(event1 ->
                            {
                                AddWindow();
                            }
                    );
                    MenuItem del=new MenuItem();
                    del.setText("Sterge");
                    del.setOnAction(event -> {if(tr.getItem()!=null) deleteMark(tr.getItem());});

                    MenuItem mod= new MenuItem();
                    mod.setText("Modifica");
                    mod.setOnAction(event ->{if(tr.getItem()!=null) modifyMarkWindow(tr.getItem());});
                    cm.getItems().addAll(add,del,mod);
                    tr.setContextMenu(cm);
                    return  tr;
                }
        );
    }
    @FXML
    private void dupaNume()
    {
        List<Mark> x = new LinkedList<>();
        try
        {
            ctr.findAllMarks().forEachRemaining((s) ->
            {
                try
                {

                    if (this.ctr.findOneStudent(s.getIdStudent()).getNume().contains(nume.getText()))
                     x.add(s);
                }
                catch (SQLException e)
                {
                    System.out.println(e.getMessage());
                }
            });
        }
        catch (SQLException e)
        {
        }
        setTableItems(x.stream().iterator());
    }
}
