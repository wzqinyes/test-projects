package wu;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class EncrypRSA2 {

    private final static String KEY_ALGORITHM = "RSA";

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param srcBytes 原文
     * @return
     */
    protected static byte[] encrypt(PublicKey publicKey, byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (publicKey != null) {
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(srcBytes);
        }
        return null;
    }

    /**
     * 私钥解密
     * @param privateKey 私钥
     * @param srcBytes 密文
     * @return
     */
    protected static byte[] decrypt(PrivateKey privateKey, byte[] srcBytes) throws NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        if (privateKey != null) {
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            // 根据私钥，对Cipher对象进行初始化
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(srcBytes);
        }
        return null;
    }

    public static void genKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        FileOutputStream out1 = new FileOutputStream("Private.key");
        out1.write(keyPair.getPrivate().getEncoded());
        out1.close();
        FileOutputStream out2 = new FileOutputStream("Public.key");
        out2.write(keyPair.getPublic().getEncoded());
        out2.close();
    }

    public static PublicKey getRSAPublicKey(BigInteger modulus, BigInteger publicExponent) throws
        NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        return factory.generatePublic(keySpec);
    }

    public static PrivateKey getRSAPrivateKey(BigInteger modulus, BigInteger privateExponent) throws
        NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new RSAPrivateKeySpec(modulus, privateExponent);
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        return factory.generatePrivate(keySpec);
    }

    public static void main(String[] args) throws Exception {
        String msg = "郭XX-精品相声";
        System.out.println("明文是:" + msg);

        //非对称密钥内容
        BigInteger modulus = new BigInteger("8746747582928812734998396715372779287408837877406964610159077075629304133140826192973568501595603600017280131004304057613122403076531446063245331835766973");
        BigInteger privateExponent = new BigInteger("3066840299496790940041932925256741920523790920929164285469970125591433536405979822279720459763509900721125998929508827354634072644371720086654594264592193");
        BigInteger publicExponent = new BigInteger("65537");

        PrivateKey privateKey = getRSAPrivateKey(modulus, privateExponent);
        PublicKey publicKey = getRSAPublicKey(modulus, publicExponent);

        byte[] resultBytes = encrypt(publicKey, msg.getBytes());
        System.out.println("加密后是:" + new String(Base64.getEncoder().encode(resultBytes)));

        byte[] decBytes = decrypt(privateKey, resultBytes);
        System.out.println("解密后是:" + new String(decBytes));
    }


}
