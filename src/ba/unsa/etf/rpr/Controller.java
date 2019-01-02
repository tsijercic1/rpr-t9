package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ListView list;
    public Button ispisiBtn;
    private GeografijaDAO baza;
    ObservableList<Grad> lista;

    public Controller(GeografijaDAO baza) {
        this.baza = baza;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //baza = GeografijaDAO.getInstance();
        //refreshList();
    }

    public void refreshList(){
        lista = FXCollections.observableArrayList(baza.gradovi());
        list.itemsProperty().setValue(lista);
    }

    public void printCity(ActionEvent actionEvent){
        refreshList();
    }
}
