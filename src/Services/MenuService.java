package Services;

import Controllers.ControllerDB;
import Utils.User;
import fxmlFiles.clasa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


import java.io.IOException;
import java.sql.SQLException;

public class MenuService
{
    private ControllerDB ctr;
    private TabPane tabPane;
    private Tab students;
    private Tab note;
    private Tab teme;
    private Tab rap;
    /*
        sets the controller and the main tab pane
     */
    public void setCtrAndTab(ControllerDB ctr, TabPane tabPane)
    {
        this.ctr = ctr;
        this.tabPane=tabPane;
    }
    /*
        opens the studens tab
     */
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
}
