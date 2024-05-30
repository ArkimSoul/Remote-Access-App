package remoteaccessapp.server;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAHelper {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    private final Cipher encrypter;
    private final Cipher decrypter;

    public RSAHelper() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

        encrypter = Cipher.getInstance("RSA");
        encrypter.init(Cipher.ENCRYPT_MODE, publicKey);

        decrypter = Cipher.getInstance("RSA");
        decrypter.init(Cipher.DECRYPT_MODE, privateKey);
    }

    public RSAHelper(String encodedPublicKey, String encodedPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        byte[] decodedPublicKey = Base64.getDecoder().decode(encodedPublicKey);
        byte[] decodedPrivateKey = Base64.getDecoder().decode(encodedPrivateKey);

        publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedPublicKey));
        privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedPrivateKey));

        encrypter = Cipher.getInstance("RSA");
        encrypter.init(Cipher.ENCRYPT_MODE, publicKey);

        decrypter = Cipher.getInstance("RSA");
        decrypter.init(Cipher.DECRYPT_MODE, privateKey);
    }

    public byte[] encrypt(byte[] data) {
        try {
            return encrypter.doFinal(data);
        }
        catch (Exception _) {

        }
        return null;
    }

    public byte[] decrypt(byte[] data) {
        try {
            return decrypter.doFinal(data);
        }
        catch (Exception _) {

        }
        return null;
    }

    public String encodePublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String encodePrivateKey() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

}
