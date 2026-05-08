package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static Path path = Paths.get(System.getProperty("user.dir"), "src/resources/data.json");
    public static String filePath = path.toString();

    public static void save(List<Barang> barang) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(barang, writer);
        }

    }

    public static List<Barang> load(String filename) throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Barang>>() {
            }.getType();
            List<Barang> data = gson.fromJson(reader, type);
            return data != null ? data : new ArrayList<>();
        }
    }

    public static void add(Barang barang) throws Exception {
        List<Barang> list = load(filePath);

        for (Barang b : list) {
            if (b.nama.equals(barang.nama) || b.kategori.equals(barang.kategori)) {
                System.out.println("Barang sudah ada");
                return;
            }
        }
        list.add(barang);
        save(list);
    }

    public static void delete(String key) throws Exception {
        List<Barang> list = load(filePath);


        // bakal diganti
        boolean ditemukan = false;
        for (Barang b : list) {
            if (b.nama.equalsIgnoreCase(key)) {
                b.status = false;
                ditemukan = true;
                System.out.println("Status barang '" + key + "' berhasil diubah menjadi tidak aktif (terhapus).");
                break;
            }
        }

        if (!ditemukan) {
            System.out.println("Barang tidak ditemukan.");
        }

        save(list);
    }

    public static void edit(int key2, Scanner input) throws Exception {
        List<Barang> list = load(filePath);
        Barang b = cariById(key2);

        if (b == null || !b.status) {
            System.out.println("[!] Data tidak ditemukan.");
            return;
        }
        System.out.println("  EDIT DATA  (Enter = tidak diubah)");
        cetak(list);
        System.out.println();

        // --- Nama (String) ---
        System.out.print("Nama [" + b.nama + "]: ");
        String inputNama = input.nextLine().trim();
        if (!inputNama.isEmpty()) {
            b.nama = inputNama;
        }

        // --- Kode (int) ---
        System.out.print("Kode [" + b.id + "]: ");
        String inputKode = input.nextLine().trim();
        if (!inputKode.isEmpty()) {
            try {
                b.id = Integer.parseInt(inputKode);
            } catch (NumberFormatException e) {
                System.out.println("[!] Bukan angka, kode tidak diubah.");
            }  
        }

        // --- Kategori (enum) ---
        System.out.println("Kategori [" + b.kategori + "] (Enter = skip):");
        kategoriBarang.showKategori();
        System.out.print("Pilihan: ");
        String inputKat = input.nextLine().trim();
        if (!inputKat.isEmpty()) {
            try {
                kategoriBarang katBaru = kategoriBarang.dariNomor(Integer.parseInt(inputKat));
                if (katBaru != null) {
                    b.kategori = katBaru; 
                }else {
                    System.out.println("[!] Pilihan tidak valid, kategori tidak diubah.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Kategori tidak diubah.");
            }
        }

        // --- Jumlah (int) ---
        System.out.print("Jumlah [" + b.stok + "]: ");
        String inputJumlah = input.nextLine().trim();
        if (!inputJumlah.isEmpty()) {
            try {
                b.stok = Integer.parseInt(inputJumlah);
            } catch (NumberFormatException e) {
                System.out.println("[!] Bukan angka, jumlah tidak diubah.");
            }
        }

        save(list);
        System.out.println("\n[✓] Data berhasil diupdate.");
        System.out.println("Data setelah edit:");
        cetak(list);
    }

    // auto increment id
    public static int getNextId(List<Barang> list) throws Exception {
        int max = 0;

        for (Barang b : list) {
            if (b.id > max) {
                max = b.id;
            }
        }

        return max + 1;
    }


    // bakal dihapus
    public static Barang cariById(int id) throws Exception {
        List<Barang> list = load(filePath);
        for (Barang b : list) {
            if (b.id == id) {
                return b;
            }
        }
        return null;
    }

    // 1. Cetak untuk sebuah List (Banyak Data)
    public static void cetak(List<Barang> list) {
        cetakHeader();
        for (Barang b : list) {

            cetakIsi(b);

        }
        System.out.println("└───────┴─────────────────┴─────────────────┴──────────┴────────────┘");
    }

    // Cetak untuk 1 Object Barang saja
    public static void cetak(Barang b) {
        cetakHeader();
        cetakIsi(b);
        System.out.println("└───────┴─────────────────┴─────────────────┴──────────┴────────────┘");
    }

    // Helper Method: Cetak Header Table
    private static void cetakHeader() {
        System.out.println();
        System.out.println("┌───────┬─────────────────┬─────────────────┬──────────┬────────────┐");
        System.out.printf("│ %-5s │ %-15s │ %-15s │ %-8s │ %-10s │%n", "ID", "Nama", "Kategori", "Stok", "tersedia ?");
        System.out.println("├───────┼─────────────────┼─────────────────┼──────────┼────────────┤");
    }

    // Helper Method: Cetak Isi / Data Barang
    private static void cetakIsi(Barang b) {
        String statusText = b.status ? "ya" : "tidak ";
        System.out.printf("│ %-5d │ %-15s │ %-15s │ %-8d │ %-10s │%n", b.id, b.nama, b.kategori, b.stok, statusText);
    }

    // helper method for integer input error handling
    public static int cekIputInt(String msg, Scanner input) {

        boolean success = false;
        int angka = 0;
        while (!success) {
            System.out.print(msg + " :");
            String data = input.nextLine();
            try {
                angka = Integer.parseInt(data);
                success = true;
            } catch (NumberFormatException e) {
                System.out.println("[!] Input tidak valid. Masukkan angka.");
            }

        }
        return angka;

    }
// ===== SEARCH =====

// Linear Search (nama)
    public static List<Barang> linearSearchNama(String keyword) throws Exception {
        List<Barang> list = load(filePath);
        List<Barang> hasil = new ArrayList<>();

        for (Barang b : list) {
            if (b.nama.toLowerCase().contains(keyword.toLowerCase())) {
                hasil.add(b);
            }
        }

        return hasil;
    }

// Binary Search (ID)
    public static Barang binarySearchId(int idCari) throws Exception {
        List<Barang> list = load(filePath);

        list.sort((a, b) -> a.id - b.id);

        int kiri = 0;
        int kanan = list.size() - 1;

        while (kiri <= kanan) {
            int tengah = (kiri + kanan) / 2;

            if (list.get(tengah).id == idCari) {
                return list.get(tengah);
            } else if (list.get(tengah).id < idCari) {
                kiri = tengah + 1;
            } else {
                kanan = tengah - 1;
            }
        }

        return null;
    }
}

// ┌──────────────────────────────────┬─────────┬────────────────────────┬──────────┬────────────────┐
// │ Col1 │ Col2 │ Col3 │ col4 │ Numeric Column │
// ├──────────────────────────────────┼─────────┼────────────────────────┼──────────┼────────────────┤
// │ Value 1 │ Value 2 │ 123 │ 10.0 │ │
// │ Separate │ cols │ with a tab or 4 spaces │ -2,027.1 │ │
// │ This is a row with only one cell │ │ │ │ │
// └──────────────────────────────────┴─────────┴────────────────────────┴──────────┴────────────────┘
