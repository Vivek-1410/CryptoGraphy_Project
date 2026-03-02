package crypto;

import java.io.File;
import java.io.FileOutputStream;
import java.security.*;
import java.util.Base64;

public class KeyGeneratorUtil {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    public static void generateAndSaveKeys(String publicKeyPath, String privateKeyPath) throws Exception {

        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(KEY_SIZE);

        KeyPair pair = generator.generateKeyPair();

        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();

        saveKey(publicKey.getEncoded(), publicKeyPath);
        saveKey(privateKey.getEncoded(), privateKeyPath);

        System.out.println("RSA Key Pair Generated Successfully.");
    }

    private static void saveKey(byte[] key, String path) throws Exception {

        File file = new File(path);
        file.getParentFile().mkdirs();

        String encodedKey = Base64.getEncoder().encodeToString(key);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(encodedKey.getBytes());
        }
    }
}