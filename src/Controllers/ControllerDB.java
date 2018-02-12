package Controllers;

import Domains.Homework;
import Domains.Mark;
import Domains.Student;
import Exceptions.MarkException;
import Exceptions.StudentValidatorException;
import Exceptions.HomeworkValidatorException;
import Observer.*;
import Repositoryes.DBHomeworkRepository;
import Repositoryes.DBMarkRepository;

import Repositoryes.DBStudentRepository;
import Utils.EmailSender;
import Utils.User;
import Validators.HomeworkValidator;
import Validators.MarkValidator;
import Validators.StudentValidator;

import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class ControllerDB implements Observable
{
    private DBStudentRepository studentDBRepository;
    private DBHomeworkRepository homeworkDBRepository;
    private DBMarkRepository markDBRepository;

    private StudentValidator studentValidator;
    private HomeworkValidator homeworkValidator;
    private MarkValidator markValidator;

    private LinkedList<Observer> studentsObserverList;
    private LinkedList<Observer> homeworkObseverList;
    private LinkedList<Observer> marksObserverList;

    public ControllerDB(DBStudentRepository studentDBRepository, DBHomeworkRepository homeworkDBRepository, DBMarkRepository markDBRepository, StudentValidator studentValidator, HomeworkValidator homeworkValidator, MarkValidator markValidator)
    {
        this.studentDBRepository = studentDBRepository;
        this.homeworkDBRepository = homeworkDBRepository;
        this.markDBRepository = markDBRepository;
        this.studentValidator = studentValidator;
        this.homeworkValidator = homeworkValidator;
        this.markValidator = markValidator;
        studentsObserverList= new LinkedList<>();
        homeworkObseverList = new LinkedList<>();
        marksObserverList= new LinkedList<>();
    }
    /*
        adds Observer by type in its corespondent list
     */
    @Override
    public void addObserver(Observer o)
    {
        switch (o.getObserverType())
        {
            case Homework:
                homeworkObseverList.add(o);
                break;
            case Mark:
                marksObserverList.add(o);
                break;
            case Student:
                studentsObserverList.add(o);
                break;
        }
    }

    /*
    removes observer by type in its corespondent list
     */
    @Override
    public void removeObserver(Observer o)
    {
        switch (o.getObserverType())
        {
            case Homework:
                homeworkObseverList.remove(o);
                break;
            case Mark:
                marksObserverList.remove(o);
                break;
            case Student:
                studentsObserverList.remove(o);
                break;
        }
    }

    /*
    notifyes observers by types
     */
    @Override
    public void notifyObservers(ObserverType ot)
    {
        switch (ot)
        {
            case Student:
                studentsObserverList.forEach((x)->x.notifyMe());
                break;
            case Mark:
                marksObserverList.forEach((x)->x.notifyMe());
            case Homework:
                homeworkObseverList.forEach((x)->x.notifyMe());
        }

    }
    
    
    
    
    
    /////////////////////Homeworks


    /*
        validates adds homework  and notify observers
     */
    public void addHomework(Homework t) throws HomeworkValidatorException,SQLException
    {
        homeworkValidator.Validate(t);
        homeworkDBRepository.add(t);
        notifyObservers(ObserverType.Homework);
    }
    /*
       deletes Homeworks
     */
    public void deleteHomework(int id) throws SQLException
    {
        homeworkDBRepository.delete(id);
        notifyObservers(ObserverType.Homework);
    }
    /*
        finds one homework by its id
     */
    public Homework findOneHomework(int id)throws SQLException
    {
        return    homeworkDBRepository.findOne(id);
    }
    /*
        returns an iterator for homework corresponding to fetch
     */
    public Iterator<Homework> fetchFindAllHomework() throws SQLException
    {
        return homeworkDBRepository.fetchFindAll();
    }
    /*
        returns an interator coresponding to all homeworks
     */
    public Iterator<Homework> findAllHomework() throws SQLException
    {
        return  homeworkDBRepository.findAll();
    }
    /*
        updates a homework
     */
    public void Update(Homework t) throws HomeworkValidatorException,SQLException
    {

        homeworkValidator.Validate(t);
        homeworkDBRepository.Update(t);
        notifyObservers(ObserverType.Homework);
    }
    /*
        establish connection to database
     */
    public void ConnectToHomeworkDatabase(String username,String password) throws SQLException
    {
        homeworkDBRepository.connect(username,password);
    }
    /*
        returns an iterator on a filtered list by a predicate
     */
    public  Iterator<Homework> filterHomework(Predicate<Homework> p) throws SQLException
    {
        return homeworkDBRepository.filter(p);
    }
    /*
        returns the nummber of homeworks in database
     */
    public int sizeHomework()
    {
        return homeworkDBRepository.size();
    }
    /*
        fetches forward to the next set of homeworks
     */
    public int fetchForwardHomework()
    {
        return homeworkDBRepository.fetchForward();
    }
    /*
        fetches backward to the previous set of homeworks
     */
    public int fetchBackwardHomework()
    {
        return homeworkDBRepository.fetchBackward();
    }


    /*
        returns the homework with the minumum average of the given marks
     */
    public Homework hardestHomework() throws  SQLException
    {
        return this.homeworkDBRepository.hardestHomework();

    }


    ////////////////////////////////////////////////////
    
    
    
    ///////////////////////////////////////Students

    /*
        validates adds student and notify observers
     */
    public void AddStudent(Student s) throws StudentValidatorException,SQLException
    {
        studentValidator.Validate(s);
        studentDBRepository.add(s);
        notifyObservers(ObserverType.Student);
    }
    /*
        updates student
     */
    public void UpdateStudent(Student s) throws SQLException,StudentValidatorException
    {
        studentValidator.Validate(s);
        studentDBRepository.Update(s);
        notifyObservers(ObserverType.Student);
    }
    /*
        etablishes connection to database
     */
    public void ConnectToStudentDatabase(String usrname,String password) throws SQLException
    {
        studentDBRepository.connect(usrname,password);
    }
    /*
        finds the student corresponding to the id
     */
    public Student findOneStudent(int id) throws SQLException
    {
        return studentDBRepository.findOne(id);
    }
    /*
        returns an iterator corresponding to fetch
     */
    public Iterator<Student> fetchFindAll() throws SQLException
    {
        return studentDBRepository.fetchFindAll();
    }
    /*
        returns an iterator with all students in database
     */
    public Iterator<Student> findAllStudents() throws SQLException
    {
        return  this.studentDBRepository.findAll();
    }
    /*
        deletes a student coresponding to the id
     */
    public void deleteStudent(int id)throws SQLException
    {
        studentDBRepository.delete(id);
        notifyObservers(ObserverType.Student);
    }
    /*
        fetches forward to the next set of data in database
     */
    public int fetchForwardStudents()
    {
        return studentDBRepository.fetchForward();
    }
    /*
        returns the number of students in database
     */
    public int sizeStudents()
    {
        return studentDBRepository.size();
    }
    /*
        returns an iterator
     */
    public Iterator<Student> filterStudents(Predicate<Student>p) throws SQLException
    {
        return studentDBRepository.filter(p);
    }
    public int fetchBackwardStudents()
    {
        return studentDBRepository.fetchBackward();
    }

//////////////////////////////////////////////////////



    /////////////////////////////////////Marks


    /*
        writes a log file
     */
    private void writeLogFile(String tip ,Mark k ,String obs)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(k.getIdStudent())+".txt", true));

            writer.write((tip + ",Numar tema:" + k.getNrTema() + ",Nota obtinuta:" +k.getValoare()+",Termenul temei:"+ findOneHomework(k.getNrTema()).getTermenLimita() + ",Saptamana in care s-a predat:" + User.getWeek() + "," + obs));

            writer.newLine();

            writer.close();
        }
        catch (Exception e)
        {
        }
    }
    /*
        returns the content of a log file
     */
    private String getLogContent(int id)
    {
        String content="";
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(id) + ".txt"));
            String line="";
            while ((line=reader.readLine())!=null)
            {
                content+="\n"+line;
            }
            reader.close();
        } catch (Exception e)
        {
        }
        return content;
    }

    /*
        computes the penalization for a new mark and writes the log file
     */
    private void setPenalizationAndWriteLog(Mark m ) throws  SQLException
    {
        Homework h = findOneHomework(m.getNrTema());
        String obs="Totul a fost in ordine!";
        if (h.getTermenLimita() < User.getWeek())
        {
            float diff=User.getWeek()-h.getTermenLimita();
            float penalizare=diff*2;
            if(m.getValoare()-penalizare<1.0f)
                m.setValoare(1);
            else
                m.setValoare(m.getValoare()-penalizare);
            m.setPenalizare(penalizare);
            obs="Depunctare pt intarziere de " + diff + " saptamani";
        }
        writeLogFile("Adaugare nota",m,obs);
    }
    /*
        validates adds mark and nodify observers
     */
    public void addMark(Mark n) throws MarkException,SQLException
    {
        if(!findMarkForStudentAndHomework(n.getIdStudent(),n.getNrTema()))
        {
            markValidator.Validate(n);
            setPenalizationAndWriteLog(n);
            markDBRepository.add(n);
            EmailSender em = new EmailSender(findOneStudent(n.getIdStudent()).getEmail(), "Adaugare nota", getLogContent(n.getIdStudent()));
            notifyObservers(ObserverType.Mark);
        }
        else throw new MarkException(("Studentul are deja o nota la tema aleasa!"));
    }
    /*
        finds the mark corresponding to a student id and a homework number
     */
    public boolean findMarkForStudentAndHomework(int ids,int nrh) throws SQLException
    {
        return  markDBRepository.findMarkForStudentAndHomework(ids, nrh);
    }
    /*
        returns a list with marks with no penalization
     */
    public List<Mark> getMarksWithNoPenalization() throws  SQLException
    {
        return markDBRepository.WithGivenQuery("SELECT * FROM note where penalizare=0");
    }
    /*
        removes mark
     */
    public void removeMark(Mark n) throws SQLException
    {
        markDBRepository.delete(n.getIdNota());
        notifyObservers(ObserverType.Mark);
    }
    /*
        updates a mark
     */
    public  void UpdateMark(Mark n,String obs) throws MarkException,SQLException
    {
        if(n.getValoare()>findOneMark(n.getIdNota()).getValoare())
        {
            markValidator.Validate(n);
            markDBRepository.Update(n);
            writeLogFile("Modificare nota",n,obs);
            EmailSender em= new EmailSender(findOneStudent(n.getIdStudent()).getEmail(),"Modificare Nota",getLogContent(n.getIdStudent()));
            notifyObservers(ObserverType.Mark);
        }
        else throw  new MarkException("Se poate doar mari nota!");
    }
    /*
        finds the mark corresponding to the id

     */
    public Mark findOneMark(int id)throws SQLException
    {
        return markDBRepository.findOne(id);
    }
    /*
        returns an iterator corresponding to fetch
     */
    public Iterator<Mark> fetchFindAllMarks() throws SQLException
    {
        return markDBRepository.fetchFindAll();
    }
    /*
        returns an iterator corresponding to all marks in database
     */
    public Iterator<Mark> findAllMarks() throws SQLException
    {
        return  markDBRepository.findAll();
    }
    /*
        establishes the connection to database
     */
    public void ConnectToMarkDatabase(String username,String password) throws SQLException
    {
        markDBRepository.connect(username,password);
    }

    /*
        returns a List of marks from database corresponding to a given query
     */
    public List<Mark> WithGivenQuery(String query) throws SQLException
    {
        return markDBRepository.WithGivenQuery(query);
    }
    /*
        fetch forward to the nest set o data in database
     */
    public int fetchForwardMarks()
    {
        return markDBRepository.fetchForward();
    }
    /*
        fetches backward to the previous set of data in database
     */
    public int fetchBackwardMarks()
    {
        return markDBRepository.fetchBackward();
    }
    /*
        returns the number of marks in database
     */
    public int sizeMarks()
    {
        return markDBRepository.size();
    }
    /*
        returns an iterator corresponding to a filterd list by a predicate
     */
    public Iterator<Mark> filterMarks(Predicate<Mark>p)
    {
        return markDBRepository.filter(p);
    }
    /*
        returns the average of marks to a student
     */
    public Float Avg(int ids) throws SQLException
    {

        return markDBRepository.Avg(ids);
    }
    /*
         returns the boolean valuea corresponding to the numbers of marks given for a homework bigger than 0
     */

    public boolean markWithHomework(int idH) throws  SQLException
    {
        return markDBRepository.WithGivenQuery("SELECT * FROM note where nrTema="+String.valueOf(idH)).size()>0;

    }
    ////////////////////////////////
}
