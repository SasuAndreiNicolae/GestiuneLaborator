package Services;

import Domains.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class AddEditStudentService
{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button action;
    @FXML
    private TextField nume;
    @FXML
    private TextField email;
    @FXML
    private TextField grupa;
    @FXML
    private TextField prof;

    private Student s;

    public AddEditStudentService()
    {
    }

    /*
        action button handler
     */
    @FXML
    private void pressedButton()
    {
        s.setNume(nume.getText());
        s.setEmail(email.getText());
        s.setProf(prof.getText());
        try
        {
            int gr = Integer.parseInt(grupa.getText());
            s.setGrupa(gr);
        } catch (NumberFormatException e)
        {
            System.out.println("nr gresittt");
            s.setGrupa(0);
        }
        Stage s = (Stage) anchorPane.getScene().getWindow();
        s.fireEvent(new WindowEvent(s, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    /*
        sets the student to add/edit
     */
    public void setStudent(Student s)
    {
        this.s = s;
    }
    /*
        sets the text fields  text for editing
     */
    public void setTextFieldsText(String nume, int grupa, String email, String prof)
    {
        this.nume.setText(nume);
        this.grupa.setText(String.valueOf(grupa));
        this.email.setText(email);
        this.prof.setText(prof);
    }
    /*
        sets the action buttin text
     */
    public void setButtonText(String text)
    {
        action.setText(text);
    }
    /*
        cancel button handler
     */
    @FXML
    private void anuleaza()
    {
        ((Stage)anchorPane.getScene().getWindow()).close();
    }
}
