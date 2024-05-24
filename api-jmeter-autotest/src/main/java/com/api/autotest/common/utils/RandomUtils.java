package com.api.autotest.common.utils;

import java.util.Random;

/**
 * Created by harvey.xu on 2017/3/25.
 */
public class RandomUtils {

    /**
     * 生成N位随机数
     * @param n
     * @return num
     */
    public static int random(Integer n) {
        int num = 0;

        for(int i = 0; i < n; i++){
            num += (int)(Math.random()*10);
        }
        return num;
    }

    /**
     * 生成0.01~0.10的随机数
     * @return
     */
    public static float floatRandom() {
        float num = 0.01f;
        num += (float)(Math.random()/10);

        return (float)(Math.round(num*100))/100;
    }

    /**
     * 生成时间戳随机数
     * @return num
     */
    public static String random() {
        String num;
        num = System.currentTimeMillis() + String.valueOf((long) (Math.random() * 1000000));
        return num;
    }

    /**
     * java生成随机数字和字母组合
     * @paramlength随机值长度
     * @return String
     */
    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 生成随机数
     * @param length
     * @return
     */
    public static String getNumber(int length){
        String Num = "";
        Random random = new Random();
        for(int i = 0;i < length;i++)
        {
            Num += String.valueOf(random.nextInt(10));
        }
        return Num;
    }

    public static void main(String[] args) {
//        System.out.println(random(Integer.parseInt("AUTO_SN_10".split("_")[2])));
        System.out.println(getNumber(15));
    }
}
