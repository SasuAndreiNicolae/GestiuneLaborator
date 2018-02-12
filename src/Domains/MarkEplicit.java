package Domains;

public class MarkEplicit extends Mark
{
    private String name;
    public MarkEplicit(int idNota, int idStudent, int nrTema, float valoare, String name)
    {
        super(idNota, idStudent, nrTema, valoare);
        this.name=name;

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
