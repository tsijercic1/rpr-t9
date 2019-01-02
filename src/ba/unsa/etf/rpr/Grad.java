package ba.unsa.etf.rpr;

public class Grad {
    private int id;

    private String naziv ;

    private Drzava drzava;
    private int brojStanovnika;
    public Grad(){
        id = -1;
    }

    public Grad(String naziv, Drzava drzava, int brojStanovnika) {
        this.id = -1;
        this.naziv = naziv;
        this.drzava = drzava;
        this.brojStanovnika = brojStanovnika;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void setNaziv(String naziv) {
        this.naziv=naziv;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika=brojStanovnika;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava=drzava;
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    @Override
    public String toString() {
        return naziv+" sa "+brojStanovnika+" stanovnika u ->"+drzava.getNaziv()+"\n";
    }
}
