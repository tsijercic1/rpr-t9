package ba.unsa.etf.rpr;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public class GeografijaDAO {
    private static GeografijaDAO instance;
    private static Connection connection;
    public static void removeInstance() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        instance = null;
    }
    private static PreparedStatement ubaci_drzavu = null;
    private static PreparedStatement ubaci_grad = null;
    private static PreparedStatement dajGradove = null;
    private static PreparedStatement dajDrzave = null;
    private static PreparedStatement getGrad = null;
    private static PreparedStatement getDrzava = null;

    private GeografijaDAO(){
        connection = null;
        try {
//            connection = DriverManager.getConnection("jdbc:sqlite:baza.db");
//            System.out.println("asdasdasd");
            Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection("jdbc:oracle:thin:@ora.db.lab.ri.etf.unsa.ba:1521:ETFLAB","TS18252","DpF2A8un");
            System.out.println(connection);
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement statement=null;
            Statement statement1 = null;
            try{

                 statement1= connection.createStatement();
                System.out.println(statement1);
                statement1.execute("CREATE TABLE drzava(id INTEGER PRIMARY KEY ,naziv varchar(255) not null, glavni_grad integer )");
                System.out.println("prvo je ok");
                statement1.execute("CREATE TABLE grad(id integer primary key, naziv varchar(255), broj_stanovnika INTEGER,drzava integer) ");
//                statement1.execute("ALTER TABLE main.drzava add column glavni_grad INTEGER;");
                System.out.println("drugo je ok");
                statement1.closeOnCompletion();
            }catch (Exception e){
                System.out.println("ovdje je greska"+e.getMessage());
                //statement1.execute("CREATE TABLE grad(id int primary key, naziv varchar, broj_stanovnika INTEGER,drzava int; ");
                statement = connection.prepareStatement("delete from drzava");
                statement.execute();
                statement = connection.prepareStatement("delete from grad");
                statement.execute();
            }
            statement = connection.prepareStatement("SELECT * from drzava");
            System.out.println("proso jedan");
            ubaci_drzavu = connection.prepareStatement("INSERT INTO drzava VALUES(?,?,?)");
            System.out.println("proso dva");
            ubaci_grad = connection.prepareStatement("INSERT INTO grad VALUES (?,?,?,?)");
            System.out.println("Proso 3");


            System.out.println("Proso 4");
//            System.out.println(drzave.isClosed()+"  ");
            if(true){
                ubaci_drzavu.setInt(1,1);
                ubaci_drzavu.setString(2,"Austrija");
                ubaci_drzavu.setInt(3,5);
                System.out.println("proso 10");
                ubaci_drzavu.execute();
                System.out.println("proso 10");
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
                ubaci_grad.setInt(3,8825000);
                ubaci_grad.setString(2,"London");
                ubaci_grad.setInt(4,4);
                ubaci_grad.execute();

                ubaci_grad.setInt(1,3);
                ubaci_grad.setInt(3,545500);
                ubaci_grad.setString(2,"Manchester");
                ubaci_grad.setInt(4,4);
                ubaci_grad.execute();
                ubaci_grad.setInt(1,4);
                ubaci_grad.setInt(3,280200);
                ubaci_grad.setString(2,"Graz");
                ubaci_grad.setInt(4,1);
                ubaci_grad.execute();
                ubaci_grad.setInt(1,5);
                ubaci_grad.setInt(3,1899055);
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
        } catch (Exception e) {
//            System.out.println("ima baza, ali nece da pripremi statement");
//            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
        }

        try {
            dajGradove = connection.prepareStatement("select * from grad");
            dajDrzave = connection.prepareStatement("select * from drzava");
            getGrad = connection.prepareStatement("select * from grad where id=?");
            getDrzava = connection.prepareStatement("select * from drzava where naziv=?");
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
//                System.out.println(drId);
                Drzava drzava;
                while(tabDrzava.next()){

                    int id = tabDrzava.getInt(1);
                    if(id==drId){
                        drzava=new Drzava();
                        drzava.setNaziv(tabDrzava.getString(2));
//                        System.out.println("ID glavnog grada: "+tabDrzava.getInt(3));
                        if(tabGrad.getInt(1)==tabDrzava.getInt(3)){
                            drzava.setGlavniGrad(grad);
//                            System.out.println("doso");
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
        try {

            int id=0;
            ResultSet res = dajDrzave.executeQuery();
            Drzava drzava1 = new Drzava();
            while(res.next()){
//                System.out.println(res.getString(2));
                if(drzava.equals(res.getString(2))){
                    drzava1.setNaziv(drzava);
                    id=res.getInt(3);
                }
            }

            getGrad.setInt(1,id);
            res = getGrad.executeQuery();
            if(!res.next()){
                return null;
            }
            Grad glavniGrad=new Grad();
            do{
                glavniGrad.setNaziv(res.getString(2));
                glavniGrad.setDrzava(drzava1);
                glavniGrad.setBrojStanovnika(res.getInt(3));
                drzava1.setGlavniGrad(glavniGrad);
                res.close();
                break;
            }while(res.next());
            return glavniGrad;
        } catch (SQLException e) {
//            e.printStackTrace();
        }


        return null;
    }

    public void obrisiDrzavu(String drzava) {
        int id=0;
        PreparedStatement statement= null;
        try {
            statement = connection.prepareStatement("select id from drzava where naziv = ?");
            statement.setString(1,drzava);
            ResultSet set = statement.executeQuery();
            if(set.isClosed()){
                //nema drzave
                return ;
            }
            while(set.next()){
                id=set.getInt(1);
                set.close();
                break;
            }
            statement = connection.prepareStatement("delete from grad where drzava=?");
            statement.setInt(1,id);
            statement.execute();
            //System.out.println(id);
            statement = connection.prepareStatement("delete from drzava where id=?");
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }

    public Drzava nadjiDrzavu(String drzava) {

        try {

            getDrzava.setString(1,drzava);
            ResultSet set = getDrzava.executeQuery();
            if(set.isClosed()){
                //nema drzave
                return null;
            }
            Drzava drzava1 = new Drzava();
            int id=0;
            while(set.next()){
                drzava1.setNaziv(set.getString(2));
                id=set.getInt(3);
                set.close();
                break;
            }
            Grad glavniGrad = new Grad();
            getGrad.setInt(1,id);
            ResultSet res = getGrad.executeQuery();
            while(res.next()){
                glavniGrad.setNaziv(res.getString(2));
                glavniGrad.setBrojStanovnika(res.getInt(3));
                glavniGrad.setDrzava(drzava1);
                res.close();
                break;
            }
            return drzava1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void dodajGrad(Grad grad) {
        try {
            String nazivDrzave = grad.getDrzava().getNaziv();
            getDrzava.setString(1,nazivDrzave);
            ResultSet set = getDrzava.executeQuery();
            if(set.isClosed())return;
            int id=0;
            while(set.next()){
                id=set.getInt(1);
                set.close();
                break;
            }
        PreparedStatement stm = connection.prepareStatement("select * from grad where drzava=?");
            stm.setInt(1,id);
            ResultSet res = stm.executeQuery();
            while(res.next()){
                if(res.getString(2).equals(grad.getNaziv()))return;
            }

            stm = connection.prepareStatement("SELECT count(*)FROM grad");
            int k=0;
            res = stm.executeQuery();
            while(res.next()){
                k=res.getInt(1);
                res.close();
                break;
            }
            stm = connection.prepareStatement("INSERT INTO grad VALUES(?,?,?,?)");
            stm.setInt(1,k+3);
            stm.setInt(3,grad.getBrojStanovnika());
            stm.setInt(4,id);
            stm.setString(2,grad.getNaziv());
            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dodajDrzavu(Drzava drzava) {

        try {
            getDrzava.setString(1,drzava.getNaziv());

            ResultSet set = getDrzava.executeQuery();
//            Stream a = set.getArray(1).getArray();

            if( set.isClosed()|| !set.next() ){
                Statement stm = connection.createStatement();
                set = stm.executeQuery("select count(*) from drzava");
                ubaci_drzavu.setString(2,drzava.getNaziv());
                set.next();
                int a = set.getInt(1);
                ubaci_drzavu.setInt(1,a+2);
                set = stm.executeQuery("select count(*) from grad");
                set.next();
                int b=set.getInt(1)+3;

                ubaci_drzavu.setInt(3,b);
                System.out.println(ubaci_drzavu.execute()+" lkjhgfdsa");;
                dodajGrad(drzava.getGlavniGrad());


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void izmijeniGrad(Grad grad) {
        PreparedStatement statement = null;
        try {
            getDrzava.setString(1,grad.getDrzava().getNaziv());
            ResultSet res = getDrzava.executeQuery();
            int id=0;
            while(res.next()){
                id=res.getInt(3);
            }
            getGrad.setInt(1,id);
            ResultSet set = getGrad.executeQuery();
            statement = connection.prepareStatement("update grad set naziv=? where id =?");
            while(set.next()){
                statement.setInt(2,id);
                statement.setString(1,grad.getNaziv());
                set.close();
                break;
            }
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
