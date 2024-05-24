package com.api.autotest.common.utils;

import com.helger.commons.io.resource.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by harvey.xu on 2017/9/29.
 */
public class ResourceUtils {

    public String getPath(String path) {
        // 获取jar包内文件路径
        String filePath = String.valueOf(new ClassPathResource(path).getAsURL());

        //System.out.println("filePath = " + filePath);
        return filePath;
    }

    public String getResource(String resource) {
        //返回读取指定资源的输入流
        InputStream is = this.getClass().getResourceAsStream(resource);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String str;
        try {
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        String resource = "/key/tomcat.cer";
        ResourceUtils res = new ResourceUtils();
        System.out.println(res.getResource(resource));
    }

}
