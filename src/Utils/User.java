package Utils;


public  class User
{
    private static String username;
    private static String password;
    private static int week;

    public static   void set(String usr,String pass,int Week)
    {
        username=usr;
        password=pass;
        week=Week;
    }
    public static void setWeek(int Week)
    {
        week=Week;
    }

    public static String getUsername()
    {
        return username;
    }

    public static String getPassword()
    {
        return password;
    }

    public static int getWeek()
    {
        return week;
    }
}
