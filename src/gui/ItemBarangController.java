package gui;


import services.Barang;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ItemBarangController {

    @FXML private Label namaLabel;
    @FXML private Label stokLabel;
    @FXML private Label kategoriLabel;
    @FXML private Label idLabel;

    public void setData(Barang barang) {        

        idLabel.setText(String.valueOf(barang.getId()));
        namaLabel.setText(barang.getNama());
        kategoriLabel.setText(barang.getKategori().toString());
        stokLabel.setText(barang.getStok() + " pcs");

        if (barang.getStok() <= 5) {
            stokLabel.setStyle("-fx-background-color: #3a1a2a; -fx-text-fill: #ff79c6; -fx-font-size: 11px; -fx-padding: 3 8; -fx-background-radius: 4;");
        }
    }
}