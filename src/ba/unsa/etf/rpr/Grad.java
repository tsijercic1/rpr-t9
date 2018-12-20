package ba.unsa.etf.rpr;

public class Grad {
    private String naziv ;
    private Drzava drzava;
    private int brojStanovnika;

    public Grad(){}
    public Grad(String naziv, Drzava drzava, int brojStanovnika) {
        this.naziv = naziv;
        this.drzava = drzava;
        this.brojStanovnika = brojStanovnika;
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

//    @Override
//    public String toString() {
//        return naziv+" sa "+brojStanovnika+" stanovnika u ->"+drzava+"\n";
//    }
}
