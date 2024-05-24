package com.api.autotest.common.utils;


public abstract class Base64 {

    /**
     * 字符编码
     */
    public final static String ENCODING = "UTF-8";

    /**
     * Base64编码
     *
     * @param data 待编码数据
     * @return String 编码数据
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {

        // 执行编码
        byte[] b = org.apache.commons.codec.binary.Base64.encodeBase64(data.getBytes(ENCODING));

        return new String(b, ENCODING);
    }

    /**
     * Base64安全编码<br>
     * 遵循RFC 2045实现
     *
     * @param data
     *            待编码数据
     * @return String 编码数据
     *
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {

        // 执行编码
        byte[] b = org.apache.commons.codec.binary.Base64.encodeBase64(data.getBytes(ENCODING), true);

        return new String(b, ENCODING);
    }

    /**
     * Base64解码
     *
     * @param data 待解码数据
     * @return String 解码数据
     * @throws Exception
     */
    public static String decode(String data) throws Exception {

        // 执行解码
        byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(data.getBytes(ENCODING));

        return new String(b, ENCODING);
    }

}
