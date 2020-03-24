package wu;

import java.security.KeyPair;

public class RSAUtilsTest {

    static String publicKey;
    static String privateKey;

    static {
        try {
            KeyPair keyPair = RSAUtils.genKeyPair();
            publicKey = RSAUtils.getPublicKey(keyPair);
            privateKey = RSAUtils.getPrivateKey(keyPair);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

//        RSAUtils.genKeyPairToDist("000000");

//        testSign();
//        System.out.println();
//        test1();
//        System.out.println();
//        test2();
//        System.out.println();
        test3();
    }

    static void test1() throws Exception {
        System.out.println("----------私钥加密——公钥解密");
        String source = "这是一行测试RSA数字签名的无意义文字";
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
        System.out.println("加密后：\r\n" + new String(encodedData));
        byte[] decodedData = RSAUtils.decryptByPublicKey(encodedData, publicKey);
        String target = new String(decodedData);
        System.out.println("解密后: \r\n" + target);
    }


    static void test2() throws Exception {
        System.out.println("----------公钥加密——私钥解密");
        String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encryptByPublicKey(data, publicKey);
        System.out.println("加密后文字：\r\n" + new String(encodedData));
        byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData, privateKey);
        String target = new String(decodedData);
        System.out.println("解密后文字: \r\n" + target);
    }

    static void testSign() throws Exception {
        System.out.println("----------私钥签名——公钥验证签名");
        String source = "这是一行测试RSA数字签名的无意义文字";
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
        String sign = RSAUtils.sign(encodedData, privateKey);
        System.out.println("签名:\r" + sign);
        boolean status = RSAUtils.verify(encodedData, publicKey, sign);
        System.out.println("验证结果:" + status);
    }

    static void test3() throws Exception {
        System.out.println("----------公钥加密——私钥解密2222-------------");
        String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
        KeyPair keyPair = RSAUtils.loadKeyPairFromDist();
        byte[] data = source.getBytes();
        byte[] encodedData = RSAUtils.encrypt(data, keyPair.getPublic());
        System.out.println("加密后文字：\r\n" + new String(encodedData));
        byte[] decodedData = RSAUtils.decrypt(encodedData, keyPair.getPrivate());
        String target = new String(decodedData);
        System.out.println("解密后文字: \r\n" + target);
    }
}
