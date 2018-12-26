package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GeografijaDAO {
    private static GeografijaDAO instance;
    private static Connection connection;
    private static int lastGrad;
    private static int lastDrzava;

    private static PreparedStatement ubaci_drzavu = null;
    private static PreparedStatement ubaci_grad = null;
    private static PreparedStatement gradByNaziv = null;
    private static PreparedStatement gradById = null;
    private static PreparedStatement drzavaByNaziv = null;
    private static PreparedStatement glavniGradstm = null;
    private static PreparedStatement deleteGradByDrzavaId = null;
    private static PreparedStatement deleteDrzavaByNaziv = null;
    private static PreparedStatement editGrad = null;

    private GeografijaDAO(){
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:baza.db");

            Statement statement = null;
            try{
                statement = connection.createStatement();
                statement.execute("select id from drzava");

            }catch (Exception e){
                try{
                    Statement statement2=null;
                    statement2 = connection.createStatement();
//                    connection.setAutoCommit(false);
                    statement2.execute("CREATE TABLE grad(id integer primary key, naziv varchar(255), broj_stanovnika integer)");
                    statement2.execute("CREATE TABLE drzava(id integer primary key, naziv varchar(255), glavni_grad integer unique references grad(id))");
                    statement2.execute("ALTER TABLE grad ADD drzava integer references drzava(id)");

                    statement2.execute("INSERT INTO drzava values (1,'Velika Britanija',1)");
                    statement2.execute("INSERT INTO drzava values (2,'Austrija',2)");
                    statement2.execute("INSERT INTO drzava values (3,'Francuska',3)");
                    statement2.execute("INSERT INTO grad values (1,'London',8825000,1)");
                    statement2.execute("INSERT INTO grad values (2,'Beč',1899055,2)");
                    statement2.execute("INSERT INTO grad values (3,'Pariz',2206488,3)");
                    statement2.execute("INSERT INTO grad values (4,'Manchester',545500,1)");
                    statement2.execute("INSERT INTO grad values (5,'Graz',280200,2)");
                    lastGrad=5;
                    lastDrzava=3;
                }catch (Exception ex){

                }
            }
            glavniGradstm = connection.prepareStatement("select g.id, g.naziv, g.broj_stanovnika, d.id, d.naziv from grad g inner join drzava d on g.id = d.glavni_grad where d.naziv=?");
            gradByNaziv = connection.prepareStatement("select id, naziv from grad where naziv = ?");
            gradById = connection.prepareStatement("select id,naziv,broj_stanovnika from grad where id =?");
            drzavaByNaziv = connection.prepareStatement("select id,naziv,glavni_grad from drzava where naziv = ?");
            deleteGradByDrzavaId = connection.prepareStatement("DELETE FROM grad WHERE drzava = ?");
            deleteDrzavaByNaziv = connection.prepareStatement("DELETE FROM drzava WHERE naziv = ?");
            ubaci_drzavu = connection.prepareStatement("INSERT INTO drzava VALUES(?,?,?)");
            ubaci_grad = connection.prepareStatement("INSERT INTO grad VALUES(?,?,?,?)");
            editGrad = connection.prepareStatement("UPDATE grad SET naziv = ?, broj_stanovnika=? WHERE id = ?");


        } catch (SQLException e) {
        }
    }

    private static void initialize(){
        instance = new GeografijaDAO();
    }

    public static GeografijaDAO getInstance() {
        if(instance==null)initialize();
        return instance;
    }

    public ArrayList<Grad> gradovi()  {
        ArrayList<Grad> gradovi = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            Grad grad = null;
            Drzava drzava = null;
            Grad glavni = null;
            ResultSet res = statement.executeQuery("select g.id, g.naziv, g.broj_stanovnika, g.drzava,d.id, d.naziv, g2.id, g2.naziv, g2.broj_stanovnika from grad g2,grad g INNER JOIN drzava d on g.drzava = d.id and g2.id = d.glavni_grad ");
            while(res.next()){
                grad = new Grad(res.getString(2),null,res.getInt(3));
                grad.setId(res.getInt(1));
                drzava = new Drzava(res.getString(6),null);
                if(res.getInt(1)==res.getInt(7)){
                    //ovo je ujedno i glavni grad te države
                    drzava.setGlavniGrad(grad);
                }else{
                    glavni = new Grad(res.getString(8),drzava,res.getInt(9));
                    glavni.setId(res.getInt(7));
                    drzava.setGlavniGrad(glavni);
                }
                grad.setDrzava(drzava);
                drzava.setId(res.getInt(5));
                gradovi.add(grad);
            }
        }catch (SQLException e){

        }
        gradovi.sort(new Comparator<Grad>() {
            @Override
            public int compare(Grad o1, Grad o2) {
                Integer a=o2.getBrojStanovnika();
                Integer b=o1.getBrojStanovnika();
                return a.compareTo(b);
            }
        });
        return gradovi;
    }

    public Grad glavniGrad(String drzava) {
        Grad grad = null;
        Drzava d = null;
        try {
            glavniGradstm.clearParameters();
            glavniGradstm.setString(1,drzava);
            ResultSet res = glavniGradstm.executeQuery();
            while(res.next()){
                grad = new Grad();
                grad.setId(res.getInt(1));
                grad.setNaziv(res.getString(2));
                grad.setBrojStanovnika(res.getInt(3));
                d = new Drzava();
                d.setNaziv(res.getString(5));
                d.setId(res.getInt(4));
                grad.setDrzava(d);
                d.setGlavniGrad(grad);
            }
        } catch (SQLException e) {
            System.out.println("Neki izuzetak myb!");
        }
        return grad;
    }

    public void obrisiDrzavu(String drzava) {
        try{
            drzavaByNaziv.clearParameters();
            drzavaByNaziv.setString(1,drzava);
            ResultSet res = drzavaByNaziv.executeQuery();
            while(res.next()){
                deleteGradByDrzavaId.clearParameters();
                deleteGradByDrzavaId.setInt(1,res.getInt(1));
                deleteGradByDrzavaId.execute();
                deleteDrzavaByNaziv.clearParameters();
                deleteDrzavaByNaziv.setString(1,drzava);
                deleteDrzavaByNaziv.execute();
            }
        }catch (SQLException e){

        }
    }

    public Drzava nadjiDrzavu(String drzava) {
        Drzava d=null;
        Grad grad = null;
        try{
            d = new Drzava();
            grad = new Grad();
            drzavaByNaziv.clearParameters();
            drzavaByNaziv.setString(1,drzava);
            ResultSet res = drzavaByNaziv.executeQuery();
            while(res.next()){
                d.setId(res.getInt(1));
                d.setNaziv(res.getString(2));
                glavniGradstm.clearParameters();
                glavniGradstm.setString(1,res.getString(2));
                ResultSet res2 = glavniGradstm.executeQuery();
                while(res2.next()){
                    grad.setId(res2.getInt(1));
                    grad.setNaziv(res2.getString(2));
                    grad.setBrojStanovnika(res2.getInt(3));
                    grad.setDrzava(d);
                }
                d.setGlavniGrad(grad);
            }
        }catch(SQLException e){

        }
        return d;
    }

    public void dodajGrad(Grad grad) {
        try {
            gradByNaziv.clearParameters();
            gradByNaziv.setString(1,grad.getNaziv());
            ResultSet res = gradByNaziv.executeQuery();
            while(res.next()){
                return;
            }
            drzavaByNaziv.clearParameters();
            drzavaByNaziv.setString(1,grad.getDrzava().getNaziv());
            ResultSet resd = drzavaByNaziv.executeQuery();
            if(!resd.next()){
                dodajDrzavu(grad.getDrzava());
            }
            if(!grad.getNaziv().equals(grad.getDrzava().getGlavniGrad().getNaziv())) {
                Drzava drzava = nadjiDrzavu(grad.getDrzava().getNaziv());
                ubaci_grad.clearParameters();
                addGrad();
                ubaci_grad.setInt(1,getLastGrad() );
                ubaci_grad.setInt(4,drzava.getId());
                ubaci_grad.setString(2,grad.getNaziv());
                ubaci_grad.setInt(3,grad.getBrojStanovnika());
                ubaci_grad.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            drzavaByNaziv.clearParameters();
            drzavaByNaziv.setString(1,drzava.getNaziv());
            ResultSet res = drzavaByNaziv.executeQuery();
            if(!res.next()){
                addDrzava();
                addGrad();
                //ako bude bilo potrebe ovjde još dodati da se provjerava da li taj grad postoji
                ubaci_grad.clearParameters();
                ubaci_grad.setInt(1,getLastGrad());
                ubaci_grad.setInt(4,getLastDrzava());
                ubaci_grad.setString(2,drzava.getGlavniGrad().getNaziv());
                ubaci_grad.setInt(3,drzava.getGlavniGrad().getBrojStanovnika());
                ubaci_grad.execute();
                ubaci_drzavu.clearParameters();
                ubaci_drzavu.setInt(1,getLastDrzava());
                ubaci_drzavu.setInt(3,getLastGrad());
                ubaci_drzavu.setString(2,drzava.getNaziv());
                ubaci_drzavu.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public void izmijeniGrad(Grad grad) {
        try {
            gradById.clearParameters();
            gradById.setInt(1,grad.getId());
            ResultSet res = gradById.executeQuery();
            while(res.next()){
                editGrad.clearParameters();
                editGrad.setString(1,grad.getNaziv());
                editGrad.setInt(3,res.getInt(1));
                editGrad.setInt(2,grad.getBrojStanovnika());
                editGrad.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeInstance() {
        if(instance!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }

    public static int getLastGrad() {
        return lastGrad;
    }

    public static void addGrad(){
        lastGrad++;
    }

    public static void addDrzava(){
        lastDrzava++;
    }

    public static int getLastDrzava() {
        return lastDrzava;
    }
}
