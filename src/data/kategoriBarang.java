package data;

public enum kategoriBarang {
    rumahTangga,
    komputer,
    audio,
    video,
    aksesoris,
    lainnya;

    public static void showKategori() {
        for (int i = 0; i < kategoriBarang.values().length; i++) {
            System.out.println((i + 1) + ". " + kategoriBarang.values()[i]);
        }
    }

    public static kategoriBarang dariNomor(int nomor) {
        kategoriBarang[] list = values();
        if (nomor < 1 || nomor > list.length)
            return null;
        return list[nomor - 1];
    }
}
