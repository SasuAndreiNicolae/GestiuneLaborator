package Services;

import Controllers.ControllerDB;
import Domains.Homework;
import Domains.Student;
import Utils.MyPdfWriter;
import Utils.User;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;


import javax.imageio.ImageIO;
import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


public class ReportsService
{

    @FXML
    private PieChart pie;
    private TableView tabel;
    @FXML
    private AnchorPane lay;

    private ControllerDB ctr;

    @FXML
    private Button export;

    private  int nrNote14=0;
    private int nrNote45=0;
    private int nrNote58=0;
    private  int nrNote810=0;

    /*
        sets the controller
     */
    public void setCtr(ControllerDB ctr)
    {
        this.ctr = ctr;
        initConnections();
        export.setDisable(true);
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
    /*
        initialize connection to database
     */
    public void initConnections()
    {
        try
        {
            ctr.ConnectToStudentDatabase(User.getUsername(), User.getPassword());
            ctr.ConnectToMarkDatabase(User.getUsername(), User.getPassword());
            ctr.ConnectToHomeworkDatabase(User.getUsername(), User.getPassword());
        } catch (SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }

    }
    /*
    crates image with piechart
     */
    private String createTableImage()
    {
        WritableImage image = pie.snapshot(new SnapshotParameters(),null);
        File f = new File("img.png");
        try
        {
            ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",f);
            return "img.png";
        }
        catch (Exception e)
        {
        }
        return null;
    }
    /*
        writes a pds with a table of students
     */
    private void writeStudentsPdf(String title, String phrase, List<Student> tableElems)
    {   String filename="pdf.pdf";
        TextInputDialog dialog= new TextInputDialog();
        dialog.setTitle("Nume fisier");
        dialog.setHeaderText("Introduceti nume fisier(fara extensie)");
        dialog.setContentText("Nume fisier:");
        Optional<String> res = dialog.showAndWait();
        if(res.isPresent())
            filename=res.get()+".pdf";

        MyPdfWriter pdfWriter = new MyPdfWriter(filename);
        pdfWriter.addTitle(title);
        pdfWriter.addText(phrase);
        pdfWriter.createTable(5);
        String[] headers={"Numar matricol","Nume","Grupa","Email","Profesor"};
        pdfWriter.addTableHeader(headers);

       tableElems.forEach((x)->
        {
            String[] line = {String.valueOf(x.getNrMatricol()),x.getNume(),String.valueOf(x.getGrupa()),x.getEmail(),x.getProf()};
            pdfWriter.addRow(line);
        });
        pdfWriter.addTable();
        String path = createTableImage();
        if(path!=null)
        {
            pdfWriter.addImage(path);
            File f = new File(path);
            f.delete();
        }
        pdfWriter.close();
    }
    /*
        writes a pdf document with a table of homework
     */
    private void writeHomeworkPdf(String title,String phrase,List<Homework>items)
    {
        String filename="pdf.pdf";
        TextInputDialog dialog= new TextInputDialog();
        dialog.setTitle("Nume fisier");
        dialog.setHeaderText("Introduceti nume fisier(fara extensie)");
        dialog.setContentText("Nume fisier:");
        Optional<String> res = dialog.showAndWait();
        if(res.isPresent())
            filename=res.get()+".pdf";

        MyPdfWriter pdfWriter = new MyPdfWriter(filename);
        pdfWriter.addTitle(title);
        pdfWriter.addText(phrase);
        pdfWriter.createTable(3);
        String[] headers={"Numar Tema","Enunt","Termen Limita"};
        pdfWriter.addTableHeader(headers);
        items.forEach((x)->
        {
            String[] line={String.valueOf(x.getNrTema()),x.getEnunt(),String.valueOf(x.getTermenLimita())};
            pdfWriter.addRow(line);
        });
        pdfWriter.addTable();
        String path = createTableImage();
        if(path!=null)
        {
            pdfWriter.addImage(path);
            File f = new File(path);
            f.delete();
        }
        pdfWriter.close();
    }
    /*
        empties the piechart
     */
    private void unsetPieData()
    {
        pie.setData(FXCollections.observableArrayList());
    }
    /*
        writes a student pdf with grades for each student
     */
    private void writeStudentsGradesPdf(String title,String phrase)
    {
        String filename="pdf.pdf";
        TextInputDialog dialog= new TextInputDialog();
        dialog.setTitle("Nume fisier");
        dialog.setHeaderText("Introduceti nume fisier(fara extensie)");
        dialog.setContentText("Nume fisier:");
        Optional<String> res = dialog.showAndWait();
        if(res.isPresent())
            filename=res.get()+".pdf";

        MyPdfWriter pdfWriter= new MyPdfWriter(filename);
        pdfWriter.addTitle(title);
        pdfWriter.addText(phrase);
        pdfWriter.createTable(6);
        String[]headers={"Numar matricol","Nume","Grupa","Email","Profesor","Medie"};
        pdfWriter.addTableHeader(headers);
        ObservableList<TableColumn> cols =tabel.getColumns();
        int rows=tabel.getItems().size();

        for (int i=0; i<rows;i++)
        {
            String[] row = new String[6];
            row[0]=cols.get(0).getCellObservableValue(i).getValue().toString();
            row[1]=cols.get(1).getCellObservableValue(i).getValue().toString();
            row[2]=cols.get(2).getCellObservableValue(i).getValue().toString();
            row[3]=cols.get(3).getCellObservableValue(i).getValue().toString();
            row[4]=cols.get(4).getCellObservableValue(i).getValue().toString();
            row[5]=cols.get(5).getCellObservableValue(i).getValue().toString();
            pdfWriter.addRow(row);
        }

        pdfWriter.addTable();
        String path = createTableImage();
        if(path!=null)
        {
            pdfWriter.addImage(path);
            File f = new File(path);
            f.delete();
        }
        pdfWriter.close();
    }

    /*
        creates and displays a table wich contains students and each mark
     */
    @FXML
    private void setTableForStudentsMarks()
    {
        unsetPieData();
        if (tabel != null)
        {
            lay.getChildren().remove(tabel);
        }
        export.setDisable(true);
        TableView<Student> t = new TableView<>();

        TableColumn<Student, Integer> nrMat = new TableColumn<>("Numar matricol");
        TableColumn<Student, String> nume = new TableColumn<>("Nume");
        TableColumn<Student, Integer> grupa = new TableColumn<>("Grupa");
        TableColumn<Student, String> email = new TableColumn<>("Email");
        TableColumn<Student, String> prof = new TableColumn<>("Profesor");
        TableColumn<Student, Float> medie = new TableColumn<>("Medie");

        nrMat.setCellValueFactory(new PropertyValueFactory<>("nrMatricol"));
        nume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        grupa.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        prof.setCellValueFactory(new PropertyValueFactory<>("prof"));

        medie.setCellValueFactory(param ->
        {

            float m = 0f;
            ObservableValue<Float> x = null;
            if (param != null)
            {
                try
                {
                    m = ctr.Avg(param.getValue().getNrMatricol());
                } catch (SQLException e)
                {
                    System.out.println(e.getMessage());
                }
                x = new SimpleFloatProperty(m).asObject();
            }
            return x;
        });

        t.getColumns().addAll(nrMat, nume, grupa, email, prof, medie);
        ObservableList<Student> x = FXCollections.observableArrayList();
        try
        {
            ctr.findAllStudents().forEachRemaining((s) -> x.add(s));
            t.setItems(x);
        } catch (Exception e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
        t.setMinWidth(550);
        AnchorPane.setRightAnchor(t, (double) 0);
        AnchorPane.setBottomAnchor(t, (double) 0);
        AnchorPane.setTopAnchor(t, (double) 0);

        lay.getChildren().addAll(t);
        tabel = t;

        int n=ctr.sizeStudents();
        if(n>0)
        {
            try
            {
                Iterator<Student> it=ctr.findAllStudents();
                while (it.hasNext())
                {
                    try
                    {
                        float m = ctr.Avg(it.next().getNrMatricol());
                        if (m>=0&&m<4)
                            nrNote14++;
                        if(m>=4&&m<5)
                            nrNote45++;
                        if(m>=6&&m<8)
                            nrNote58++;
                        if(m>8)
                            nrNote810++;
                    }
                    catch (SQLException e)
                    {
                    }
                }
            }
            catch (Exception e)
            {
            }
            ObservableList<PieChart.Data> pieList= FXCollections.observableArrayList();
        double pnb= (100*nrNote14)/n;
        pieList.add(new PieChart.Data("Medii[1,4]",pnb));
        pnb=(100*nrNote45)/n;

        pieList.add(new PieChart.Data("Medii[4-5)",pnb));
        pnb=(100*nrNote58)/n;

        pieList.add(new PieChart.Data("Medii[5-8)",pnb));
        pnb=(100*nrNote810)/n;

        pieList.add(new PieChart.Data("Medii[8-10]",pnb));

        pie.setData(pieList);
        export.setOnAction(event -> writeStudentsGradesPdf("Medii studenti","Media fiecarui student."));export.setDisable(false);
        }
    }

    /*
        creates and display a table with students that jave the average of grades at least 5
     */
    @FXML
    private void setTableForPassedStudents()
    {
        unsetPieData();
        if (tabel != null)
        {
            lay.getChildren().remove(tabel);
        }
        export.setDisable(true);
        TableView<Student> t = new TableView<>();
        TableColumn<Student, Integer> nrMat = new TableColumn<>("Numar matricol");
        TableColumn<Student, String> nume = new TableColumn<>("Nume");
        TableColumn<Student, Integer> grupa = new TableColumn<>("Grupa");
        TableColumn<Student, String> email = new TableColumn<>("Email");
        TableColumn<Student, String> prof = new TableColumn<>("Profesor");
        TableColumn<Student, Float> medie = new TableColumn<>("Medie");

        nrMat.setCellValueFactory(new PropertyValueFactory<>("nrMatricol"));
        nume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        grupa.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        prof.setCellValueFactory(new PropertyValueFactory<>("prof"));
        t.getColumns().addAll(nrMat, nume, grupa, email, prof, medie);

        medie.setCellValueFactory(param ->
        {
            Float f = 0f;
            if (param != null)
            {
                try
                {
                    f = ctr.Avg(param.getValue().getNrMatricol());
                } catch (SQLException e)
                {
                }
            }
            return new SimpleFloatProperty(f).asObject();
        });

        ObservableList<Student> x = FXCollections.observableArrayList();
        try
        {
            ctr.findAllStudents().forEachRemaining((s) ->
                    {
                        try
                        {
                            Float m = ctr.Avg(s.getNrMatricol());
                            if (m.compareTo(5.0f) > 0)
                                x.add(s);
                        } catch (Exception e)
                        {
                        }
                    }
            );
        } catch (SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
        t.setItems(x);
        tabel = t;
        t.setMinWidth(550);

        AnchorPane.setTopAnchor(t, (double) 0);
        AnchorPane.setBottomAnchor(t, (double) 0);
        AnchorPane.setRightAnchor(t, (double) 0);

        lay.getChildren().add(t);
        if(x.size()>0)
        {
            export.setOnAction( event -> {
                writeStudentsPdf("Studenti promovati","Tabelul cu toti studentii cu media notelor de la laborator mai mare sau egala cu 5,",x);
            });
        }

        int nrS=ctr.sizeStudents();
        int nrsP=x.size();
        int nrStudentiNepromovati=nrS-nrsP;
        ObservableList<PieChart.Data> pieList = FXCollections.observableArrayList();
        pieList.add(new PieChart.Data("Nepromovati",nrStudentiNepromovati));
        pieList.add(new PieChart.Data("Promovati",nrsP));
        pie.setData(pieList);
        export.setDisable(false);
    }

    /*
        displays in a table the most difficult homework
     */
    @FXML
    private void hardestHomework()
    {
            initConnections();
        unsetPieData();
        if (ctr.sizeHomework() > 0)
        {
            if (tabel != null)
                lay.getChildren().remove(tabel);
            export.setDisable(true);
            TableView<Homework> t = new TableView<>();

            TableColumn<Homework, Integer> nrTema = new TableColumn<>("nrTema");
            TableColumn<Homework, String> enunt = new TableColumn<>("Enunt");
            TableColumn<Homework, Integer> lim = new TableColumn<>("Termen limita");

            nrTema.setCellValueFactory(new PropertyValueFactory<>("nrTema"));
            enunt.setCellValueFactory(new PropertyValueFactory<>("enunt"));
            lim.setCellValueFactory(new PropertyValueFactory<>("termenLimita"));
            t.getColumns().addAll(nrTema, enunt, lim);

            ObservableList<Homework> x = FXCollections.observableArrayList();
            try
            {
                Homework h = ctr.hardestHomework();
                x.add(h);
            } catch (SQLException e)
            {
                showMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
            t.setItems(x);
            tabel = t;
            t.setMinWidth(550);
            AnchorPane.setRightAnchor(t, 0d);
            AnchorPane.setBottomAnchor(t, 0d);
            AnchorPane.setTopAnchor(t, 0d);

            lay.getChildren().add(t);

            export.setOnAction(event -> writeHomeworkPdf("Tema cea mai dificila","Este tema pentru care s-au acordat cele mai mici note.",x));
            export.setDisable(false);
        }
    }

    /*
        creates and display a table with stdudents that have no penalization
     */
    @FXML
    private void studentsWithNoPenalization()
    {
        unsetPieData();
        if(tabel!=null)
        {
            lay.getChildren().remove(tabel);
            tabel=null;
        }
        export.setDisable(true);
        TableView<Student> t = new TableView<>();
        TableColumn<Student,Integer> nrMatricol = new TableColumn<>("Numar matricol");
        TableColumn<Student,String> nume = new TableColumn<>("Nume");
        TableColumn<Student,Integer> gr = new TableColumn<>("Grupa");
        TableColumn<Student,String> email = new TableColumn<>("Email");
        TableColumn<Student,String> prof = new TableColumn<>("Profesor");

        nrMatricol.setCellValueFactory(new PropertyValueFactory<>("nrMatricol"));
        nume.setCellValueFactory(new PropertyValueFactory<>("nume"));
        gr.setCellValueFactory(new PropertyValueFactory<>("grupa"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        prof.setCellValueFactory(new PropertyValueFactory<>("prof"));


        t.getColumns().addAll(nrMatricol,nume,gr,email,prof);
        ObservableList<Student> x = FXCollections.observableArrayList();
        try
        {
            initConnections();
            ctr.getMarksWithNoPenalization().forEach((mark ->
            {
                try
                {
                    Student s = ctr.findOneStudent(mark.getIdStudent());
                    if(!x.contains(s))
                        x.add(s);
                }
                catch (Exception e)
                {
                }
            }));
        }
        catch (SQLException e)
        {
            showMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
        t.setItems(x);
        tabel=t;
        t.setMinWidth(550);

        AnchorPane.setTopAnchor(t,0d);
        AnchorPane.setBottomAnchor(t,0d);
        AnchorPane.setRightAnchor(t,0d);
        lay.getChildren().add(t);
        export.setOnAction(event -> writeStudentsPdf("Stidenti fara penalizare","Studemti care nu au intarziat deloc cu laboratoarele.",x));

        int n = ctr.sizeMarks();
        int nrNoPenalization=x.size();
        double procentNoPenlization=(nrNoPenalization*100)/n;
        ObservableList<PieChart.Data> pieList= FXCollections.observableArrayList();
        pieList.add(new PieChart.Data("Fara penalizare",procentNoPenlization));
        pieList.add(new PieChart.Data("Restul",100-procentNoPenlization));
        pie.setData(pieList);
        export.setDisable(false);
    }

}