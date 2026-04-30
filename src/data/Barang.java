package data;

public class Barang {
    public int id;
    public String nama;
    public int stok;
    public kategoriBarang kategori;
    public boolean status;

    public Barang(int id, String nama, int stok, kategoriBarang kategori, boolean status) {
        this.id = id;
        this.nama = nama;
        this.stok = stok;
        this.kategori = kategori;
        this.status = status; // false = tidak ada, true = ada
    }
}
