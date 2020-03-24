package wu;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class EncrypRSA {

    private final static String KEY_ALGORITHM = "RSA";

    /**
     * @param key 密钥
     * @param srcBytes 原文
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    protected byte[] encrypt(Key key, byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        if (key != null) {
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(srcBytes); //加密
        }
        return null;
    }

    /**
     * @param key 密钥
     * @param srcBytes 密文
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    protected byte[] decrypt(Key key, byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        if (key != null) {
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            // 根据私钥，对Cipher对象进行初始化
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(srcBytes);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        //genKeyPair("RSA", 512);
        EncrypRSA rsa = new EncrypRSA();

        String msg = "郭XX-精品相声";
        System.out.println("明文是:" + msg);

        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 用公钥加密
        byte[] resultBytes = rsa.encrypt(publicKey, msg.getBytes());
        System.out.println("加密后是:" + new String(Base64.getEncoder().encode(resultBytes)));
        // 用私钥解密
        byte[] decBytes = rsa.decrypt(privateKey, resultBytes);
        System.out.println("解密后是:" + new String(decBytes));

        // 用私钥加密
        resultBytes = rsa.encrypt(privateKey, msg.getBytes());
        System.out.println("加密后是:" + new String(Base64.getEncoder().encode(resultBytes)));
        // 用公钥解密
        decBytes = rsa.decrypt(publicKey, resultBytes);
        System.out.println("解密后是:" + new String(decBytes));

        System.out.println(" -----------------public.key-------------------- ");
        URL url = EncrypRSA.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
        File file = new File(filePath, "keys/public.key");
        if (file.exists()) {
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            is.close();
            System.out.println(new String(bytes));
        }

        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getProperty("java.io.tmpdir"));
        System.out.println(File.separator);

    }


}
