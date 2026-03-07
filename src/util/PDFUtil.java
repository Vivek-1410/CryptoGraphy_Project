package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class PDFUtil {

    public static void generatePDF(
            String doctorID,
            String prescriptionID,
            String medicine,
            String dosage,
            String date,
            int validDays) throws Exception {

        Document document = new Document();

        PdfWriter.getInstance(document,
                new FileOutputStream("prescription.pdf"));

        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph title = new Paragraph("Digital Medical Prescription", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Doctor ID: " + doctorID));
        document.add(new Paragraph("Prescription ID: " + prescriptionID));
        document.add(new Paragraph("Medicine: " + medicine));
        document.add(new Paragraph("Dosage: " + dosage));
        document.add(new Paragraph("Date: " + date));
        document.add(new Paragraph("Valid Days: " + validDays));

        document.add(new Paragraph("\nScan QR below for verification:\n"));

        Image qr = Image.getInstance("prescription_qr.png");
        qr.scaleAbsolute(200, 200);
        qr.setAlignment(Element.ALIGN_CENTER);

        document.add(qr);

        document.close();

        System.out.println("PDF generated: prescription.pdf");
    }
}