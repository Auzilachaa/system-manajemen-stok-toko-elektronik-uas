# Sistem Manajemen Stok Toko Elektronik

Aplikasi **Sistem Manajemen Stok Toko Elektronik** adalah sebuah program Command Line Interface (CLI) berbasis Java yang dirancang untuk membantu toko elektronik dalam mengelola data persediaan barang (stok) secara efisien. Aplikasi ini memungkinkan pengguna untuk mendata barang masuk, melihat stok saat ini, serta menyimpan semua perubahan data secara permanen menggunakan format JSON.

## Tujuan Project

Project ini dibuat untuk:
- Membantu pengelolaan stok barang toko elektronik
- Mengurangi kesalahan pencatatan manual
- Mempermudah monitoring persediaan barang
- Menerapkan konsep pemrograman Java dalam studi kasus nyata
  
## 🚀 Fitur

- **Menambah Barang Baru:** Menginput nama barang, stok awal, kategori (misal: Elektronik, Aksesoris), dan status ketersediaan.
- **Melihat Daftar Barang:** Menampilkan semua data barang beserta jumlah stoknya.
- **Edit data barang:** Dapat merubah stok,harga dan kategori barang.
- **Update Stok Barang:** Mengubah atau memperbarui jumlah stok barang yang sudah ada.
- **Hapus Barang:** Menghapus data barang dari sistem jika barang sudah tidak dijual.
- **Cari berdasarkan Nama, ID/kode dan kategori barang:** Mencari data barang sesuai nama,id/kode dan kategori yang kita mau.
- **Sorting berdasarkan Nama, ID/kode dan jumlah stok terbanyak:** Mensorting data barang sesuai nama,id/kode dan jumlah stok terbanyak.
- **Antarmuka Interaktif (Menu):** Membuat menu CLI yang lebih interaktif agar pengguna dapat memilih aksi (Tambah, Lihat, Update, Hapus, Keluar) tanpa harus menjalankan program berulang kali.
- **Penyimpanan Permanen (JSON):** Data otomatis disimpan ke dalam file `data.json` dan dimuat kembali saat aplikasi dijalankan, sehingga data tidak hilang.
- **Hasil pencarian berupa file:** Membuat file dari hasil yang kita cari untuk mendukung pengambilan keputusan.

## 💻 Tech Stack

- **Bahasa Pemrograman:** Java (Target & Source minimum versi 21)
- **Dependency Management & Build Tool:** Maven
- **Penyimpanan Data:** JSON (JavaScript Object Notation)
- **Library Tambahan:** 
  - `Gson` by Google (versi 2.10.1) untuk membaca (parsing) dan menulis format JSON dengan mudah.

## 📁 Struktur Project

```text
system-manajemen-stok-toko-elektronik-uas/
├── pom.xml                     # File konfigurasi Maven dan dependencies
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── toko/
│                   ├── Main.java                # Titik masuk (entry point) aplikasi
│                   ├── data/
│                   │   ├── Barang.java          # Model data untuk objek Barang
│                   │   └── Manager.java         # Logika untuk load, save, add, dll (interaksi JSON)
│                   └── resources/
│                       └── data.json            # File database berformat JSON
└── target/                     # Hasil kompilasi Maven
```

## 🛠️ Cara Run

1. **Download dan Install Java** Sebelum menjalankan project, pastikan Java sudah terinstall di laptop.
2. **Clone Repository GitHub**
3. **Masuk ke Folder Project**
4. **Masuk ke Folder Source Code**
5. **Compile Program Java**
6. **Jalankan Program**
   
## 📊 Contoh Data

Aplikasi ini menggunakan file `data.json` yang secara otomatis diproses dari entri input Anda. Berikut adalah contoh struktur data barang yang tersimpan:

```json
[
  {
    "id": 1,
    "nama": "Laptop",
    "stok": 10,
    "kategori": "Elektronik",
    "status": true
  },
  {
    "id": 2,
    "nama": "Mouse",
    "stok": 50,
    "kategori": "Aksesoris",
    "status": true
  },
  {
    "id": 3,
    "nama": "charger Typec",
    "stok": 20,
    "kategori": "aksesoris",
    "status": true
  }
]
```

## ⚙️ Cara Kerja Project Ini

1. **Inisialisasi Data:** Saat program dijalankan melalui `Main.main()`, program akan memanggil `Manager.load()` untuk me-load seluruh data yang ada dalam file `data.json`.
2. **Input Pengguna:** Program menggunakan `Scanner` untuk meminta input interaktif di terminal, menanyakan informasi terkait barang yang ingin ditambahkan (Nama, Stok, Kategori, Status).
3. **Pembuatan Objek & Auto-Increment ID:** Input dari pengguna dibungkus menjadi sebuah objek model `Barang`. Untuk ID, program akan mencari ID paling besar yang ada dalam collection (melalui fungsi `Manager.getNextId`), lalu menetapkan ID unik berurut untuk barang tersebut.
4. **Menyimpan Kembali (Save):** Objek `Barang` baru tersebut ditambahkan ke dalam list, lalu disave dengan memanggil `Manager.save()` untuk menulis ulang file `data.json` menggunakan format library Gson (Pretty Printing aktif otomatis).
5. **Output Hasil:** Program meload kembali data (untuk memastikan data tersimpan) lalu me-loop isi list dan mencetak status `Nama Barang - Jumlah Stok` secara keseluruhan di console.
