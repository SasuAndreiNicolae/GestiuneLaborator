package Repositoryes;

import java.sql.*;

import java.util.Iterator;

import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;

abstract public class DBRepository<E>
{
    protected Stack<E> cont;
    Connection con;
    protected int pos=-20;//start position first neded a fetchForward
    protected int forward=20;//nr of elements to forward
    protected int limit =20;// maximum number of  elements fetched at once
    protected int backward=20;//nr o elements to backward
    private String usrname;// username for db
    private String pasword;//password for db
    int n=0;// numbers of elements in database
    /*
        etablishes connection to database
     */
    protected void OpenConnection() throws SQLException
    {
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/map", usrname, pasword);
    }
    /*
        close connection to database
     */
    protected void CloseConnection() throws SQLException
    {
        con.close();
    }
    /*
        fetching forward the next set of data
     */
    public int fetchForward()
    {
        if(pos+forward<n)//verific sa nu depasesc nr de inregistrari in bd
        {
            pos=pos+forward;//pos=20 apoi 40 ....60....1234540
            return 1;//daca returnez 1 inseamna ca am putut face fetch
        }
        else
            return 0;
        //System.out.println(pos);

    }
    /*
        fetching backward the previous set o data
     */
    public int fetchBackward()//acelasi lucru doar ca scad
    {
        if(pos>-20)
        {
            pos-=backward;
            return 1;
           // cont.clear();
        }
        else return 0;
        //System.out.println(pos);
    }

    /*
        sets the username and password for database  and etablishes the first connection
     */
    public void connect(String usrname,String password) throws SQLException
    {
        this.usrname=usrname;
        this.pasword=password;
        OpenConnection();
        CloseConnection();
        count();

    }
    public DBRepository()
    {
        cont=new Stack<>();
    }

    /*
        adds element in database
     */
    abstract public void add(E e) throws SQLException;
    /*
        deletes element in database
     */
    abstract public void delete(int id)throws SQLException;
    /*
        finds one element in database using a given id
     */
    abstract public E findOne(int id) throws SQLException;
    /*
        returns an iterator for current fetched  data in database
     */
    abstract public Iterator<E> fetchFindAll()throws SQLException;
    /*
        returns an iterator for all elements currently in database
     */
    abstract public Iterator<E> findAll()throws SQLException;
    /*
        updates an element in database
     */
    abstract public void Update(E e) throws SQLException;
    /*
        sets the number of elements in database
     */
    abstract protected void count()throws SQLException ;
    /*
        returns the number of elements in database
     */
    public  int size()
    {
        return n;
    }

    /*
        returns an iterator to a filtered list using a given predicate
     */
    abstract public Iterator<E> filter(Predicate<E> p) throws SQLException;
    /*
        returns a list of elements corresponding to a given query string
     */

    abstract public List<E>WithGivenQuery(String query)throws SQLException;
}
