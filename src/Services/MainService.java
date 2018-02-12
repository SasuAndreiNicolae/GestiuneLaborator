package Services;

import Controllers.ControllerDB;
import Utils.User;
import fxmlFiles.clasa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;

import java.util.Calendar;


public class MainService
{
    private ControllerDB ctr;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button logInButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private ComboBox<Integer> saptComboBox;
    @FXML
    private Tab logTab;
    @FXML
    private Button studentButton;
    @FXML
    private Button homeworkButton;
    @FXML
    private Button markButton;
    @FXML
    private Button raportButton;
    @FXML
    private Hyperlink logOutLabel;

    @FXML
    private PieChart pie;
    private Tab students;
    private Tab note;
    private Tab teme;
    private Tab rap;



    public MainService() //throws  Exception
    {
    }
    /*
        sets the controller
     */
    public void setController(ControllerDB ctr)
    {
        this.ctr = ctr;
    }

    /*
        login button handler
     */
    @FXML
    private void LogInButton()
    {
            try
            {
                //ctr.ConnectToStudentDatabase("prof", "cocolino");
                ctr.ConnectToStudentDatabase(usernameTextField.getText(),passwordTextField.getText());

                User.set(usernameTextField.getText(),passwordTextField.getText(),saptComboBox.getSelectionModel().getSelectedItem());
                //logTab.setDisable(true);
                enableMenuButtons();
                disableLogInControls();
            }
            catch (SQLException e)
            {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText(e.getMessage());
                a.showAndWait();
            }
    }
    /*

     */
    @FXML
    private void LogOut()
    {
        if(this.students!=null)
        {
            tabPane.getTabs().remove(students);
            students=null;
        }
        if(this.note!=null)
        {
            tabPane.getTabs().remove(note);
            note=null;
        }
        if(this.teme!=null)
        {
            tabPane.getTabs().remove(teme);
            teme=null;
        }
        if(this.rap!=null)
        {
            tabPane.getTabs().remove(rap);
            rap=null;
        }
        enableLogInControls();
        disableMenuButtons();
        while (ctr.fetchBackwardHomework()>0);
        while (ctr.fetchForwardMarks()>0);
        while (ctr.fetchBackwardStudents()>0);
    }
    /*
        disables log in controls
     */
    private void disableLogInControls()
    {
        this.usernameTextField.setDisable(true);
        this.passwordTextField.setDisable(true);
        this.logInButton.setDisable(true);
        this.saptComboBox.setDisable(true);
        this.logOutLabel.setDisable(false);
    }
    /*
        enables log in controls
     */
    private void enableLogInControls()
    {
        this.usernameTextField.setDisable(false);
        this.passwordTextField.setDisable(false);
        this.logInButton.setDisable(false);
        this.saptComboBox.setDisable(false);
        this.logOutLabel.setDisable(true);
    }
    /*
    disables te menu buttons
     */
    private void disableMenuButtons()
    {
        studentButton.setDisable(true);
        homeworkButton.setDisable(true);
        markButton.setDisable(true);
        raportButton.setDisable(true);
    }
    /*
        enables the menu buttons
     */
    private void  enableMenuButtons()
    {
        studentButton.setDisable(false);
        homeworkButton.setDisable(false);
        markButton.setDisable(false);
        raportButton.setDisable(false);
    }
    /*
    init components
     */
    public void init()
    {
        ObservableList<Integer> x = FXCollections.observableArrayList();
        x.addAll(1,2,3,4,5,6,7,8,9,10,11,12,13,14);
        saptComboBox.setItems(x);
        saptComboBox.getSelectionModel().select(0);
        disableMenuButtons();

    }

