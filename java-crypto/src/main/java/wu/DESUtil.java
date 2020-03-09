package wu;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.util.Base64;

	/*
	 * 算法名称/加密模式/填充方式
	 *
	 * AES/CBC/NoPadding (128)
	 * AES/CBC/PKCS5Padding (128)
	 * AES/ECB/NoPadding (128)
	 * AES/ECB/PKCS5Padding (128)
	 * DES/CBC/NoPadding (56)
	 * DES/CBC/PKCS5Padding (56)
	 * DES/ECB/NoPadding (56)
	 * DES/ECB/PKCS5Padding (56)
	 * DESede/CBC/NoPadding (168)
	 * DESede/CBC/PKCS5Padding (168)
	 * DESede/ECB/NoPadding (168)
	 * DESede/ECB/PKCS5Padding (168)
	 * RSA/ECB/PKCS1Padding (2048)
	 * RSA/ECB/OAEPPadding (2048)
	 *
	 * 加密模式：ECB：、CBC：、CFB：、OFB
	 */

public class DESUtil {
    // 算法名称
    public static final String KEY_ALGORITHM = "DES";

    // DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
    public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    /**
     * 生成密钥key对象
     *
     * @param keyStr 密钥字符串
     * @return 密钥对象
     * @throws Exception
     */
    private static SecretKey keyGenerator(String keyStr) throws Exception {
        byte input[] = HexString2Bytes(keyStr);
        //byte input[] = "12345678".getBytes();
        DESKeySpec desKey = new DESKeySpec(input);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generateSecret(desKey);
    }

    // 从十六进制字符串到字节数组转换
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    //十六进制数转换成10进制数
    private static int parse(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @param key 密钥
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        // 实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化Cipher对象，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] results = cipher.doFinal(data.getBytes());
        // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
        return new String(Base64.getEncoder().encode(results));
    }

    /**
     * 解密数据
     *
     * @param data 待解密数据
     * @param key 密钥
     * @return 解密后的数据
     */
    public static String decrypt(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        // 执行解密操作
        byte[] results = cipher.doFinal(Base64.getDecoder().decode(data.getBytes()));
        return new String(results);
    }


    public static void main(String[] args) throws Exception {
        String source = "helloworld";
        System.out.println("原文: " + source);
        String key = "0123456789ABCDEF";
        String encryptData = encrypt(source, key);
        System.out.println("加密后: " + encryptData);
        String decryptData = decrypt(encryptData, key);
        System.out.println("解密后: " + decryptData);
    }

}
