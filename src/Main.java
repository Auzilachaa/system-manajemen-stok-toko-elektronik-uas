

import java.util.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import services.Barang;
import services.Manager;
import services.kategoriBarang;
import services.exportToPdf;

public class Main extends Application {
    public static Scanner input = new Scanner(System.in);

    @Override
    public void start(Stage stage) throws Exception {
        // exeption jafafx handler. biar apl ny jalan tanpa error karna bug bawaan jafafx
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> { //ngambil thread dan exeption
            if (e instanceof IndexOutOfBoundsException // cek exception
                && e.getStackTrace().length > 0
                && (e.getStackTrace()[0].getClassName().contains("ReadOnlyUnbackedObservableList")
                    || e.getStackTrace()[0].getClassName().contains("SelectedItemsReadOnlyObservableList"))) {
                // bug JavaFX 26, diabaikan
                return; 
            }
            // print exeption lain
            e.printStackTrace();
        });

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/gui/Main.fxml")
        );
        BorderPane root = loader.load();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Inventaris");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        // Load data sekali di awal
        Manager.load();
        int pilihan = 0;

        launch(args);

        while (pilihan != 6) {
            System.out.println("\n=== Sistem Manajemen Stok Toko Elektronik ===");
            System.out.println("1. Tampilkan Barang");
            System.out.println("2. Tambah Barang");
            System.out.println("3. Hapus Barang");
            System.out.println("4. Update Barang");
            System.out.println("5. Cari Barang");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu (1-6): ");

            pilihan = input.nextInt();
            input.nextLine();

            switch (pilihan) {
                case 1:
                    tampilkanBarangMenu();
                    break;

                case 2:
                    addMenu();
                    break;

                case 3:
                    System.out.print("Masukkan nama barang yang akan dihapus: ");
                    String key = input.nextLine();
                    Manager.delete(key);
                    break;

                case 4:
                    System.out.print("Masukkan ID barang yang akan diupdate: ");
                    // int key2 = input.nextInt();
                    input.nextLine();
                    // Manager.edit(key2, input);
                    break;

                case 5:
                    menuCari();
                    break;

                case 6:
                    exportToPdf pdf = new exportToPdf();
                    pdf.exportBarang(Manager.list);
                    break;
                case 7:
                    System.out.println("Program selesai.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }
        }
    }

    static void addMenu() throws Exception {
        System.out.print("Masukkan nama barang: ");
        String nama = input.nextLine();

        int stok = Manager.cekIputInt("Masukkan stok barang", input);

        System.out.println("Masukkan kategori barang:");
        kategoriBarang.showKategori();

        System.out.print("Pilihan kategori: ");
        int pil = Integer.parseInt(input.nextLine());

        kategoriBarang kategori = kategoriBarang.dariNomor(pil);
        if (kategori == null) {
            System.out.println("[!] Kategori tidak valid, diubah menjadi lainnya");
            kategori = kategoriBarang.lainnya;
        }

        System.out.print("Apakah barang tersedia? (true/false): ");
        boolean status = Boolean.parseBoolean(input.nextLine().trim());

        Manager.list.add(new Barang(Manager.getNextId(Manager.list), nama, stok, kategori, status));

        Manager.save(Manager.list);
        Manager.cetakTersedia(Manager.list);
    }

    static void menuCari() throws Exception {
        System.out.println("\n1. Cari Nama");
        System.out.println("2. Cari ID");
        System.out.print("Pilih: ");

        int pilih = input.nextInt();
        input.nextLine();

        if (pilih == 1) {
            System.out.print("Masukkan nama barang: ");
            String keyword = input.nextLine();
            Barang data =  Manager.linearSearchNama(keyword);

            if (data == null) {
                System.out.println("Tidak ditemukan");
            } else {
                Manager.cetak(data);
            }
            
        } else if (pilih == 2) {
            cariId();
        }

    }


    static void cariId() throws Exception {
        System.out.print("Masukkan ID: ");
        int id = input.nextInt();
        input.nextLine();

        Barang b = Manager.binarySearchId(id);

        if (b == null) {
            System.out.println("Tidak ditemukan");
        } else {
            Manager.cetak(b);
        }
    }

    static void tampilKategoriTertentu(){
        System.out.println("Masukkan kategori barang:");
        kategoriBarang.showKategori();
        System.out.print("Pilihan kategori: ");
        int pil = Integer.parseInt(input.nextLine());
        kategoriBarang kategori = kategoriBarang.dariNomor(pil);
        if (kategori == null) {
            System.out.println("[!] Kategori tidak valid, diubah menjadi lainnya");
            kategori = kategoriBarang.lainnya;
        }
        for (Barang b : Manager.list) {
            if (b.kategori == kategori) {
                Manager.cetak(b);
            }
        }
    }

    static void tampilkanBarangMenu() throws Exception{
        System.out.println("\n=== Tampilkan Barang ===");
        System.out.println("1. Tampilkan Barang tersedia (ID ascending)");
        System.out.println("2. Tampilkan Barang tersedia (ID descending)");
        System.out.println("3. Tampilkan Barang Semua Barang (ID ascending)");
        System.out.println("4. Tampilkan Barang Semua Barang (ID descending)");
        System.out.println("5. Kembali ke Menu Utama");
        System.out.print("Pilih: ");

        int pilihan = input.nextInt();
        input.nextLine();

        switch (pilihan) {
            case 1:
                Manager.SortByIdASC();
                Manager.cetakTersedia(Manager.list);
                break;
            case 2:
                Manager.SortByIdASC();
                Manager.cetakSemua(Manager.list);
                break;
            case 3:
                tampilKategoriTertentu();
                break;
            case 4:
                break;
            default:
                System.out.println("Pilihan tidak valid.");
                break;
        }
    }

    

}
