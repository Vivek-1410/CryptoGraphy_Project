package app;

import crypto.SignatureUtil;
import model.Prescription;
import util.FileUtil;
import util.PDFUtil;
import util.QRUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Select Mode:");
        System.out.println("1 - Doctor (Create Prescription)");
        System.out.println("2 - Pharmacy (Verify Prescription)");

        int choice = scanner.nextInt();
        scanner.nextLine();

        try {

            if (choice == 1) {
                doctorMode();
            } else if (choice == 2) {
                pharmacyMode();
            } else {
                System.out.println("Invalid option.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doctorMode() throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Doctor ID:");
        String doctorID = scanner.nextLine();

        System.out.println("Enter Medicine Name:");
        String medicine = scanner.nextLine();

        System.out.println("Enter Dosage:");
        String dosage = scanner.nextLine();

        System.out.println("Enter Valid Days:");
        int validDays = scanner.nextInt();

        // Create prescription object
        Prescription prescription = new Prescription(doctorID, medicine, dosage, validDays);

        String signableData = prescription.toSignableString();

        PrivateKey privateKey = SignatureUtil.loadPrivateKey("keys/doctor_private.key");

        String signature = SignatureUtil.signData(signableData, privateKey);

        String json = "{\n" +
                "  \"doctorID\": \"" + prescription.getDoctorID() + "\",\n" +
                "  \"prescriptionID\": \"" + prescription.getPrescriptionID() + "\",\n" +
                "  \"medicine\": \"" + prescription.getMedicineName() + "\",\n" +
                "  \"dosage\": \"" + prescription.getDosage() + "\",\n" +
                "  \"date\": \"" + prescription.getDate() + "\",\n" +
                "  \"validDays\": " + prescription.getValidDays() + ",\n" +
                "  \"signature\": \"" + signature + "\"\n" +
                "}";

        FileUtil.writeToFile("prescription.json", json);

        QRUtil.generateQR(json, "prescription_qr.png");
        PDFUtil.generatePDF(
                prescription.getDoctorID(),
                prescription.getPrescriptionID(),
                prescription.getMedicineName(),
                prescription.getDosage(),
                prescription.getDate(),
                prescription.getValidDays());

        System.out.println("\nPrescription Generated Successfully!");
        System.out.println("QR Code generated: prescription_qr.png");
    }

    private static void pharmacyMode() throws Exception {

        String fileContent = FileUtil.readFromFile("prescription.json");

        String doctorID = extractValue(fileContent, "doctorID");
        String prescriptionID = extractValue(fileContent, "prescriptionID");
        String medicine = extractValue(fileContent, "medicine");
        String dosage = extractValue(fileContent, "dosage");
        String date = extractValue(fileContent, "date");
        String signature = extractValue(fileContent, "signature");
        int validDays = Integer.parseInt(extractValue(fileContent, "validDays"));

        String reconstructedData = "DoctorID:" + doctorID +
                "|PrescriptionID:" + prescriptionID +
                "|Medicine:" + medicine +
                "|Dosage:" + dosage +
                "|Date:" + date +
                "|ValidDays:" + validDays;

        PublicKey publicKey = SignatureUtil.loadPublicKey("keys/doctor_public.key");

        boolean isValid = SignatureUtil.verifySignature(reconstructedData, signature, publicKey);

        if (!isValid) {
            System.out.println("\nVerification Failed: Prescription Tampered!");
            return;
        }

        /* ---------- ADD EXPIRY CHECK HERE ---------- */

        LocalDate prescriptionDate = LocalDate.parse(date);
        LocalDate expiryDate = prescriptionDate.plusDays(validDays);
        LocalDate today = LocalDate.now();

        if (today.isAfter(expiryDate)) {
            System.out.println("Prescription Expired!");
            return;
        }

        /* ---------- CONTINUE WITH REUSE CHECK ---------- */

        // Anti-Reuse Check
        String usedFile = "used_prescriptions.txt";

        String usedIDs = "";
        try {
            usedIDs = FileUtil.readFromFile(usedFile);
        } catch (Exception ignored) {
        }

        if (usedIDs.contains(prescriptionID)) {
            System.out.println("\nPrescription Already Used! Rejecting.");
            return;
        }

        // Mark as used
        FileUtil.writeToFile(usedFile, usedIDs + prescriptionID + "\n");

        System.out.println("\nPrescription Valid and Accepted.");
    }

    private static String extractValue(String json, String key) {
        String pattern = "\"" + key + "\": ";
        int start = json.indexOf(pattern) + pattern.length();

        if (json.charAt(start) == '"') {
            start++;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } else {
            int end = json.indexOf(",", start);
            if (end == -1)
                end = json.indexOf("}", start);
            return json.substring(start, end).trim();
        }
    }
}