package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.Alert.AlertType;
import services.Barang;
import services.Manager;

public class deleteController {
    
    @FXML
    private void initialize() throws Exception{
        List<Barang> list = Manager.load();
        list.removeIf(b -> !b.status);
        tampilkanBarang(list);
    }


    @FXML private Label pageTitle;

    @FXML private TextField namaBarangInput;
    @FXML private VBox objectContainer;

    @FXML
    private void showDashboard(ActionEvent event) throws Exception { SwitchHelper.switchScene("Main.fxml", event); }

    @FXML
    private void showAdd(ActionEvent event) throws Exception { SwitchHelper.switchScene("add.fxml", event); }
    
    @FXML
    private void showDelete(ActionEvent event) throws Exception { SwitchHelper.switchScene("delete.fxml", event); }
    
    @FXML
    private void showEdit(ActionEvent event) throws Exception { SwitchHelper.switchScene("edit.fxml", event); }

    public void deleteBarang() throws Exception {
        String nama = namaBarangInput.getText();
        if (nama.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Masukkan nama barang.");
            return;
        }

        Manager.delete(nama);
        showAlert(AlertType.INFORMATION, "Sukses", "Barang dihapus.");
        tampilkanBarang(Manager.load());
    }

    private void tampilkanBarang(List<Barang> daftar) {

        objectContainer.getChildren().clear();

        for (Barang barang : daftar) {

            try {
                if (barang.status) {
                        FXMLLoader loader =
                            new FXMLLoader(
                                getClass().getResource("objectElement.fxml")
                            );
                        
                        Parent item = loader.load();
                        
                        ItemBarangController controller =
                            loader.getController();
                        
                        controller.setData(barang);
                        
                        objectContainer.getChildren().add(item);
                    }
            } catch (IOException e) {
                   e.printStackTrace();
            }
        }
    }


    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
