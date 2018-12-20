package ba.unsa.etf.rpr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static GeografijaDAO baza;
    public static void main(String[] args) throws SQLException {
//        System.out.println("Gradovi su:\n" + ispisiGradove());
//        glavniGrad();
//        Connection connection = DriverManager.getConnection("jdbc:sqlite:baza.db");
//        Statement statement = connection.createStatement();

//        statement.execute("CREATE TABLE drzava(id INT PRIMARY KEY ,naziv VARCHAR not null )");
//        statement.execute("CREATE TABLE grad(id int primary key, naziv varchar, broj_stanovnika int, drzava int references drzava(id));");
//        statement.execute("ALTER TABLE drzava add column glavni_grad int references drzava(id);");
//        statement.execute("DROP TABLE main.drzava;");
//        statement.execute("DROP TABLE main.grad;");
//          statement.execute("alter table drzava drop column glavni_grad");
//          statement.execute("alter table drzava add glavni_grad int references drzava(id);");
        baza = GeografijaDAO.getInstance();
        System.out.println(baza);
        System.out.println(ispisiGradove());
        Grad sarajevo = new Grad();
        sarajevo.setNaziv("Sarajevo");
        sarajevo.setBrojStanovnika(500000);
        Drzava bih = new Drzava();
        bih.setNaziv("Bosna i Hercegovina");
        bih.setGlavniGrad(sarajevo);
        sarajevo.setDrzava(bih);

        GeografijaDAO dao = GeografijaDAO.getInstance();
        dao.dodajDrzavu(bih);

        dao.dodajGrad(sarajevo);

        // Provjera
        Grad proba = dao.glavniGrad("Bosna i Hercegovina");
        System.out.println(proba);

    }

    private static void glavniGrad() {

    }

    public static String ispisiGradove() {
        baza = GeografijaDAO.getInstance();
        ArrayList<Grad> gradovi = baza.gradovi();
        StringBuilder res= new StringBuilder();
        for(Grad x : gradovi){
            res.append(x.getNaziv()).append(" (").append(x.getDrzava().getNaziv()).append(") - ").append(x.getBrojStanovnika()).append("\n");
        }
        return res.toString();

    }
}
