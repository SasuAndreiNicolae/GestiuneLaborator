package Services;


import Controllers.ControllerDB;
import Domains.Mark;
import Domains.Student;
import Exceptions.MarkException;
import Exceptions.StudentValidatorException;
import Observer.*;
import Utils.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import fxmlFiles.clasa;
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
import java.util.function.Predicate;

//observer
public class StudentService implements Observer
{
    private ControllerDB ctr;
    @FXML
    private TableView<Student> studentstv;
    @FXML
    private TextField numeF;
    @FXML
    private TextField grupaF;
    @FXML
    private TextField emailF;
    @FXML
    private TextField profF;
    @FXML
    private TextField nrMatricolF;
    @FXML
    private Pagination pag;
    @FXML
    private Label number;

    private Student toAddStudent;

    /*
        displays a message
     */
    private void showMessage(String msg, Alert.AlertType at)
    {
        Alert a = new Alert(at);
        a.setContentText(msg);
        a.showAndWait();
    }
    /*
        delete a student
     */
    private void deleteStudent(Student s)
    {
        try
        {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("Sunteti sigur ca vreti sa stergeti studentul"+s.toStringAfisare());
            if(a.showAndWait().get()==ButtonType.OK)
            {
                ctr.deleteStudent(s.getNrMatricol());
                showMessage("Student sters!", Alert.AlertType.INFORMATION);
            }
        }
        catch (SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        opens window to add new student
     */
    private void addStudentWindow()
    {
        FXMLLoader loader= new FXMLLoader(clasa.class.getResource("AddEditStudent.fxml"));
        try
        {
            Parent r=loader.load();
            Stage st = new Stage() ;
            st.setScene(new Scene(r,400,350));
            st.initStyle(StageStyle.UNDECORATED);
            st.initModality(Modality.APPLICATION_MODAL);

            st.show();
            AddEditStudentService addEditStudentService = loader.getController();
            addEditStudentService.setButtonText("Adaugaaaa");
            toAddStudent= new Student();
            addEditStudentService.setStudent(toAddStudent);
            st.setOnCloseRequest(event ->
            {
               addStudent( toAddStudent);
            });
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    /*
    sets the value of the labesl that shows the number of entities in database
     */
    private void setNumberOfStudents()
    {
        int nr =ctr.sizeStudents();
        if(nr!=1)
            number.setText(String.valueOf(nr)+" studenti");
        else
            number.setText(String.valueOf(nr)+" student");
    }
    /*
         add button handler
     */
    @FXML
    private void buttonAdd()
    {
        addStudentWindow();
    }
    /*
        opens window to edit a student
     */
    private void updateStudentsWindow(Student s)
    {
        try
        {
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(clasa.class.getResource("AddEditStudent.fxml"));
            Parent r = loader.load();
            Stage st = new Stage();
            st.setScene(new Scene(r,400,350));
            st.initStyle(StageStyle.UNDECORATED);
            st.initModality(Modality.APPLICATION_MODAL);
            st.show();
            AddEditStudentService addEditStudentService =loader.getController();
            addEditStudentService.setButtonText("Modifica");
            toAddStudent= new Student();
            toAddStudent.setNrMatricol(s.getNrMatricol());
            addEditStudentService.setStudent(toAddStudent);
            addEditStudentService.setTextFieldsText(s.getNume(),s.getGrupa(),s.getEmail(),s.getProf());
            st.setOnCloseRequest(event -> updateStudent(toAddStudent));
        }
        catch (IOException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        adds student
     */
    private void addStudent(Student s)
    {
        try
        {
            ctr.AddStudent(s);
            showMessage("Student adaugat!", Alert.AlertType.INFORMATION);
        }
        catch (StudentValidatorException|SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        updates student
     */
    private void updateStudent(Student s)
    {
        try
        {
            ctr.UpdateStudent(s);
            showMessage("Student Modificat!", Alert.AlertType.INFORMATION);
        }
        catch (StudentValidatorException|SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    //
    /*
        set table view student items using a given iterator
     */
    private void setTableViewItems(Iterator<Student>i)
    {
        ObservableList<Student> x = FXCollections.observableArrayList();
        if(i.hasNext())
        {
            i.forEachRemaining((s) -> x.add(s));
        }
        studentstv.setItems(x);
    }
    /*
        set table items and cathcing errors
     */
    private void setTableViewItemsCatchy()
    {
        try
        {
            setTableViewItems(ctr.fetchFindAll());
        }
        catch (SQLException e)
        {
            showMessage(e.getMessage()+"catchy", Alert.AlertType.ERROR);
        }
    }
    //

    /*
        opens mark to add window
     */
    private void addMarkWindow(Student s)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(clasa.class.getResource("AddEditMark.fxml"));
            Parent r = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(r));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            AddEditMarkService addEditMarkService =loader.getController();
            addEditMarkService.setControllers(ctr);
            try
            {
                ctr.ConnectToStudentDatabase(User.getUsername(),User.getPassword());
                ctr.ConnectToMarkDatabase(User.getUsername(),User.getPassword());
                ctr.ConnectToHomeworkDatabase(User.getUsername(), User.getPassword());
            }
            catch (SQLException e)
            {
                showMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
            Mark m = new Mark(0,0,0,0);
            addEditMarkService.setToAddEditMark(m);
            stage.setOnCloseRequest(event ->
            {
                try
                {
                    ctr.addMark(m);
                    showMessage("Nota adaugata!", Alert.AlertType.INFORMATION);
                }
                catch (SQLException| MarkException e)
                {
                    showMessage(e.getMessage(), Alert.AlertType.ERROR);
                }
            });
            addEditMarkService.init();
            addEditMarkService.setActionText("Adauga");
            addEditMarkService.selectStudent(s);
        }
        catch (Exception e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /*
        initialize table view for students
     */
    private void initTableView()
    {
        studentstv.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nrMatricol"));
        studentstv.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nume"));
        studentstv.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("grupa"));
        studentstv.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("email"));
        studentstv.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("prof"));

        studentstv.setRowFactory(param ->
        {
            final TableRow<Student> tr = new TableRow<>();

            tr.setOnMouseClicked(event ->
            {
                ContextMenu cm = new ContextMenu();
                MenuItem add= new MenuItem("Adauga");
                MenuItem menuItem = new MenuItem("Sterge");
                MenuItem menuItem1= new MenuItem("Modifica");
                MenuItem addMark = new MenuItem("Adauga nota");
                add.setOnAction(event1 -> {
                    addStudentWindow();
                });
                menuItem.setOnAction(event1 ->
                {
                    if(tr.getItem()!=null)
                        deleteStudent(tr.getItem());
                });
                menuItem1.setOnAction(event1 -> {
                    if(tr.getItem()!=null)
                        updateStudentsWindow(tr.getItem());
                });
                addMark.setOnAction(event1 -> {
                    addMarkWindow(tr.getItem());
                });

                cm.getItems().addAll(add,menuItem,menuItem1,addMark);

                tr.setContextMenu(cm);
            });
            return  tr;
        });
    }
    /*
        initialize paginations
     */
    private void initPagination()
    {
        pag.currentPageIndexProperty().addListener((observable, oldValue, newValue) ->
        {
            if(!filtersAreActive())
            {
                if(newValue.intValue()>oldValue.intValue())
                {
                    for (int i=oldValue.intValue();i<newValue.intValue();i++)
                    ctr.fetchForwardStudents();
                    setTableViewItemsCatchy();
                }
                else
                {
                    for (int i=oldValue.intValue();i>newValue.intValue();i--)
                    ctr.fetchBackwardStudents();
                    setTableViewItemsCatchy();
                }
            }
        });  pag.setPageCount(ctr.sizeStudents()/20+1);
        pag.setCurrentPageIndex(0);
    }
    /*
        sets the service and initialize componets
     */
    public void setService(ControllerDB ctr)
    {
        this.ctr=ctr;
        this.ctr.addObserver(this);
        initTableView();
        initPagination();
        ctr.fetchForwardStudents();
        setTableViewItemsCatchy();
        setNumberOfStudents();
    }
    /*
    check if filters are active
     */

    private boolean filtersAreActive()
    {
        if (!numeF.getText().isEmpty())
            return true;
        if (!grupaF.getText().isEmpty())
            return true;
        if (!emailF.getText().isEmpty())
            return true;
        if (!nrMatricolF.getText().isEmpty())
            return true;
        if (!profF.getText().isEmpty())
            return true;
        return false;
    }

    /*
        returns the predicate
     */
    private Predicate<Student> getFilterPredicate()
    {
        Predicate<Student> p =x->x.getNume().compareTo("")!=0;

        String n=numeF.getText();
        if(!n.isEmpty())//1
        {
            Predicate<Student> a =
                    x-> x.getNume().contains(numeF.getText());
            p=p.and(a);
        }
        String e=emailF.getText();
        if(!e.isEmpty())//2
        {
            Predicate<Student> b=x->x.getEmail().contains(e);
            p=p.and(b);
        }
        String prf=profF.getText();
        if(!prf.isEmpty())//3
        {
            Predicate<Student > c=x->x.getProf().contains(prf);
            p=p.and(c);
        }
        if(!grupaF.getText().isEmpty())
        {
            try//4
            {
                int g =Integer.parseInt(grupaF.getText());
                if(g<=0)
                    throw new NumberFormatException("Grupa invalida!");
                Predicate<Student> d=x->x.getGrupa()==g;
                p=p.and(d);
            }
            catch (NumberFormatException ex)
            {
                showMessage(ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
        if(!nrMatricolF.getText().isEmpty())
        {
            try//5
            {
                int nr = Integer.parseInt(nrMatricolF.getText());
                if (nr < 1)
                    throw new NumberFormatException("Numar matricol invalid!");
                Predicate<Student> f = x -> x.getNrMatricol() == nr;
                p=p.and(f);
            } catch (NumberFormatException ex)
            {
                showMessage(ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
        return p;
    }
    /*
        notify me as observer
     */
    @Override
    public void notifyMe()
    {
        setTableViewItemsCatchy();
        setNumberOfStudents();
    }

    /*
        return my observer type
     */
    @Override
    public ObserverType getObserverType()
    {
        return ObserverType.Student;
    }

    /*
        onkey releeased handler dor text boxes for filters
     */
    @FXML
    private void OnFilterActive()
    {
        try
        {
            setTableViewItems(ctr.filterStudents(getFilterPredicate()));
        }
        catch (Exception e )
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
