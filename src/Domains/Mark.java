package Domains;


public class Mark
{
    private int idNota;
    private int idStudent;
    private int nrTema;
    private float valoare;
    private float penalizare=0;

    public Mark(int idNota, int idStudent, int nrTema, float valoare)
    {
        this.idStudent = idStudent;
        this.nrTema = nrTema;
        this.valoare = valoare;
        this.idNota=idNota;
    }

    public int getIdNota()
    {
        return idNota;
    }

    public void setIdNota(int idNota)
    {
        this.idNota = idNota;
    }

    public int getIdStudent()
    {
        return idStudent;
    }

    public void setIdStudent(int idStudent)
    {
        this.idStudent = idStudent;
    }

    public int getNrTema()
    {
        return nrTema;
    }

    public void setNrTema(int nrTema)
    {
        this.nrTema = nrTema;
    }

    public float getValoare()
    {
        return valoare;
    }

    public void setValoare(float valoare)
    {
        this.valoare = valoare;
    }

    @Override
    public String toString()
    {
        return idNota +","+ idStudent +","+ nrTema+"," + valoare;
    }

    public float getPenalizare()
    {
        return penalizare;
    }
    public void setPenalizare(float penalizare)
    {
        this.penalizare=penalizare;
    }
}
