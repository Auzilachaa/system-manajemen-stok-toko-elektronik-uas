

import data.Barang;
import data.Manager;
import data.kategoriBarang;
import java.util.*;

public class Main {
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        int pilihan = 0;

        while (pilihan != 5) {
            System.out.println("\n=== Sistem Manajemen Stok Toko Elektronik ===");
            System.out.println("1. Tampilkan Barang");
            System.out.println("2. Tambah Barang");
            System.out.println("3. Hapus Barang");
            System.out.println("4. Update Barang");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu (1-5): ");

            pilihan = input.nextInt();
            input.nextLine();

            switch (pilihan) {
                case 1:
                    Manager.cetak(Manager.load(Manager.filePath));
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
                    int key2 = input.nextInt();
                    input.nextLine();
                    Manager.edit(key2, input);
                    break;

                case 5:
                    System.out.println("Program selesai.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }
        }
    }

    static void addMenu() throws Exception {
        List<Barang> list = Manager.load(Manager.filePath);

        System.out.print("Masukkan nama barang: ");
        String nama = input.nextLine();

        int stok = Manager.cekIputInt("Masukkan stok barang", input);

        System.out.println("Masukkan kategori barang:");
        kategoriBarang.showKategori();

        System.out.print("Pilihan kategori: ");
        int pil = Integer.parseInt(input.nextLine());

        kategoriBarang kategori = kategoriBarang.dariNomor(pil);

        System.out.print("Apakah barang tersedia? (true/false): ");
        boolean status = Boolean.parseBoolean(input.nextLine().trim());

        list.add(new Barang(Manager.getNextId(list), nama, stok, kategori, status));

        Manager.save(list);
        Manager.cetak(list);
    }

}


