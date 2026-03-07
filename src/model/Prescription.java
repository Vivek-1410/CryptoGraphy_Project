package model;

import java.time.LocalDate;
import java.util.UUID;

public class Prescription {

    private String doctorID;
    private String prescriptionID;
    private String medicineName;
    private String dosage;
    private String date;
    private int validDays;

    public Prescription(String doctorID, String medicineName, String dosage, int validDays) {
        this.doctorID = doctorID;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.validDays = validDays;
        this.prescriptionID = "RX-" + UUID.randomUUID();
        this.date = LocalDate.now().toString();
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

    public int getValidDays() {
        return validDays;
    }

    public String toSignableString() {
        return "DoctorID:" + doctorID +
                "|PrescriptionID:" + prescriptionID +
                "|Medicine:" + medicineName +
                "|Dosage:" + dosage +
                "|Date:" + date +
                "|ValidDays:" + validDays;
    }
}