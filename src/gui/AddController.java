package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.util.List;

import javafx.scene.control.Alert.AlertType;


import data.kategoriBarang;
import data.Barang;
import data.Manager;

public class AddController {
    
    @FXML
    private void initialize() throws Exception{
        // inisialisasi
        enumKategoriInput.getItems().setAll(
            java.util.Arrays.stream(kategoriBarang.values())
                .map(Enum::name)
                .toList()
        );
        barangToTable();
    }

    public void barangToTable() throws Exception{
        List<Barang> list = Manager.load(Manager.filePath);
        ObservableList<Barang> data = FXCollections.observableArrayList(list);
        tableBarang.setItems(data);
        tableBarang.refresh();
    }

    @FXML
    private void submitBarang() throws Exception {
        String nama = namaBarangInput.getText();
        String jumlahStr = jumlahBarangInput.getText();
        String kategoriStr = enumKategoriInput.getValue();

        if (nama.isEmpty() || jumlahStr.isEmpty() || kategoriStr == null) {
            showAlert(AlertType.ERROR, "Error", "Mohon lengkapi semua input.");
            return;
        }

        try {
            int stokInt = Integer.parseInt(jumlahStr);
            kategoriBarang kategori = kategoriBarang.valueOf(kategoriStr);

            List<Barang> currentList = Manager.load(Manager.filePath);
            int nextId = Manager.getNextId(currentList);

            Barang barang = new Barang(nextId, nama, stokInt, kategori, true);
            Manager.add(barang);

            showAlert(AlertType.INFORMATION, "Sukses", "Data berhasil ditambahkan.");
            barangToTable(); // Refresh table
            
            // Clear inputs
            namaBarangInput.clear();
            jumlahBarangInput.clear();
            enumKategoriInput.setValue(null);
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "Jumlah harus berupa angka.");
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error", "Kategori tidak valid.");
        }
    }



    @FXML private Label pageTitle;
    @FXML private TableView<Barang> tableBarang;
    @FXML private TextField namaBarangInput;
    @FXML private TextField jumlahBarangInput;
    @FXML private ComboBox<String> enumKategoriInput;
    @FXML private TextField stockInput;

    @FXML
    private void showDashboard(ActionEvent event) throws Exception { SwitchHelper.switchScene("Main.fxml", event); }

    @FXML
    private void showAdd(ActionEvent event) throws Exception { SwitchHelper.switchScene("add.fxml", event); }
    
    @FXML
    private void showDelete(ActionEvent event) throws Exception { SwitchHelper.switchScene("Main.fxml", event); }
    
    @FXML


    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
