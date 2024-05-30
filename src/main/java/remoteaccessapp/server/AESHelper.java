package remoteaccessapp.server;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESHelper {
    private final SecretKey secretKey;

    private final Cipher encrypter;
    private final Cipher decrypter;

    public AESHelper() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        secretKey = keyGenerator.generateKey();

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

    public byte[] encrypt(byte[] data) {
        try {
            return encrypter.doFinal(data);
        }
        catch (Exception _) {

        }
        return null;
    }

    public byte[] decrypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        return decrypter.doFinal(data);
    }

    public byte[] encodeKey() {
        return secretKey.getEncoded();
    }
}
