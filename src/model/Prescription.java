package model;

import java.time.LocalDate;
import java.util.UUID;

public class Prescription {

    private String doctorID;
    private String prescriptionID;
    private String medicineName;
    private String dosage;
    private String date;

    public Prescription(String doctorID, String medicineName, String dosage) {
        this.doctorID = doctorID;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.prescriptionID = generatePrescriptionID();
        this.date = LocalDate.now().toString();
    }

    private String generatePrescriptionID() {
        return "RX-" + UUID.randomUUID().toString();
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getPrescriptionID() {
        return prescriptionID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getDate() {
        return date;
    }

    // Convert Prescription into canonical string for signing
    public String toSignableString() {
        return "DoctorID:" + doctorID +
                "|PrescriptionID:" + prescriptionID +
                "|Medicine:" + medicineName +
                "|Dosage:" + dosage +
                "|Date:" + date;
    }
}