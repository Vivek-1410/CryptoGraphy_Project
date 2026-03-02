package crypto;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class SignatureUtil {

    // Load Private Key
    public static PrivateKey loadPrivateKey(String filePath) throws Exception {

        String key = new String(Files.readAllBytes(Paths.get(filePath)));
        byte[] keyBytes = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePrivate(spec);
    }

    // Load Public Key
    public static PublicKey loadPublicKey(String filePath) throws Exception {

        String key = new String(Files.readAllBytes(Paths.get(filePath)));
        byte[] keyBytes = Base64.getDecoder().decode(key);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");

        return factory.generatePublic(spec);
    }

    // Sign Data
    public static String signData(String data, PrivateKey privateKey) throws Exception {

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());

        byte[] signedBytes = signature.sign();

        return Base64.getEncoder().encodeToString(signedBytes);
    }

    // Verify Signature
    public static boolean verifySignature(String data, String signatureStr, PublicKey publicKey) throws Exception {

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());

        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);

        return signature.verify(signatureBytes);
    }
}