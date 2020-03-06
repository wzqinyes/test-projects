package demo;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class KeyGenDemo {

    static String ALGORITHM = "RSA/ECB/PKCS1Padding";

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] encoded = secretKey.getEncoded();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        //cipher.init(Cipher.ENCRYPT_MODE, );
    }
}
