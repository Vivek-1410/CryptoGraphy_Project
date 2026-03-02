package app;

import crypto.SignatureUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Main {

    public static void main(String[] args) {

        try {

            // ORIGINAL PRESCRIPTION
            String originalData =
                    "DoctorID:D1023|Medicine:Paracetamol|Dosage:500mg|Date:2026-03-02";

            // Load keys
            PrivateKey privateKey =
                    SignatureUtil.loadPrivateKey("keys/doctor_private.key");

            PublicKey publicKey =
                    SignatureUtil.loadPublicKey("keys/doctor_public.key");

            // Doctor signs ORIGINAL data
            String signature =
                    SignatureUtil.signData(originalData, privateKey);

            System.out.println("Original Data:");
            System.out.println(originalData);

            System.out.println("\nGenerated Signature:");
            System.out.println(signature);

            // NOW SIMULATE TAMPERING
            String tamperedData =
                    "DoctorID:D1023|Medicine:Ibuprofen|Dosage:500mg|Date:2026-03-02";

            System.out.println("\nTampered Data:");
            System.out.println(tamperedData);

            // Verify tampered data using ORIGINAL signature
            boolean isValid =
                    SignatureUtil.verifySignature(tamperedData, signature, publicKey);

            System.out.println("\nVerification Result: " + isValid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}