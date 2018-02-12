package Repositoryes;

import Domains.Mark;

import java.sql.*;

import java.util.*;
import java.util.function.Predicate;

public class DBMarkRepository extends DBRepository<Mark>
{
    /*
        adds mark in database
     */
    @Override
    public void add(Mark mark) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps= con.prepareStatement("INSERT INTO note(idStudent,nrTema,valoare,penalizare) VALUES(?,?,?,?)");
        ps.setInt(1, mark.getIdStudent());
        ps.setInt(2, mark.getNrTema());
        ps.setFloat(3, mark.getValoare());
        ps.setFloat(4, mark.getPenalizare());
        ps.execute();
        n++;
        CloseConnection();
    }
    /*
         deletes mark from database
     */
    @Override
    public void delete(int id) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM note where idNota=?");
        ps.setInt(1,id);
        ps.execute();
        n--;
        CloseConnection();
    }

    /*
        finds one markcorresponding to given id
     */
    @Override
    public Mark findOne(int id) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("SELECT* FROM  note where idNota=?");
        ps.setInt(1,id);
        ResultSet s =ps.executeQuery();
        Mark n = null;
        if(s.next())
        {
             n = new Mark(s.getInt(1), s.getInt(2), s.getInt(3), s.getFloat(4));
        }
        CloseConnection();
        return n;
    }
    /*
        returns an iterator for all marks in database
     */
    @Override
    public Iterator<Mark> findAll() throws SQLException
    {
        OpenConnection();
        LinkedList<Mark>  x= new LinkedList<>();
        PreparedStatement ps=con.prepareStatement("SELECT * FROM note");
        ResultSet s = ps.executeQuery();
        while (s.next())
        {
            Mark n = new Mark(s.getInt(1),s.getInt(2),s.getInt(3),s.getFloat(4));
            x.add(n);
        }
        CloseConnection();
        return x.iterator();
    }
    /*
        returns an iterator corresponding to current fetched data in database
     */
    @Override
    public Iterator<Mark> fetchFindAll() throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM note Limit ? Offset ?");
        ps.setInt(1,limit);
        ps.setInt(2,pos);
        ResultSet s=ps.executeQuery();
        cont.clear();
        while (s.next())
        {

            Mark n = new Mark(s.getInt(1),s.getInt(2),s.getInt(3),s.getFloat(4));
            cont.push(n);
        }
        CloseConnection();
        return cont.iterator();
    }

    /*
        updates a mark
     */
    @Override
    public void Update(Mark mark) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE note SET  valoare=? where idNota=?");
        //ps.setInt(1,mark.getNrTema());
        ps.setFloat(1, mark.getValoare());
        ps.setInt(2, mark.getIdNota());
        ps.execute();
        CloseConnection();
    }

    /*
        sets the number of marks in database
     */
    @Override
    protected void count() throws SQLException
    {
        OpenConnection();
        PreparedStatement p = con.prepareStatement("SELECT COUNT(idNota) FROM note");
        ResultSet res=p.executeQuery();
        res.next();
        n=res.getInt(1);
        CloseConnection();
    }
    /*
        returns the numbers of marks corresponding to a homework
     */
    public int MarkWithHomework(int nrTema) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps =con.prepareStatement("SELECT COUNT(nrTema) FROM note where nrTema=?");
        ps.setInt(1,nrTema);
        ResultSet rs= ps.executeQuery();
        rs.next();
        int l=rs.getInt(1);

        CloseConnection();
        return l;

    }
    /*
        returns an iterator to a filterd list applying a predicate
     */
    @Override
    public Iterator<Mark> filter(Predicate<Mark> p)
    {
        return  cont.stream().filter(p).iterator();
    }

    /*
        returns the number of marks in database
     */
    public int size()
    {
        return n;
    }

    /*
        finds if there is a mark given to a student for a homework
     */
    public boolean findMarkForStudentAndHomework(int ids,int nrh)throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("SELECT Count(idStudent) FROM note where idStudent=? and nrTema=?");
        ps.setInt(1,ids);
        ps.setInt(2,nrh);
        ResultSet res = ps.executeQuery();
        if(res.next())
        {
            return res.getInt(1)>0;
        }
        CloseConnection();
        return false;
    }
    /*
        computes the average for a given homework
     */
    public float AvgPerHomework(int nrh) throws SQLException
    {
        OpenConnection();

        PreparedStatement ps = con.prepareStatement("SELECT AVG(valoare) FROM note where nrTema=?");

        ps.setInt(1,nrh);

        ResultSet res=ps.executeQuery();

        res.next();
        float med=res.getFloat(1);

        CloseConnection();
        return  med;
    }
    /*
        returns a list of marks corresponding to a given query
     */
    @Override
    public List<Mark>WithGivenQuery(String query)throws SQLException
    {
        ArrayList<Mark> x = new ArrayList<>();
        OpenConnection();
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet res= ps.executeQuery();
        while (res.next())
        {
            x.add( new Mark(res.getInt(1), res.getInt(2),res.getInt(3), res.getFloat(4)));
        }
        CloseConnection();
        return x;
    }
    /*
        computes the average for a student
     */
    public Float Avg(int ids) throws SQLException
    {
        OpenConnection();
        PreparedStatement ps = con.prepareStatement("SELECT AVG(valoare) FROM note where idStudent=?");
        ps.setInt(1,ids);
        ResultSet resultSet=ps.executeQuery();
        Float f =null;
        if(resultSet.next())
            f=resultSet.getFloat(1);
        CloseConnection();
        return  f;
    }
}
