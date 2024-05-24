package com.api.autotest.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RadomVariables {
    //获取GUID
    public String GetGuid()
    {
        String Result="";
        UUID uuid = UUID.randomUUID();
        Result=uuid.toString();
        return Result;
    }

    //随机获取Long
    public long GetRadmomNum(Long Start,Long End)
    {
        long Result=0;
        long longnum= ThreadLocalRandom.current().nextLong(Start,End);
        Result = longnum;
        return Result;
    }

    //随机获取Double
    public double GetRadmomDouble(double Start,double End)
    {
        double Result=0;
        double doublenum= ThreadLocalRandom.current().nextDouble(Start,End);
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        String tmp=df.format(doublenum);
        Result =Double.parseDouble(tmp);
        return Result;
    }

    //随机获取指定长度的字符串
    public String GetRadmomStr(int length)
    {
        String Result="";
        String RadomStr= RandomStringUtils.randomAlphanumeric(length);
//        String Result="";
//        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        StringBuffer sb=new StringBuffer();
//
//        for(int i=0;i<length;i++) {
//            int num= ThreadLocalRandom.current().nextInt(1,length);
//            sb.append(str.charAt(num));
//        }
        Result= RadomStr;
        return Result;
    }

    //获取当前时间
    public String GetCurrentTime()
    {
        String Result="";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Result=df.format(new Date());
        return Result;
    }

    //获取当前时间
    public String GetCurrentDate(String Condition)
    {
        String Result="";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Result=df.format(new Date());
        if(Condition.equalsIgnoreCase("/"))
        {
            df = new SimpleDateFormat("yyyy/MM/dd");//设置日期格式
            Result=df.format(new Date());
        }
        if(Condition.equalsIgnoreCase("-"))
        {
            df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            Result=df.format(new Date());
        }
        return Result;
    }

    //获取当前时间戳
    public long GetCurrentTimeMillis()
    {
        long Result=0;
        Result=System.currentTimeMillis();
        return Result;
    }


    //随机获取IP
    public String GetRadmonIP()
    {
        String Result="";
        Long start=new Long(1);
        Long end=new Long(256);
        String IP=GetRadmomNum(start,end)+"."+GetRadmomNum(start,end)+"."+GetRadmomNum(start,end)+"."+GetRadmomNum(start,end);
        Result=IP;
        return Result;
    }

    public static void main(String[] args) {
    }
}
