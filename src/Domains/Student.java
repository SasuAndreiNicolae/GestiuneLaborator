package Domains;

public class Student
{
    private int nrMatricol;
    private String nume;
    private int grupa;
    private String email;
    private String prof;
    public Student(){}

    public Student(int nrMatricol, String nume, int grupa, String email, String prof) {
        this.nrMatricol = nrMatricol;
        this.nume = nume;
        this.grupa = grupa;
        this.email = email;
        this.prof = prof;
    }

    public void setNrMatricol(int nrMatricol) {
        this.nrMatricol = nrMatricol;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setGrupa(int grupa) {
        this.grupa = grupa;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public int getNrMatricol() {
        return nrMatricol;
    }

    public String getNume() {
        return nume;
    }

    public int getGrupa() {
        return grupa;
    }

    public String getEmail() {
        return email;
    }

    public String getProf() {
        return prof;
    }

    public String toStringAfisare()
    {
        return "Student{" +
                "nrMatricol=" + nrMatricol +
                ", nume='" + nume + '\'' +
                ", grupa=" + grupa +
                ", email='" + email + '\'' +
                ", prof='" + prof + '\'' +
                '}';
    }
    //@Override
    public String toString() {
        return nrMatricol+","+nume+ ","+ grupa + "," + email +"," + prof +"\n";
    }

}
