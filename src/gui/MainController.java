package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.util.List;


import data.Barang;
import data.Manager;

public class MainController {
    
    @FXML
    private void initialize() throws Exception{
        // inisialisasi
        barangToTable();
    }

    public void barangToTable() throws Exception{
        List<Barang> list = Manager.load(Manager.filePath);
        ObservableList<Barang> data = FXCollections.observableArrayList(list);
        tableBarang.setItems(data);
        tableBarang.refresh();
    }


    @FXML private Label pageTitle;
    @FXML private TableView<Barang> tableBarang;


    @FXML
    private void showDashboard(ActionEvent event) throws Exception { SwitchHelper.switchScene("Main.fxml", event); }

    @FXML
    private void showAdd(ActionEvent event) throws Exception { SwitchHelper.switchScene("add.fxml", event); }
    
    @FXML
    private void showDelete(ActionEvent event) throws Exception { SwitchHelper.switchScene("Main.fxml", event); }
    

}
