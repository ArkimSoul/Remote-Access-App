package remoteaccessapp.server;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESHelper {
    private static SecretKey secretKey;

    private static Cipher encrypter;
    private static Cipher decrypter;

    private void generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        secretKey = keyGenerator.generateKey();
    }

    public AESHelper() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        generateKey();

        encrypter = Cipher.getInstance("AES");
        encrypter.init(Cipher.ENCRYPT_MODE, secretKey);

        decrypter = Cipher.getInstance("AES");
        decrypter.init(Cipher.DECRYPT_MODE, secretKey);
    }

    public AESHelper(String encodedKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        encrypter = Cipher.getInstance("AES");
        encrypter.init(Cipher.ENCRYPT_MODE, secretKey);

        decrypter = Cipher.getInstance("AES");
        decrypter.init(Cipher.DECRYPT_MODE, secretKey);
    }

    public byte[] encrypt(byte[] bytes) {
        try {
            return encrypter.doFinal(bytes);
        }
        catch (Exception _) {

        }
        return null;
    }

    public byte[] decrypt(byte[] bytes) {
        try {
            return decrypter.doFinal(bytes);
        }
        catch (Exception _) {

        }
        return null;
    }

    public byte[] encodeKey() {
        return secretKey.getEncoded();
    }
}
