package com.api.autotest.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.*;

/**
 * Created by harvey.xu on 2017/3/19.
 */
public class TextUtils {

    public static String jm(String content, String path)
            throws UnsupportedEncodingException, CertificateException,
            FileNotFoundException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        byte[] msg = content.getBytes("GBK"); // 待加解密的消息

        // 用证书的公钥加密
        CertificateFactory cff = CertificateFactory.getInstance("X.509");
        FileInputStream fis1 = new FileInputStream(path + "tomcat.cer"); // 证书文件
        Certificate cf = cff.generateCertificate(fis1);
        PublicKey pk1 = cf.getPublicKey(); // 得到证书文件携带的公钥
        Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // 定义算法：RSA
        byte[] dataReturn = null;
        c1.init(Cipher.PUBLIC_KEY, pk1);
        // StringBuilder sb = new StringBuilder();
        for (int i = 0; i < msg.length; i += 100) {
            byte[] doFinal = c1.doFinal(ArrayUtils.subarray(msg, i, i + 100));

            // sb.append(new String(doFinal,"gbk"));
            dataReturn = ArrayUtils.addAll(dataReturn, doFinal);
        }

        BASE64Encoder encoder = new BASE64Encoder();

        String afjmText = encoder.encode(dataReturn);

        return afjmText;
    }

    public static String buildMysign(Map sArray, String key) {
        Map sParaNew = mapFilter(sArray);
        String prestr = createLinkString(sParaNew); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        prestr = prestr + key; // 把拼接后的字符串再与安全校验码直接连接起来
        //System.out.println("==" + prestr + "============");
        String mysign = Md5Encrypt.md5(prestr);
        //System.out.println("=======sign = " + mysign);
        return mysign;
    }

    public static String sign(Map sArray, String key, String signType, String privateKey) {
        String prestr = createLinkString(sArray);
        prestr = prestr + key;
        //System.out.println("=======prestr = " + prestr);
        String charset = (String) sArray.get("charset");
        if (StringUtils.isBlank(charset)) {
            if (sArray.containsKey("_input_charset")) {
                charset = String.valueOf(sArray.get("_input_charset"));
            } else {
                charset = "UTF-8";
            }
        }
        String mysign;
        if ("RSA".equalsIgnoreCase(signType)) {
            mysign = RSAUtils.sign(prestr, privateKey, charset);
        } else if ("SHA".equalsIgnoreCase(signType)) {
            mysign = SHAUtils.sign(prestr, charset);
        } else {
            mysign = Md5Encrypt.md5Charset(prestr, charset);
        }
        //System.out.println("=======sign = " + mysign);
        return mysign;
    }

    /**
     * 功能：除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    @SuppressWarnings("rawtypes")
    public static Map mapFilter(Map sArray) {
        List keys = new ArrayList(sArray.keySet());
        Map sArrayNew = new HashMap();
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) sArray.get(key);
            if(value.equals("") || value == null || key.equalsIgnoreCase("sign") ){//新增notifyid不参加签名,只做标识用
				continue;
			}
            sArrayNew.put(key, value);
        }
        return sArrayNew;
    }

    /**
     * 功能：把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map params) {
        List keys = new ArrayList(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    public static String getSplitStr(String inStr) {
        if (inStr.contains("-")) {
            String outStr = inStr.split("-")[1];
            return outStr;
        }
        return inStr;
    }


}
