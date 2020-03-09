package wu;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * {@link javax.crypto.Mac} 此类提供“消息验证码”（Message Authentication Code，MAC）算法的功能.
 * MAC 基于秘密密钥提供一种方式来检查在不可靠介质上进行传输或存储的信息的完整性。
 * 通常，消息验证码在共享秘密密钥的两个参与者之间使用，以验证这两者之间传输的信息。
 * 基于加密哈希函数的 MAC 机制也叫作 HMAC
 */
public class MacUtil {

    public static final String ALGORITHM = "HmacSHA256";


    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        String mess = "这是一串需要通过Mac处理的文本！";

        SecretKey key = AESUtil.getRandomSecretKey();  //共享密钥

        String r1 = getHMacMessage(key, mess);
        System.out.println(r1);

        String r2 = getHMacMessage(key, mess);
        System.out.println(r2);

        String r3 = getHMacMessage(AESUtil.getRandomSecretKey(), mess);
        System.out.println(r3);

    }

    static String getHMacMessage(Key key, String src) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSHA256 = Mac.getInstance(ALGORITHM);
        hmacSHA256.init(key);
        byte[] result = hmacSHA256.doFinal(src.getBytes());
        byte[] encode = Base64.getEncoder().encode(result);
        return new String(encode);
    }
}
