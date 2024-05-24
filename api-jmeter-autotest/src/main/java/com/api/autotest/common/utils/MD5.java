package com.api.autotest.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public abstract class MD5 {

    /**
     * MD5加密
     *
     * @param data
     *            待加密数据
     * @return byte[] 消息摘要
     *
     * @throws Exception
     */
    public static byte[] encodeMD5(String data) throws Exception {

        // 执行消息摘要
        return DigestUtils.md5(data);
    }

    /**
     * MD5加密
     *
     * @param data
     *            待加密数据
     * @return byte[] 消息摘要
     *
     * @throws Exception
     */
    public static String encrypt(String data) {
        // 执行消息摘要
        return DigestUtils.md5Hex(data);
    }
}
