package Services;

import Domains.Homework;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class AddEditHomeworkService
{
    @FXML
    private AnchorPane mainStage;
    @FXML
    private ComboBox<Integer> termenComboBox;
    @FXML
    private TextArea ta;
    @FXML
    private Button action;
    @FXML
    private Label title;
    private Homework toAddEditHomework;

    /*
        cancel button handler
     */
    @FXML
    private void onActionCancel()
    {
        ((Stage)mainStage.getScene().getWindow()).close();
    }
    /*
        action button handler
     */
    @FXML
    private void onAction()
    {
        if(termenComboBox.getSelectionModel().getSelectedItem()!=null)
        {
            toAddEditHomework.setTermenLimita(termenComboBox.getSelectionModel().getSelectedItem());
            toAddEditHomework.setEnunt(ta.getText());
            Stage s = ((Stage) mainStage.getScene().getWindow());
            s.fireEvent(new WindowEvent(s, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        else showMessage("Nu ati selectat termen limita", Alert.AlertType.INFORMATION);

    }
    /*
    displays a message
 */
    private void showMessage(String msg, Alert.AlertType at)
    {
        Alert a = new Alert(at);
        a.setContentText(msg);
        a.showAndWait();
    }
    public AddEditHomeworkService()
    {

    }
    /*
        sets the homework entity used for adding/modifying
     */
    public void setToAddEditHomework(Homework toAddEditHomework)
    {
        this.toAddEditHomework = toAddEditHomework;
    }

    /*
        initialize combobox with weeks
     */
    public void init()
    {
        ObservableList<Integer> x = FXCollections.observableArrayList();
        x.addAll(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
        termenComboBox.setItems(x);
    }
    /*
        sets the acton button tet and window title
     */
    public void setTextAction(String Btext,String Ltext)
    {
        action.setText(Btext);
        title.setText(Ltext);
    }
    /*
        sets the values for enunt textfield
     */
    public void setValues(String enunt,int week)
    {
        ta.setText(enunt);
        termenComboBox.getSelectionModel().select((Integer)week);
    }


}
