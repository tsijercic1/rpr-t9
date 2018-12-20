package ba.unsa.etf.rpr;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {
    private static GeografijaDAO instance;
    private static Connection connection;
    public static void removeInstance() {
        instance = null;
    }
    private static PreparedStatement ubaci_drzavu = null;
    private static PreparedStatement ubaci_grad = null;
    private static PreparedStatement dajGradove = null;
    private static PreparedStatement dajDrzave = null;

    private GeografijaDAO(){
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            PreparedStatement statement=null;
            Statement statement1 = null;
            try{

                 statement1= connection.createStatement();
                statement1.execute("CREATE TABLE drzava(id INTEGER PRIMARY KEY ,naziv text not null, glavni_grad int );");
                statement1.execute("CREATE TABLE grad(id int primary key, naziv varchar, broj_stanovnika INTEGER,drzava int); ");
//                statement1.execute("ALTER TABLE main.drzava add column glavni_grad INTEGER;");
                statement1.closeOnCompletion();
            }catch (Exception e){
//                System.out.println("ovdje je greska"+e.getMessage());
                //statement1.execute("CREATE TABLE grad(id int primary key, naziv varchar, broj_stanovnika INTEGER,drzava int; ");
            }
            statement = connection.prepareStatement("SELECT * from main.drzava;");
            ubaci_drzavu = connection.prepareStatement("INSERT INTO main.drzava VALUES(?,?,?);");
            ubaci_grad = connection.prepareStatement("INSERT INTO main.grad VALUES (?,?,?,?);");

            ResultSet drzave = statement.executeQuery();
            System.out.println(drzave.isClosed()+"  ");
            if(drzave.isClosed()){
                ubaci_drzavu.setInt(1,1);
                ubaci_drzavu.setString(2,"Austrija");
                ubaci_drzavu.setInt(3,5);
                ubaci_drzavu.execute();
//                ubaci_drzavu.setInt(1,2);
//                ubaci_drzavu.setString(2,"Bosna i Hercegovina");
//                ubaci_drzavu.execute();
                ubaci_drzavu.setInt(1,3);
                ubaci_drzavu.setString(2,"Francuska");
                ubaci_drzavu.setInt(3,1);
                ubaci_drzavu.execute();
                ubaci_drzavu.setInt(1,4);
                ubaci_drzavu.setString(2,"Velika Britanija");
                ubaci_drzavu.setInt(3,2);
                ubaci_drzavu.execute();
//                statement.execute("DELETE FROM main.grad;");
                ubaci_grad.setInt(1,1);
                ubaci_grad.setInt(3,2206488);
                ubaci_grad.setString(2,"Pariz");
                ubaci_grad.setInt(4,3);
                ubaci_grad.execute();
                ubaci_grad.setInt(1,2);
                ubaci_grad.setInt(3,8136000);
                ubaci_grad.setString(2,"London");
                ubaci_grad.setInt(4,4);
                ubaci_grad.execute();
                ubaci_grad.setInt(1,3);
                ubaci_grad.setInt(3,510746);
                ubaci_grad.setString(2,"Manchester");
                ubaci_grad.setInt(4,4);
                ubaci_grad.execute();
                ubaci_grad.setInt(1,4);
                ubaci_grad.setInt(3,283869);
                ubaci_grad.setString(2,"Graz");
                ubaci_grad.setInt(4,1);
                ubaci_grad.execute();
                ubaci_grad.setInt(1,5);
                ubaci_grad.setInt(3,1868000);
                ubaci_grad.setString(2,"Beč");
                ubaci_grad.setInt(4,1);
                ubaci_grad.execute();
                ubaci_grad.close();
                PreparedStatement x = connection.prepareStatement("update table drzava set glavni_grad = 1 where id = 3");
                x.execute();
                x = connection.prepareStatement("update table drzava set glavni_grad = 2 where id = 4");
                x.execute();
                x = connection.prepareStatement("update table drzava set glavni_grad = 5 where id = 1");

            }
        } catch (SQLException e) {
//            System.out.println("ima baza, ali nece da pripremi statement");
//            System.out.println(e.getMessage());
        }

        try {
            dajGradove = connection.prepareStatement("select * from grad;");
            dajDrzave = connection.prepareStatement("select * from drzava;");
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
        try {
            ResultSet tabGrad = dajGradove.executeQuery();
            ResultSet tabDrzava ;
            while(tabGrad.next()) {
                tabDrzava = dajDrzave.executeQuery();
                Grad grad = new Grad(tabGrad.getString(2), null, tabGrad.getInt(3));
                int drId = tabGrad.getInt(4);
                System.out.println(drId);
                Drzava drzava;
                while(tabDrzava.next()){

                    int id = tabDrzava.getInt(1);
                    if(id==drId){
                        drzava=new Drzava();
                        drzava.setNaziv(tabDrzava.getString(2));
//                        System.out.println("ID glavnog grada: "+tabDrzava.getInt(3));
                        if(tabGrad.getInt(1)==tabDrzava.getInt(3)){
                            drzava.setGlavniGrad(grad);
                            System.out.println("doso");
                            tabDrzava.close();
                        }
                        grad.setDrzava(drzava);
                        break;
                    }

                }
                gradovi.add(grad);
            }
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }

        return gradovi;
    }

    public Grad glavniGrad(String drzava) {





        return null;
    }

    public void obrisiDrzavu(String drzava) {
    }

    public Drzava nadjiDrzavu(String drzava) {
        return null;
    }

    public void dodajGrad(Grad grad) {

    }

    public void dodajDrzavu(Drzava drzava) {

    }

    public void izmijeniGrad(Grad grad) {

    }
}
