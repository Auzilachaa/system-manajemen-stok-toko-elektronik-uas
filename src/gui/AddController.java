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

public class AddController {

    @FXML
    private void initialize() throws Exception {
        // inisialisasi
        enumKategoriInput.getItems().setAll(
                java.util.Arrays.stream(kategoriBarang.values())
                        .map(Enum::name)
                        .toList());
        Manager.load();
        tampilkanBarang();
    }

    public void tampilkanBarang() throws Exception {
        Manager.load();
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

            int nextId = Manager.getNextId(Manager.list);

            Barang barang = new Barang(nextId, nama, stokInt, kategori, true);
            Manager.add(barang);

            showAlert(AlertType.INFORMATION, "Sukses", "Data berhasil ditambahkan.");
            tampilkanBarang(Manager.list);

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

    @FXML
    private Label pageTitle;
    @FXML
    private TableView<Barang> tableBarang;
    @FXML
    private TextField namaBarangInput;
    @FXML
    private TextField jumlahBarangInput;
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
