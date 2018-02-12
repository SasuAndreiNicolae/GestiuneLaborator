package Services;


import Controllers.ControllerDB;
import Domains.Mark;
import Domains.Student;
import Domains.Homework;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.Iterator;



public class AddEditMarkService
{
    @FXML
    private ComboBox<Student> idStudentCb;
    @FXML
    private ComboBox<Homework> nrTemaCb;
    @FXML
    private Button action;
    @FXML
    private TextField valoare;
    @FXML
    private Label title;
    @FXML
    private TextField obs;
    private String modify=null;
    private Mark toAddEditMark;


    private ControllerDB ctr;

    public AddEditMarkService()
    {
    }

    /*
        handler for cancel button
     */
    @FXML
    private void anuleaza()
    {
        ((Stage) nrTemaCb.getScene().getWindow()).close();
    }
    /*
        sets controller
     */
    public void setControllers(ControllerDB ctr)
    {
        this.ctr=ctr;
    }
    /*
        on action button handler
     */
    @FXML
    private void setOnAction()
    {
        try
        {
            float f =Float.parseFloat( valoare.getText());

            toAddEditMark.setValoare(f);
            toAddEditMark.setNrTema(nrTemaCb.getSelectionModel().getSelectedItem().getNrTema());
            toAddEditMark.setIdStudent(idStudentCb.getSelectionModel().getSelectedItem().getNrMatricol());
            if(modify!=null)
                modify=obs.getText();
            Stage s =(Stage) action.getScene().getWindow();
            s.fireEvent(new WindowEvent(s, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        catch (NumberFormatException e)
        {
            showMessage("Valoare nota gresita", Alert.AlertType.ERROR);
        }
    }
    /*
        tells the window that is used for editig the mark entity
     */
    public void setAddEditMarkModify(String modify)
    {
        nrTemaCb.setVisible(false);
        idStudentCb.setVisible(false);
        obs.setVisible(true);
        this.modify=modify;
    }
    /*
        sets the text for action buton
     */
    public void setActionText(String text)
    {
        action.setText(text);
        title.setText(text);
    }
    /*
        sets the mark entity to update/add
     */
    public void setToAddEditMark(Mark toAddEditMark)
    {
        this.toAddEditMark = toAddEditMark;
    }

    private void showMessage(String msg, Alert.AlertType at)
    {
        Alert a = new Alert(at);
        a.setContentText(msg);
        a.showAndWait();
    }

    /*
        selects the student and homework when used for editing
     */
    public void selectStudentAndHomework(Student s, Homework t, float v)
    {
        idStudentCb.getSelectionModel().select(s);
        nrTemaCb.getSelectionModel().select(t);
        valoare.setText(String.valueOf(v));
    }
    /*
        selects the studen from combobox
     */
    public void selectStudent(Student s)
    {
        idStudentCb.getSelectionModel().select(s);

    }
    /*
        selects the homework from combobox
     */
    public  void selectHomework(Homework h)
    {
        nrTemaCb.getSelectionModel().select(h);
    }

    /*
        sets the students combobox items
     */
    private void setIdStudentCbItems()
    {
        ObservableList<Student>x = FXCollections.observableArrayList();
        try
        {
            Iterator<Student> it=ctr.findAllStudents();
            it.forEachRemaining(x::add);
            idStudentCb.setItems(x);
        }
        catch (SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        sets the homework combobox items
     */
    private void setNrHomeworkCbItems()
    {
        ObservableList<Homework> y =FXCollections.observableArrayList();
        try
        {
            ctr.findAllHomework().forEachRemaining(y::add);
            nrTemaCb.setItems(y);

        }
        catch (SQLException e )
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    /*
        initialize components
     */
    public void init()
    {
      setIdStudentCbItems();
      setNrHomeworkCbItems();
        obs.setVisible(false);

    }
}
