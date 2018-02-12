package Repositoryes;

import Domains.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class DBStudentRepository extends DBRepository<Student>
{
    public DBStudentRepository()
    {
        super();
    }

    /*
        adds student in database
     */
    @Override
    public void add(Student student) throws SQLException
    {
        OpenConnection();
        PreparedStatement s = con.prepareStatement("INSERT  INTO studenti(nume ,grupa, email, prof) VALUES (?,?,?,?)");
        s.setString(1,student.getNume());
        s.setInt(2,student.getGrupa());
        s.setString(3,student.getEmail());
        s.setString(4,student.getProf());
        s.execute();
        ResultSet res=(s=con.prepareStatement("select LAST_INSERT_ID()")).executeQuery();
        res.next();
        student.setNrMatricol(res.getInt(1));
        CloseConnection();
        count();
    }
    /*
        deletes student from database
     */
    @Override
    public void delete(int id) throws SQLException
    {
        OpenConnection();
        PreparedStatement s = con.prepareStatement("Delete from studenti where nrMatricol=?");
        s.setInt(1,id);
        s.execute();
        CloseConnection();
        count();
    }
    /*
        finds the student from database corresponding to the given id
    */
    @Override
    public Student findOne(int id) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps= con.prepareStatement("select* from studenti where nrMatricol=?");
        ps.setInt(1,id);
        ResultSet rs=ps.executeQuery();
        if(rs.next())
        {
            Student s = new Student(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5));
            CloseConnection();
            return s;
        }
        CloseConnection();
        return null;
    }
    /*
        reuturns an itarator to a container wich contains all students in database
     */
    @Override
    public Iterator<Student> findAll() throws SQLException
    {
        OpenConnection();
        LinkedList<Student> x = new LinkedList<>();
        PreparedStatement ps =con.prepareStatement("SELECT * FROM studenti");
        ResultSet res=ps.executeQuery();
        while (res.next() )
        {
            Student s = new Student(res.getInt(1),res.getString(2),res.getInt(3),res.getString(4),res.getString(5));
            x.push(s);
        }
        CloseConnection();
        return x.iterator();
    }

    /*
        returns an iterator to current fetched data in database
     */
    @Override
    public Iterator<Student> fetchFindAll() throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("select * from studenti LIMIT ? OFFSET ?");
        ps.setInt(1,limit);
        ps.setInt(2,pos);
        ResultSet res=ps.executeQuery();
        cont.clear();
        while (res.next() )
        {
            Student s = new Student(res.getInt(1),res.getString(2),res.getInt(3),res.getString(4),res.getString(5));
            cont.push(s);
        }
        CloseConnection();
        return cont.iterator();
    }
    /*
        updates a student
     */
    @Override
    public void Update(Student s) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE studenti set nume=?,grupa=? ,email=?,prof=? where nrMatricol=?");
        ps.setString(1, s.getNume());
        ps.setInt(2, s.getGrupa());
        ps.setString(3, s.getEmail());
        ps.setString(4, s.getProf());
        ps.setInt(5, s.getNrMatricol());
        ps.execute();
        CloseConnection();
    }

    /*
        sets the local variable with number of students in database
     */
    @Override
    protected void count() throws SQLException
    {
        OpenConnection();
        PreparedStatement ps=con.prepareStatement("SELECT COUNT(nrMatricol) from studenti");
        ResultSet res = ps.executeQuery();
        res.next();
        n = res.getInt(1);
        CloseConnection();
    }

    /*
        returns an iterator of a continer with students filter using a given predicate
     */
    @Override
    public Iterator<Student> filter(Predicate<Student> p) throws SQLException
    {
        return WithGivenQuery("Select * FROM studenti").stream().filter(p).iterator();
    }
    /*

        returns a list of students from database selected using a given query string
     */
    @Override
    public List<Student> WithGivenQuery(String query) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet res = ps.executeQuery();
        List<Student> x = new LinkedList<>();
        while (res.next())
        {
            x.add(new Student(res.getInt(1),res.getString(2),res.getInt(3),res.getString(4),res.getString(5)));
        }
        CloseConnection();
        return x;
    }
}
