package services;

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

    public int getId()                { return id; }
    public String getNama()           { return nama; }
    public int getStok()              { return stok; }
    public kategoriBarang getKategori(){ return kategori; }
    public boolean isStatus()         { return status; }
}
