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

    public static List<Barang> list = new ArrayList<>();

    public static void refreshList() throws Exception {
        list = load(filePath);
    }

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
        Barang b = linearSearchNama(key);
        if (b != null) {
            b.status = false;
            save(list);
        }
    }

    public static void edit(int key2, Scanner input) throws Exception {
        Barang b = binarySearchId(key2);

        if (b == null || !b.status) {
            System.out.println("[!] Data tidak ditemukan.");
            return;
        }
        System.out.println("  EDIT DATA  (Enter = tidak diubah)");
        cetakTersedia(list);
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
                } else {
                    System.out.println("[!] Pilihan tidak valid, kategori tidak diubah.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Kategori tidak diubah.");
            }
        }

        System.out.print("stok [" + b.stok + "]: ");
        String inputJumlah = input.nextLine().trim();
        if (!inputJumlah.isEmpty()) {
            try {
                b.stok = Integer.parseInt(inputJumlah);
            } catch (NumberFormatException e) {
                System.out.println("[!] Bukan angka, stok tidak diubah.");
            }
        }

        try {
            save(list);
            System.out.println("\n[!] Data berhasil diupdate.");
            System.out.println("Data setelah edit:");
            cetakTersedia(list);
        } catch (Exception e) {
            System.out.println("[!] Gagal update");
        }
    }

    // auto increment id
    public static int getNextId(List<Barang> list) {
        int seed = list.isEmpty() ? (int) System.currentTimeMillis() : list.get(list.size() - 1).id;
        int id = Math.abs((1103515245 * seed + 12345) % 100000); // 5 digit
        for (Barang b : list) {
            if (b.id == id) {
                id++;
            }
        }
        return id;
    }


    // Cetak untuk sebuah List (Banyak Data)
    public static void cetakTersedia(List<Barang> list) {
        cetakHeader();
        for (Barang b : list) {

            if (b.status == true) {
                cetakIsi(b);
            }

        }
        System.out.println("└───────┴───────────────────────────┴─────────────────┴──────────┴────────────┘");
    }
        public static void cetakSemua(List<Barang> list) {
        cetakHeader();
        for (Barang b : list) {

            cetakIsi(b);

        }
        System.out.println("└───────┴───────────────────────────┴─────────────────┴──────────┴────────────┘");
    }

    // Cetak untuk 1 Object Barang saja
    public static void cetak(Barang b) {
        cetakHeader();
        cetakIsi(b);
        System.out.println("└───────┴───────────────────────────┴─────────────────┴──────────┴────────────┘");
    }

    // Helper Method: Cetak Header Table
    private static void cetakHeader() {
        System.out.println();
        System.out.println("┌───────┬───────────────────────────┬─────────────────┬──────────┬────────────┐");
        System.out.printf("│ %-5s │ %-25s │ %-15s │ %-8s │ %-10s │%n", "ID", "Nama", "Kategori", "Stok", "tersedia ?");
        System.out.println("├───────┼───────────────────────────┼─────────────────┼──────────┼────────────┤");
    }

    // Helper Method: Cetak Isi / Data Barang
    private static void cetakIsi(Barang b) {
        String statusText = b.status ? "ya" : "tidak ";
        System.out.printf("│ %-5d │ %-25s │ %-15s │ %-8d │ %-10s │%n", b.id, b.nama, b.kategori, b.stok, statusText);
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
    public static Barang linearSearchNama(String keyword) {
        for (Barang b : list) {
            if (b.nama.equalsIgnoreCase(keyword)) {
                return b;
            }
        }
        return null;
    }

    // Binary Search (ID)
    public static Barang binarySearchId(int idCari) {
        // Sort dulu biar binary search bisa jalan
        SortByIdASC();

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

    // ==== SORTING ====

    // Bubble Sort berdasarkan ID (Ascending)
    public static void SortByIdASC() {
        int n = list.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).id > list.get(j + 1).id) {
                    Barang temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            // Optimasi
            if (!swapped)
                break;
        }
    }
    
    // bubble sort berdasarkan ID (descending)
    public static void SortByIdDESC() {
        int n = list.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).id < list.get(j + 1).id) {
                    Barang temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            // Optimasi
            if (!swapped)
                break;
        }
    }

    // Selection Sort berdasarkan Nama (Ascending A-Z)
    public static void selectionSortByNama() throws Exception {
        int n = list.size();
        
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j).nama.compareToIgnoreCase(list.get(minIndex).nama) < 0) {
                minIndex = j;
                }
            }
            if (minIndex != i) {
                Barang temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
            }
        }
    }

    // Insertion Sort berdasarkan Stok/Jumlah (Descending - Terbanyak ke Tersedikit)
    public static void insertionSortByStok() {
        int n = list.size();

        for (int i = 1; i < n; i++) {
            Barang key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).stok < key.stok) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

}
