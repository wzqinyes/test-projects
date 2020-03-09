package wu;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 消息摘要
 *
 * MessageDigest
 */
public class MDUtil {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");  //MD5、SHA-1、SHA-256
        String message = "这是消息原文";
        byte[] digest = md.digest(message.getBytes());
        String result = new String(Base64.getEncoder().encode(digest));
        System.out.println(result);
    }
}
