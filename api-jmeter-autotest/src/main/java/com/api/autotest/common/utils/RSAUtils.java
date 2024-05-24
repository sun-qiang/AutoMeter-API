package com.api.autotest.common.utils;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by acer on 2017/6/7.
 */
public class RSAUtils {

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final Integer KEY_SIZE = 1024;

    private static final Integer MAX_ENCRYPT_SIZE = 117;

    private static final Integer MAX_DECRYPT_SIZE = 128;

    /**
     * RSA签名
     *
     * @param content       待签名数据
     * @param privateKey    商户私钥
     * @param inputCharset 编码格式
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String inputCharset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Signature signature = Signature
                .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(inputCharset));
            byte[] signed = signature.sign();
            return Base64.encodeBase64String(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * RSA验签名检查
     *
     * @param content        待签名数据
     * @param sign           签名值
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String publicKey, String inputCharset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature
                .getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(inputCharset));
            boolean bverify = signature.verify(Base64.decodeBase64(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解密
     *
     * @param content       密文
     * @param privateKey   商户私钥
     * @param inputCharset 编码格式
     * @return 解密后的字符串
     */
    public static String decrypt(String content, String privateKey, String inputCharset) {

        try{
            PrivateKey prikey = getPrivateKey(privateKey);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, prikey);

            InputStream ins = new ByteArrayInputStream(Base64.decodeBase64(content));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
            byte[] buf = new byte[128];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;

                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    for (int i = 0; i < bufl; i++) {
                        block[i] = buf[i];
                    }
                }
                writer.write(cipher.doFinal(block));
            }
            return new String(writer.toByteArray(), inputCharset);
        }catch (Exception e){

        }
        return null;
    }

    /**
     * 得到私钥
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * encrypt<br>
     * result use BASE64 encode
     *
     * @param publicKeyStr
     * @param needEncrypt
     * @return
     */
    public static byte[] encrypt(String publicKeyStr, byte[] needEncrypt)
    {
        byte[] encrypt = null;
        try
        {
            PublicKey publicKey = getPublicKey(publicKeyStr);
            encrypt = encrypt(publicKey, needEncrypt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return encrypt;
    }

    /**
     *
     * decrypt<br>
     * input was BASE64 encode
     *
     * @param privateKeyStr
     * @param needStr
     * @return
     */
    public static byte[] decrypt(String privateKeyStr, String needStr)
    {
        byte[] decrypt = null;

        try
        {
            byte[] needDecrypt = base64Decode(needStr);
            PrivateKey privateKey = getPrivateKey(privateKeyStr);
            decrypt = decrypt(privateKey, needDecrypt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return decrypt;
    }

    private static byte[] encrypt(Key key, byte[] needEncryptBytes)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
            NoSuchPaddingException, IOException
    {
        if (needEncryptBytes == null)
        {
            return null;
        }

        Cipher cipher = Cipher.getInstance("RSA");

        // encrypt
        cipher.init(Cipher.ENCRYPT_MODE, key);

        ByteArrayInputStream iis = new ByteArrayInputStream(needEncryptBytes);
        ByteArrayOutputStream oos = new ByteArrayOutputStream();
        int restLength = needEncryptBytes.length;
        while (restLength > 0) {
            int readLength = restLength < MAX_ENCRYPT_SIZE ? restLength : MAX_ENCRYPT_SIZE;
            restLength = restLength - readLength;

            byte[] readBytes = new byte[readLength];
            iis.read(readBytes);

            byte[] append = cipher.doFinal(readBytes);
            oos.write(append);
        }
        byte[] encryptedBytes = oos.toByteArray();

        return encryptedBytes;
    }

    private static byte[] decrypt(Key key, byte[] needDecryptBytes) throws IOException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        if (needDecryptBytes == null)
        {
            return null;
        }
        Cipher cipher = Cipher.getInstance("RSA");

        // decrypt
        cipher.init(Cipher.DECRYPT_MODE, key);

        ByteArrayInputStream iis = new ByteArrayInputStream(needDecryptBytes);
        ByteArrayOutputStream oos = new ByteArrayOutputStream();
        int restLength = needDecryptBytes.length;
        while (restLength > 0)
        {
            int readLength = restLength < MAX_DECRYPT_SIZE ? restLength : MAX_DECRYPT_SIZE;
            restLength = restLength - readLength;

            byte[] readBytes = new byte[readLength];
            iis.read(readBytes);

            byte[] append = cipher.doFinal(readBytes);
            oos.write(append);
        }
        byte[] decryptedBytes = oos.toByteArray();

        return decryptedBytes;
    }

    private static PublicKey getPublicKey(String publicKeyStr)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException
    {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        byte[] publicKeyBytes = base64Decode(publicKeyStr);
        KeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;
    }

    /**
     * BASE64 encode
     *
     * @param needEncode
     * @return
     */
    public static String base64Encode(byte[] needEncode)
    {
        String encoded = null;
        if (needEncode != null)
        {
            encoded = new BASE64Encoder().encode(needEncode);
        }
        return encoded;
    }

    /**
     * BASE64 decode
     *
     * @param needDecode
     * @return
     * @throws IOException
     */
    private static byte[] base64Decode(String needDecode) throws IOException
    {
        byte[] decoded = null;
        if (needDecode != null)
        {
            decoded = new BASE64Decoder().decodeBuffer(needDecode);
        }
        return decoded;
    }

    public static String readFile(String filePath, String charSet) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        try {
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            return new String(byteBuffer.array(), charSet);
        } finally {
            fileInputStream.close();
        }

    }

    /**
     *
     * decrypt<br>
     * input was BASE64 encode
     *
     * @param privateKeyStr
     * @param needStr
     * @return
     */
    public static byte[] decryptPub(String privateKeyStr, String needStr)
    {
        byte[] decrypt = null;

        try
        {
            byte[] needDecrypt = base64Decode(needStr);
            PublicKey publicKey = getPublicKey(privateKeyStr);
            decrypt = decrypt(publicKey, needDecrypt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return decrypt;
    }

    /**
     * verify
     *
     * @param publicKeyStr
     * @param needVerify
     * @return
     */
    public static byte[] verify(String publicKeyStr, byte[] needVerify)
    {
        byte[] decrypt = null;
        try
        {
            PublicKey publicKey = getPublicKey(publicKeyStr);
            decrypt = decrypt(publicKey, needVerify);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return decrypt;
    }

    /**
     * sign
     *
     * @param privateKeyStr
     * @param needSign
     * @return
     */
    public static byte[] sign(String privateKeyStr, byte[] needSign)
    {
        byte[] encrypt = null;
        try
        {
            PrivateKey privateKey = getPrivateKey(privateKeyStr);
            encrypt = encrypt(privateKey, needSign);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return encrypt;
    }

}
