package Domains;



public class Homework
{
    private int nrTema;
    private String enunt;
    private int termenLimita;


    public Homework()
    {
    }

    @Override
    public String toString()
    {
        return  nrTema+","+  enunt + ',' + termenLimita;
    }

    public String toStringAfisare()
    {
        return "Homework{" +
                "nrTema=" + nrTema +
                ", enunt='" + enunt + '\'' +
                ", termenLimita=" + termenLimita +
                '}';
    }

    public Homework(int nrTema, String enunt, int termenLimita)
    {
        this.nrTema = nrTema;
        this.enunt = enunt;
        this.termenLimita = termenLimita;
    }

    public void setNrTema(int nrTema) {
        this.nrTema = nrTema;
    }

    public void setEnunt(String enunt) {
        this.enunt = enunt;
    }

    public void setTermenLimita(int termenLimita) {
        this.termenLimita = termenLimita;
    }

    public int getNrTema() {
        return nrTema;
    }

    public String getEnunt() {
        return enunt;
    }

    public int getTermenLimita() {
        return termenLimita;
    }

}