    @FXML
    private void goToStudents()
    {

        if(students==null)
        {
            FXMLLoader loader= new FXMLLoader(fxmlFiles.clasa.class.getResource("StudentsFxml.fxml"));
            try
            {
                Parent r = loader.load();
                students=new Tab("Studenti",r);
                tabPane.getTabs().add(students);

                tabPane.getSelectionModel().select(students);
                StudentService sv=loader.getController();
                sv.setService(ctr);



            }
            catch (IOException e)
            {
                showMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else tabPane.getSelectionModel().select(students);

    }
    /*
        diplays a message
     */
    private void showMessage(String msg, Alert.AlertType at)
    {
        Alert a = new Alert(at);
        a.setContentText(msg);
        a.showAndWait();

    }
    /*
        go to raports
     */
    @FXML
    private void goToRapoarte()
    {
        if(this.rap==null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(clasa.class.getResource("Rapoarte.fxml"));
                Parent r = loader.load();
                ReportsService reportsService = loader.getController();
                rap = new Tab("Rapoate", r);
                tabPane.getTabs().addAll(rap);
                tabPane.getSelectionModel().select(rap);
                reportsService.setCtr(this.ctr);


            } catch (Exception e)
            {
                showMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else tabPane.getSelectionModel().select(rap);
    }
    /*
        opens mark window
     */
    @FXML
    private void goToMarks()
    {
        if(note==null)
        {
            try
            {
                FXMLLoader loader= new FXMLLoader();
                loader.setLocation(clasa.class.getResource("NoteFxml.fxml"));
                Parent r =loader.load();
                note=new Tab("Note",r);
                tabPane.getTabs().addAll(note);
                tabPane.getSelectionModel().select(note);
                MarkService ns =loader.getController();

                ns.setCtr(ctr);
                try
                {

                    ctr.ConnectToMarkDatabase(User.getUsername(), User.getPassword());
                }
                catch (SQLException e)
                {
                    showMessage(e.getMessage(), Alert.AlertType.ERROR);

                }
                ns.init();
            }
            catch (IOException e)
            {
                showMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else tabPane.getSelectionModel().select(note);
    }
    /*
        opens homework tab
     */
    @FXML
    private void goToHomeworks()
    {
        if(teme==null)
        {
            try
            {
                FXMLLoader loader= new FXMLLoader();
                loader.setLocation(clasa.class.getResource("TemeFxml.fxml"));
                Parent r =loader.load();
                teme=new Tab("Teme",r);
                tabPane.getTabs().addAll(teme);
                tabPane.getSelectionModel().select(teme);
                HomeworkService ts =loader.getController();
                ts.setCtr(ctr);
                ctr.ConnectToHomeworkDatabase(User.getUsername(),User.getPassword());
                ctr.ConnectToMarkDatabase(User.getUsername(),User.getPassword());
                // ctr.Connect(User.getUsername(),User.getPassword());
                ts.init();

            }
            catch (IOException|SQLException e)
            {
                e.printStackTrace();
            }
        }
        else tabPane.getSelectionModel().select(teme);


    }
    @FXML
    private void exit()
    {
        ((Stage) tabPane.getScene().getWindow()).close();
    }
    @FXML
    private void autoWeek()
    {
        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MONTH);
        System.out.println(calendar.get(Calendar.WEEK_OF_MONTH));
        int week = -1;
        switch (m)
        {
            case 0:
                if (calendar.get(Calendar.WEEK_OF_MONTH) < 3)
                    m = calendar.get(Calendar.WEEK_OF_MONTH) + 12;
                else m = -1;
                break;
            case 1:
                if (calendar.get(Calendar.WEEK_OF_MONTH) == 4)
                    m = 1;
                else m = -1;
                break;
            case 2:
                m = calendar.get(Calendar.WEEK_OF_MONTH) + 1;
                break;
            case 3:
                m = calendar.get(Calendar.WEEK_OF_MONTH) + 5;
                break;
            case 4:
                m = calendar.get(Calendar.WEEK_OF_MONTH) + 9;
                break;
            case 5:
                if (calendar.get(Calendar.WEEK_OF_MONTH) == 1)
                    m = 14;
                else m = -1;
                break;
            case 9:
                m = calendar.get(Calendar.WEEK_OF_MONTH);
                break;
            case 10:
                m = calendar.get(Calendar.WEEK_OF_MONTH) + 4;
                break;
            case 12:
                if (calendar.get(Calendar.WEEK_OF_MONTH) < 4)
                    m = 12;
                else m = -1;
                break;
            default:
                m = -1;
        }
        System.out.println(m);
        if (m > 0)
        {
            this.saptComboBox.getSelectionModel().select(m-1);
        }
        else
            showMessage("Nu s-a putu determina!", Alert.AlertType.INFORMATION);
    }
}
