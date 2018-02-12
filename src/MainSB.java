import Controllers.*;
import Domains.Student;
import Repositoryes.DBMarkRepository;
import Repositoryes.DBStudentRepository;
import Repositoryes.DBHomeworkRepository;
import Services.MainService;
import Validators.MarkValidator;
import Validators.StudentValidator;
import Validators.HomeworkValidator;
import fxmlFiles.clasa;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Random;
class formDecorator extends AnchorPane
{
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage primaryStage;

    public formDecorator(Stage stage, Node node) {
        super();

        primaryStage = stage;
        this.setPadding(new javafx.geometry.Insets(0, 0, 0, 0));

        this.getChildren().addAll(node);

        this.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        this.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }
}

public class MainSB extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    private void addMany(DBStudentRepository dbsr) throws Exception
    {
         dbsr.connect("prof","cocolino");
            for(int i=0;i<150;i++)
            {
                System.out.println(i);
               // int gr=i;
                String a="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                int ln;
                Random r= new Random();
                ln=r.nextInt(10);
                String nume="";
                for(int j=0;j<ln;j++)
                {
                    Random rn = new Random();

                    nume+=a.charAt(rn.nextInt(52));
                }
                ln=r.nextInt(10);
                String email="" ;
                for (int j=0;j<ln;j++)
                {
                    Random rn = new Random();
                    email+=a.charAt(rn.nextInt(52));
                }
                ln=r.nextInt(10);
                String prf="";
                for (int j=0;j<ln;j++)
                {
                    Random rn = new Random();
                    prf+=a.charAt(rn.nextInt(52));
                }
                Student s = new Student(i,nume,i,email,prf);
                dbsr.add(s);
            }
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Gestiune note laborator!");
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(clasa.class.getResource( "main.fxml"));

        try
        {///***************************************
            DBStudentRepository dbsr= new DBStudentRepository();
           //addMany(dbsr);
            Parent root = loader.load();
            primaryStage.setScene(new Scene(new formDecorator(primaryStage,root)));
            //primaryStage.setScene(new Scene(root));//// ,800,525));

            primaryStage.initStyle(StageStyle.UNDECORATED);

            primaryStage.show();
            primaryStage.setResizable(false);

            MainService cd=loader.getController();
            ControllerDB controllerDB= new ControllerDB(new DBStudentRepository(),new DBHomeworkRepository(),new DBMarkRepository(),new StudentValidator(),new HomeworkValidator(),new MarkValidator());
            cd.setController(controllerDB);
            cd.init();
        ///********************************************
        }
        catch (Exception e)
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }
}
