package Repositoryes;


import Domains.Homework;


import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;


public class DBHomeworkRepository extends DBRepository<Homework>
{

    /*
        adds homework to database
     */
    @Override
    public void add(Homework o) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO teme(enunt,termenLimita) VALUES (?,?)");
        ps.setString(1,o.getEnunt());
        ps.setInt(2,o.getTermenLimita());
        ps.execute();
        n++;
        CloseConnection();
    }
    /*
        deletes homework from database
     */

    @Override
    public void delete(int id) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps=con.prepareStatement("DELETE FROM teme where nrtema=? ");
        ps.setInt(1,id);
        ps.execute();
        n--;
        CloseConnection();
    }
    /*
        finds the homework corresponding to id in database
     */
    @Override
    public Homework findOne(int id) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps=con.prepareStatement("SELECT *from teme where nrtema=?");
        ps.setInt(1,id);
        ResultSet res=ps.executeQuery();
        if(res.next())
        {
            Homework t =new Homework(res.getInt(1), res.getString(2),res.getInt(3));
            CloseConnection();
            return t;
        }
        CloseConnection();
        return null;
    }
    /*
        returns an iterator corresponding to current fetch dat from database
     */
    @Override
    public Iterator<Homework> fetchFindAll() throws SQLException
    {
        OpenConnection();
        PreparedStatement ps=con.prepareStatement("SELECT * FROM teme LIMIT ? OFFSET ?");
        ps.setInt(1,limit);
        ps.setInt(2,pos);
        ResultSet res=ps.executeQuery();
        cont.clear();
        while (res.next())
        {
            Homework t = new Homework(res.getInt(1),res.getString(2),res.getInt(3));
            cont.push(t);
        }
        CloseConnection();
        return cont.iterator();
    }
    /*
        returns an iterator for all homework in database
     */
    @Override
    public Iterator<Homework> findAll() throws SQLException
    {
        OpenConnection();
        PreparedStatement ps =con.prepareStatement("SELECT * FROM teme");
        ResultSet res=ps.executeQuery();
        LinkedList<Homework> x= new LinkedList<>();
        while (res.next())
        {
            Homework t = new Homework(res.getInt(1),res.getString(2),res.getInt(3));
            x.push(t);
        }
        CloseConnection();
        return x.iterator();

    }
    /*
        updates the homework corresponding to an id
     */
    @Override
    public void Update(Homework homework) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps= con.prepareStatement("UPDATE teme SET enunt=?,termenLimita=? where nrtema=?");
        ps.setString(1, homework.getEnunt());
        ps.setInt(2, homework.getTermenLimita());
        ps.setInt(3, homework.getNrTema());
        ps.execute();
        CloseConnection();
    }
    /*
        sets the numbers of homrwork in database
     */
    @Override
    protected void count() throws SQLException
    {
        OpenConnection();
        PreparedStatement ps =con.prepareStatement("SELECT COUNT(nrtema) FROM teme");
        ResultSet res=ps.executeQuery();
        res.next();
        n= res.getInt(1);
        CloseConnection();
    }
    /*
        returns a an iterator with homework filterf using a predicate
     */
    @Override
    public Iterator<Homework> filter(Predicate<Homework> p) throws SQLException
    {
        return  WithGivenQuery("SELECT * FROM teme").stream().filter(p).iterator();
    }
    /*
        returns a list of homework from datbase applying a given query
     */
    @Override
    public List<Homework> WithGivenQuery(String query) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps =con.prepareStatement(query);
        ResultSet res=ps.executeQuery();
        LinkedList<Homework > x = new LinkedList<>();
        while (res.next())
        {
            x.add(new Homework(res.getInt(1),res.getString(2),res.getInt(3)));

        }
        CloseConnection();
        return x;
    }
    /*
        returns the homework for witch average of its marks is minimum
     */

    public Homework hardestHomework() throws  SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("SELECT MIN(a.med),nrTema FROM( SELECT nrTema, Avg( valoare) as med FROM note GROUP BY nrTema) a");
        ResultSet res=ps.executeQuery();
        Homework h= null;
        if(res.next())
        {
            h=findOne(res.getInt(2));
        }
        CloseConnection();
        return h;
    }
}
