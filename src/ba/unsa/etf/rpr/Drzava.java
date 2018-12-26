package ba.unsa.etf.rpr;

public class Drzava {
    private int id;

    private String naziv ;
    private Grad glavniGrad;
    public Drzava() {
        id = -1;
    }

    public Drzava(String naziv, Grad glavniGrad) {
        this.id=-1;
        this.naziv = naziv;
        this.glavniGrad = glavniGrad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Grad getGlavniGrad() {
        return glavniGrad;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv=naziv;
    }

    public void setGlavniGrad(Grad grad) {
        this.glavniGrad=grad;
    }

//    @Override
//    public String toString() {
//        return naziv+ "  " + glavniGrad;
//    }
}
