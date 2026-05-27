package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import services.Barang;
import services.Manager;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;

public class MainController {
    
    @FXML
    private void initialize() throws Exception{
        // inisialisasi

        idSort.getItems().addAll("ascending");
        idSort.setOnAction(e -> {
            namaSort.setValue(null);
            kategoriSort.setValue(null);
            stokSort.setValue(null);
            Manager.SortByIdASC();
             tampilkanBarang(Manager.list);
            
        });

        namaSort.getItems().addAll("A-Z");
        namaSort.setOnAction(e -> {
            idSort.setValue(null);
            kategoriSort.setValue(null);
            stokSort.setValue(null);
            Manager.selectionSortByNama();
            tampilkanBarang(Manager.list);
        });

        for (services.kategoriBarang k : services.kategoriBarang.values()) {
            kategoriSort.getItems().add(k.name());
        }
        kategoriSort.getItems();
        kategoriSort.setOnAction(e -> {
            String val = kategoriSort.getValue();
            if (val == null) return;
            if ("Reset".equals(val)) {
                resetData();
            } else {
                try {
                    List<Barang> hasil = Manager.searchByKategori(val);
                    if (hasil.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Info");
                        alert.setHeaderText(null);
                        alert.setContentText("Tidak ditemukan");
                        alert.showAndWait();
                    } else {
                        tampilkanBarang(hasil);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        stokSort.getItems().addAll("descending");
        stokSort.setOnAction(e -> {
            namaSort.setValue(null);
            kategoriSort.setValue(null);
            idSort.setValue(null);
            Manager.insertionSortByStok();
            tampilkanBarang(Manager.list);
            
        });

        Manager.list = Manager.load();
        statistikBarang();
        tampilkanBarang(Manager.list);
    }

    private void resetData() {
        try {
            Manager.list = Manager.load();
            tampilkanBarang(Manager.list);
            idSort.setValue(null);
            namaSort.setValue(null);
            kategoriSort.setValue(null);
            stokSort.setValue(null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void statistikBarang() throws Exception{
        int barangTersedia = 0 ;
        int barangHabis = 0;
        int stockTotal = 0;
        List<Barang> list = Manager.load();

        for (Barang b : list) {
            if (b.status) {
                barangTersedia++;
                stockTotal += b.stok;

            }else{
                barangHabis++;
            }
        }        
        lblTersedia.setText(String.valueOf(barangTersedia));
        lblHabis.setText(String.valueOf(barangHabis));
        lblTotal.setText(String.valueOf(stockTotal));
    }

    private void tampilkanBarang(List<Barang> daftar) {

        objectContainer.getChildren().clear();

        for (Barang barang : daftar) {

            try {

                FXMLLoader loader =
                    new FXMLLoader(
                        getClass().getResource("objectElement.fxml")
                    );

                Parent item = loader.load();

                ItemBarangController controller =
                    loader.getController();

                controller.setData(barang);

                objectContainer.getChildren().add(item);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML private Label pageTitle;
    @FXML private VBox objectContainer;
    @FXML private Label lblTersedia;
    @FXML private Label lblHabis;
    @FXML private Label lblTotal;
    @FXML private Button exportPdfBtn;

    @FXML private ComboBox<String> idSort;
    @FXML private ComboBox<String> namaSort;
    @FXML private ComboBox<String> kategoriSort;
    @FXML private ComboBox<String> stokSort;

    

    @FXML
    private void showDashboard(ActionEvent event) throws Exception { SwitchHelper.switchScene("Main.fxml", event); }

    @FXML
    private void showAdd(ActionEvent event) throws Exception { SwitchHelper.switchScene("add.fxml", event); }
    
    @FXML
    private void showDelete(ActionEvent event) throws Exception { SwitchHelper.switchScene("delete.fxml", event); }
    
    @FXML
    private void showEdit(ActionEvent event) throws Exception { SwitchHelper.switchScene("edit.fxml", event); }

    @FXML
    private void exportPdf() {
        try {
            List<Barang> list = Manager.load();
            services.exportToPdf pdf = new services.exportToPdf();
            pdf.exportBarang(list);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export PDF");
            alert.setHeaderText(null);
            alert.setContentText("PDF berhasil dibuat: laporan_barang.pdf");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Gagal export PDF: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

}
