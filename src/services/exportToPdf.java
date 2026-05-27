package services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.io.File;

public class exportToPdf {


    public void exportBarang(List<Barang> daftarBarang) throws FileNotFoundException, DocumentException {

        Font fontBold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
        Font biasa = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);

        Document document = new Document();
        Path path = Paths.get(System.getProperty("user.dir"), "reports/laporan_barang_" + LocalDate.now() + ".pdf");
        String filePath = path.toString();

        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        PdfWriter.getInstance(
                document,
                new FileOutputStream(filePath)
        );

        document.open();
        document.add(
                new Paragraph("Laporan Data Barang", fontBold)
        );

        document.add(
                new Paragraph("tanggal: " + LocalDate.now(), fontBold)
        );

        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[] {2f, 5f, 2f, 4f, 2f});

        table.addCell(
                new PdfPCell(
                        new Paragraph("ID", fontBold)
                )
        );

        table.addCell(
                new PdfPCell(
                        new Paragraph("Nama", fontBold)
                )       
        );

        table.addCell(
                new PdfPCell(
                        new Paragraph("Stok", fontBold)
                )
        );
        
        table.addCell(
                new PdfPCell(
                        new Paragraph("Kategori", fontBold)
                )
        );

        table.addCell(
                new PdfPCell(
                        new Paragraph("Status", fontBold)
                )
        );

        for (Barang barang : daftarBarang) {

            table.addCell(
                    new PdfPCell(new Paragraph(String.valueOf(barang.getId()), biasa))
            );

            table.addCell(
                    new PdfPCell(new Paragraph(barang.getNama(), biasa))
            );

            table.addCell(
                    new PdfPCell(new Paragraph(String.valueOf(barang.getStok()), biasa))
            );

            table.addCell(
                    new PdfPCell(new Paragraph(barang.getKategori().toString(), biasa))
            );

            table.addCell(
                    new PdfPCell(new Paragraph(barang.isStatus() ? "tersedia" : "habis/dihapus", biasa))
            );
        }

        document.add(table);
        document.close();

        System.out.println("PDF berhasil dibuat!");
    }
    
}
