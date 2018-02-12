package Services;

import Controllers.ControllerDB;
import Domains.Homework;
import Exceptions.HomeworkValidatorException;
import Observer.*;
import Utils.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.function.Predicate;

import fxmlFiles.clasa;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeworkService implements Observer
{
    @FXML
    private TableColumn<Homework,Integer> c1;
    @FXML
    private TableColumn<Homework,String>c2;
    @FXML
    private TableColumn<Homework,Integer> c3;
    @FXML
    private TableView<Homework> tv;
    @FXML
    private TextArea enunt;
    @FXML
    private ComboBox<Integer> termenF;
    @FXML
    private Pagination pag;
    @FXML
    private Label number;

    private Homework toAddEditHomework;
    private ControllerDB controller;

    public HomeworkService()
    {
    }
    /*
        sets controller
     */
    public void setCtr(ControllerDB controller)
    {
        this.controller = controller;
        this.controller.addObserver(this);
    }
    /*
        observer method to be notified
     */
    @Override
    public void notifyMe()
    {
        setTableItems();
        setNumberOfHomework();
    }

    /*
        returns the observer type
     */
    @Override
    public ObserverType getObserverType()
    {
        return ObserverType.Homework;
    }

    /*
        sets the table students items
     */
    private void setTableItems()
    {
        ObservableList<Homework> x= FXCollections.observableArrayList();
        try
        {
            controller.fetchFindAllHomework().forEachRemaining(x::add);
            tv.setItems(x);
        }
        catch (SQLException e )
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
       sets the value of the labesl that shows the number of entities in database
    */
    private void setNumberOfHomework()
    {
        int nr =this.controller.sizeHomework();
        if(nr!=1)
            number.setText(String.valueOf(nr)+" teme");
        else
            number.setText(String.valueOf(nr)+" tema");
    }
    /*
          sets table student items using a given iterator
     */
    private void setTableItems(Iterator<Homework> i )
    {
        ObservableList<Homework>x =FXCollections.observableArrayList();
        i.forEachRemaining(x::add);
        tv.setItems(x);
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
        opens the addEditHomework window
     */
    private void addHomeworkWindow()
    {
        try
        {
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(clasa.class.getResource("AddEditHomework.fxml"));
            Parent p= loader.load() ;
            Stage st = new Stage();
            st.setScene(new Scene(p));
            st.initStyle(StageStyle.UNDECORATED);
            st.initModality(Modality.APPLICATION_MODAL);
            st.show();
            AddEditHomeworkService addEditHomeworkService = loader.getController();
            addEditHomeworkService.init();
            toAddEditHomework = new Homework(0,"",0);
            addEditHomeworkService.setToAddEditHomework(toAddEditHomework);
            st.setOnCloseRequest(event -> addHomework(toAddEditHomework));
            addEditHomeworkService.setTextAction("Adauga","Adauga tema");
        }
        catch (IOException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        handler for add button
     */
    @FXML
    private void buttonAdd()
    {
        addHomeworkWindow();
    }
    /*
        adds homework
     */
    private void addHomework(Homework t)
    {
        try
        {
            if(t.getTermenLimita()>User.getWeek())
            {
                this.controller.addHomework(t);
                showMessage("Tema adaugata", Alert.AlertType.INFORMATION);
            }
            else showMessage("O tema adaugata trebuisa sa aiba termen limita cu o saptamana mai mult decat cea curenta!" , Alert.AlertType.ERROR);
        }
        catch (SQLException| HomeworkValidatorException e)
        {
            showMessage(e.getMessage()+"when added", Alert.AlertType.ERROR);
        }
    }
    /*
        deletes homework
     */
    private void deleteHomework(Homework t)
    {
        try
        {
            if(controller.markWithHomework(t.getNrTema()))
            {
                showMessage("Exista note acordate pe baza acestor teme!Stergeti mai intai notele!", Alert.AlertType.ERROR);
                return;
            }
        }
        catch (SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
        try
        {
            enunt.setText("");
            controller.deleteHomework(t.getNrTema());
            showMessage("Tema stearsa!", Alert.AlertType.INFORMATION);
        }
        catch (SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        opend to addEditWindow to modify a homework
     */
    private void modifyHomework(Homework homework)
    {
        if( homework.getTermenLimita()-User.getWeek()>=1)
        {
            if (homework.getTermenLimita() > User.getWeek())
            {
                try
                {
                    controller.Update(homework);
                    showMessage("Tema modificata!", Alert.AlertType.INFORMATION);
                } catch (HomeworkValidatorException | SQLException e)
                {
                    showMessage(e.getMessage(), Alert.AlertType.ERROR);
                }
            } else
                showMessage("Termen limita nu poate fi decat o saptamana mai mare decat curenta", Alert.AlertType.WARNING);
        }
        else showMessage("Termenul unei teme poate fi modificat cu minim o saptamane inainte de termenul de predare al acesteia!", Alert.AlertType.WARNING);
    }
    /*
        opens add edit homework window
     */
    private  void modifyHomeworkStudentView(Homework t)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(clasa.class.getResource("AddEditHomework.fxml"));
            Parent r = loader.load();
            Stage st = new Stage();
            st.setScene(new Scene(r));
            st.initModality(Modality.APPLICATION_MODAL);
            st.initStyle(StageStyle.UNDECORATED);
            st.show();
            AddEditHomeworkService addEditHomeworkService = loader.getController();
            toAddEditHomework = new Homework(t.getNrTema(),"",0);
            addEditHomeworkService.setToAddEditHomework(toAddEditHomework);
            addEditHomeworkService.init();
            addEditHomeworkService.setTextAction("Modifica","Modifica tema");
            addEditHomeworkService.setValues(t.getEnunt(),t.getTermenLimita());
            st.setOnCloseRequest(event ->
            {
                modifyHomework(toAddEditHomework);
            });
        }
        catch (IOException e )
        {
        }
    }
    /*
        initialize students table
     */
    private void initTable()
    {
        c1.setCellValueFactory(new PropertyValueFactory<>("nrTema"));
        c2.setCellValueFactory(new PropertyValueFactory<>("enunt"));
        c3.setCellValueFactory(new PropertyValueFactory<>("termenLimita"));

        tv.setRowFactory(param ->
                {
                    TableRow<Homework> tr = new TableRow<>();

                    tr.setOnMouseClicked(event ->
                            {
                                if(tr.getItem()!=null)
                                enunt.setText(tr.getItem().getEnunt());
                            });
                    ContextMenu cm = new ContextMenu();
                    MenuItem add = new MenuItem();
                    add.setText("Adauga");
                    add.setOnAction(event ->
                    {
                        addHomeworkWindow();
                    });
                    MenuItem del= new MenuItem();
                    del.setText("Sterge");
                    del.setOnAction(event ->
                    {
                        deleteHomework(tr.getItem());
                    });
                    MenuItem mod= new MenuItem();
                    mod.setText("Modifica");
                    mod.setOnAction(event ->
                    {
                        modifyHomeworkStudentView(tr.getItem());
                    });
                    cm.getItems().addAll(add,del,mod);
                    tr.setContextMenu(cm);
                    return tr;
                }
        );
    }
    /*
        deactivates filter
     */
    @FXML
    private void stergeFiltru()
    {
        termenF.getSelectionModel().select(-1);
    }
    /*
        handeler for textfields when key released
     */
    private void FiltruOn()
    {
        try
        {
            int t = termenF.getSelectionModel().getSelectedItem();
            Predicate<Homework> p = x->x.getTermenLimita()==t;
            setTableItems(controller.filterHomework(p));

        }
        catch (Exception e )
        {
            setTableItems();
           // showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /*
        initilize combobox with weeks
     */
    private void inittermenFCB()
    {
        ObservableList<Integer>x = FXCollections.observableArrayList();
        x.addAll(1,2,3,4,5,6,7,8,9,10,11,12,13,14);
        termenF.setItems(x);
        termenF.getSelectionModel().selectedIndexProperty().addListener(observable ->
        {
            //showMessage("coi", Alert.AlertType.ERROR);
            FiltruOn();
        });
    }
    /*
        init pagination
     */
    private void initPagination()
    {
        pag.setPageCount(this.controller.sizeHomework()/20+1);
        pag.setCurrentPageIndex(0);

        pag.currentPageIndexProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue.intValue()>oldValue.intValue())
            {
                controller.fetchForwardHomework();
                setTableItems();
            }
            else
            {
                controller.fetchBackwardHomework();
                setTableItems();
            }
        });
    }
    /*
        init components
     */
    public void init()
    {
        initTable();
        inittermenFCB();
        initPagination();
        controller.fetchForwardHomework();
        setTableItems();
        setNumberOfHomework();
    }
}
