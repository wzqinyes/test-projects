package wu;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    public static final String ALGORITHM = "AES";

	/**
	 * 加密
	 *
	 * @param content 需要加密的内容
	 * @param secretKey 密钥
	 * @return
	 */
	public static byte[] encrypt(String content, SecretKey secretKey) {
		try {
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, ALGORITHM);

			Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            return cipher.doFinal(byteContent);  // 加密
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**解密
	 * @param content  待解密内容
	 * @param secretKey 密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, SecretKey secretKey) {
		try {
			byte[] enCodeFormat = secretKey.getEncoded();

			SecretKeySpec key = new SecretKeySpec(enCodeFormat, ALGORITHM);  //创建密钥

			Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            return cipher.doFinal(content);   //解密
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
	}

    /**
     * 获取随机密钥
     * @return
     * @throws NoSuchAlgorithmException
     */
	public static SecretKey getRandomSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128);
        return keyGen.generateKey();
	}

    public static SecretKey getSecretKey(String password) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128, new SecureRandom(password.getBytes()));
        return keyGen.generateKey();
    }

	public static void main(String[] args) throws NoSuchAlgorithmException {
		String content = "hello, world";
		String password = "12345678";
		System.out.println("加密前：" + content);
        //加密
        SecretKey secretKey = getSecretKey(password);
		byte[] encryptResult = encrypt(content, secretKey);
		System.out.println("加密后：" + new String(Base64.getEncoder().encode(encryptResult)));

		//解密
        secretKey = getSecretKey(password);
		byte[] decryptResult = decrypt(encryptResult, secretKey);
		System.out.println("解密后：" + new String(decryptResult));

        System.out.println(System.getenv("env1"));
        System.out.println(System.getProperty("param1"));
        System.out.println();


        secretKey = getRandomSecretKey();
        System.out.println("随机密钥：" + new String(Base64.getEncoder().encode(secretKey.getEncoded())));
        //加密
        encryptResult = encrypt(content, secretKey);
        System.out.println("加密后：" + new String(Base64.getEncoder().encode(encryptResult)));
        //解密
        decryptResult = decrypt(encryptResult, secretKey);
        System.out.println("解密后：" + new String(decryptResult));
    }
}
