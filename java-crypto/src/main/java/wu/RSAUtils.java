package wu;


import javax.crypto.Cipher;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class RSAUtils {
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static final String PUBLIC_KEY_FILE = "public.key";
    public static final String PRIVATE_KEY_FILE = "private.key";
    private static final String GEN_KEYS_PATH;  //生成密钥目录
    private static final String LOAD_KEYS_PATH;  //加载密钥目录
    static {
        String genKeysPath = System.getenv("gen_security_keys_path");
        if (genKeysPath != null) {
            GEN_KEYS_PATH = genKeysPath.endsWith(File.separator) ? genKeysPath : genKeysPath + File.separator;
        } else {
            GEN_KEYS_PATH = System.getProperty("user.dir") + File.separator + "genkeys" + File.separator;
        }

        String loadKeysPath = System.getenv("load_security_keys_path");
        if (loadKeysPath != null) {
            LOAD_KEYS_PATH = loadKeysPath.endsWith(File.separator) ? loadKeysPath : loadKeysPath + File.separator;
        } else {
            URL url = RSAUtils.class.getProtectionDomain().getCodeSource().getLocation();
            String filePath;
            try {
                filePath = URLDecoder.decode(url.getFile(), "UTF-8") + "keys" + File.separator;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                filePath = System.getProperty("user.dir") + File.separator + "keys" + File.separator;
            }
            LOAD_KEYS_PATH = filePath;
        }
    }

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static KeyPair genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        return keyPairGen.generateKeyPair();
    }

    /**
     * 生成密钥对文件到磁盘
     * 需要设置环境变量（key生成后存放目录）：gen_security_keys_path，否则存放在默认路径 ${user.dir}/genkeys
     *
     * @return
     * @throws Exception
     */
    public static void genKeyPairToDist()  {
        try {
            KeyPair keyPair = genKeyPair();
            PublicKey publicKey = keyPair.getPublic();       //得到公钥
            PrivateKey privateKey = keyPair.getPrivate();   //得到私钥
            //写到磁盘
            writeFile(PUBLIC_KEY_FILE, publicKey.getEncoded());
            writeFile(PRIVATE_KEY_FILE, privateKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void writeFile(String outfile, byte[] content) {
        OutputStream out = null;
        try {
            File path = new File(GEN_KEYS_PATH);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, outfile);
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            out.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从磁盘加载密钥
     * @return
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public static KeyPair loadKeyPairFromDist() throws IOException, InvalidKeySpecException {
        FileInputStream fis;
        byte[] encodedPublicKey = null;
        byte[] encodedPrivateKey = null;
        PublicKey publicKey = null;
        PrivateKey privateKey = null;

        File filePublicKey = new File(LOAD_KEYS_PATH + PUBLIC_KEY_FILE);
        if (filePublicKey.exists()) {
            fis = new FileInputStream(filePublicKey);
            encodedPublicKey = new byte[(int)filePublicKey.length()];
            fis.read(encodedPublicKey);
            fis.close();
        }

        File filePrivateKey = new File(LOAD_KEYS_PATH + PRIVATE_KEY_FILE);
        if (filePrivateKey.exists()) {
            fis = new FileInputStream(filePrivateKey);
            encodedPrivateKey = new byte[(int)filePrivateKey.length()];
            fis.read(encodedPrivateKey);
            fis.close();
        }

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            if (encodedPublicKey != null) {
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
                publicKey = keyFactory.generatePublic(publicKeySpec);
            }

            if (encodedPrivateKey != null) {
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
                privateKey = keyFactory.generatePrivate(privateKeySpec);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return new KeyPair(publicKey, privateKey);
    }

    /**
     * 生成数字签名，通过私钥对信息进行签名
     *
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);   //此类表示按照 ASN.1 标准 进行编码的专用密钥的 ASN.1 编码
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);   //获取指定签名算法的 Signature 对象
        signature.initSign(privateK);   //初始化这个用于签名的对象，用私钥
        signature.update(data);         //更新要由字节签名或验证的数据。
        return new String(Base64.getEncoder().encode(signature.sign()));
    }

    /**
     * 验证数字签名，通过公约进行验证
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);    //此类表示根据 ASN.1标准 进行编码的公用密钥的 ASN.1 编码
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);   //获取指定签名算法的 Signature 对象
        signature.initVerify(publicK);    //初始化这个用于验证的对象，用公钥
        signature.update(data);           //更新要由字节签名或验证的数据。
        return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
    }

    /**
     * 用私钥解密
     *
     * @param encryptedData 已被公钥加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return 已解密的数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        return decrypt(encryptedData, privateK);
    }

    /**
     * 用公钥解密
     *
     * @param encryptedData 已被私钥加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return 已解密的数据
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        return decrypt(encryptedData, publicK);
    }

    /**
     * 解密
     *
     * @param encryptedData 已被私钥/公钥 加密数据
     * @param key 密钥，这里应该是跟encryptedData配对的公钥/私钥
     * @return 已解密的数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] encryptedData, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥加密
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        return encrypt(data, publicK);
    }

    /**
     * 私钥加密
     *
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        return encrypt(data, privateK);
    }

    /**
     * 对数据加密
     *
     * @param data 原文
     * @param key 密钥（公钥或私钥）
     * @return 密文
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 获取私钥
     *
     * @param keyPair 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(KeyPair keyPair) {
        Key key = keyPair.getPrivate();
        byte[] b = key.getEncoded();    //该方法获取key的编码
        return new String(Base64.getEncoder().encode(b));
    }

    /**
     * 获取公钥
     *
     * @param keyPair 密钥对
     * @return
     */
    public static String getPublicKey(KeyPair keyPair) {
        Key key = keyPair.getPublic();
        byte[] b = key.getEncoded();    //该方法获取key的编码
        return new String(Base64.getEncoder().encode(b));
    }
}
