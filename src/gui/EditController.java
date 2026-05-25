package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.Alert.AlertType;
import services.Barang;
import services.Manager;
import services.kategoriBarang;

public class EditController {

    @FXML
    private void initialize() throws Exception {
        // inisialisasi
        enumKategoriInput.getItems().setAll(
                java.util.Arrays.stream(kategoriBarang.values())
                        .map(Enum::name)
                        .toList());
        Manager.list = Manager.load();
        tampilkanBarang();
    }

    public void tampilkanBarang() throws Exception {
        Manager.list = Manager.load();
        tampilkanBarang(Manager.list);
    }

    private void tampilkanBarang(List<Barang> daftar) {

        objectContainer.getChildren().clear();

        for (Barang barang : daftar) {

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("objectElement.fxml"));

                Parent item = loader.load();

                ItemBarangController controller = loader.getController();

                controller.setData(barang);

                objectContainer.getChildren().add(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void submitEdit() throws Exception {
        String nama = namaBarangInput.getText();
        String jumlahStr = jumlahBarangInput.getText();
        String kategoriStr = enumKategoriInput.getValue();
        String statusStr = statusBarangInput.getText();

        if (idBarangInput.getText().isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Mohon masukkan ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idBarangInput.getText());
            Barang data = Manager.binarySearchId(id);

            if (data == null) {
                showAlert(AlertType.ERROR, "Error", "Barang dengan ID tersebut tidak ditemukan.");
                return;
            }

            Manager.edit(data, nama, jumlahStr, kategoriStr, statusStr);
            showAlert(AlertType.INFORMATION, "Sukses", "Data berhasil diupdate.");
            tampilkanBarang(Manager.list);

            // Clear inputs
            idBarangInput.clear();
            namaBarangInput.clear();
            jumlahBarangInput.clear();
            statusBarangInput.clear();
            enumKategoriInput.setValue(null);
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "ID dan Jumlah harus berupa angka.");
        } catch (IllegalArgumentException e) {
            showAlert(AlertType.ERROR, "Error", "Kategori tidak valid.");
        }
    }

    @FXML
    private Label pageTitle;
    @FXML
    private TextField idBarangInput;
    @FXML
    private TextField namaBarangInput;
    @FXML
    private TextField jumlahBarangInput;
    @FXML
    private TextField statusBarangInput;
    @FXML
    private ComboBox<String> enumKategoriInput;
    @FXML
    private VBox objectContainer;

    @FXML
    private void showDashboard(ActionEvent event) throws Exception {
        SwitchHelper.switchScene("Main.fxml", event);
    }

    @FXML
    private void showAdd(ActionEvent event) throws Exception {
        SwitchHelper.switchScene("add.fxml", event);
    }

    @FXML
    private void showDelete(ActionEvent event) throws Exception {
        SwitchHelper.switchScene("delete.fxml", event);
    }

    @FXML
    private void showEdit(ActionEvent event) throws Exception {
        SwitchHelper.switchScene("edit.fxml", event);
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
