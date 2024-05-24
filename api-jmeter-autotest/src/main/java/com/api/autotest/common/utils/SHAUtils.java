package com.api.autotest.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by acer on 2017/6/7.
 */
public class SHAUtils {

    public static final String SIGN_ALGORITHMS = "SHA-1";

    /**
     * SHA1 安全加密算法
     */
    public static String sign(String content,String inputCharset)  {
        //获取信息摘要 - 参数字典排序后字符串
        try {
            //指定sha1算法
            MessageDigest digest = MessageDigest.getInstance(SIGN_ALGORITHMS);
            digest.update(content.getBytes(inputCharset));
            //获取字节数组
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
 }
