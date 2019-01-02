package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Main extends Application {

    private static void glavniGrad() {
        GeografijaDAO baza = GeografijaDAO.getInstance();
        String drzava;
        System.out.print("Unesite ime drzave: ");
        Scanner tok = new Scanner(System.in);
        drzava = tok.nextLine();
        Grad grad = baza.glavniGrad(drzava);
        System.out.println(grad.getNaziv());


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

    @Override
    public void start(Stage primaryStage) throws Exception {
        baza=GeografijaDAO.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/prozor.fxml"));
        loader.setController(new Controller(baza));
        Parent root = loader.load();
        primaryStage.setTitle("Gradovi");
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                baza.removeInstance();
            }
        });
    }

    private static GeografijaDAO baza;
    public static void main(String[] args){
        launch(args);

    }
}
