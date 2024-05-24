package com.api.autotest.dto;

import java.io.File;

/**
 * Created by harvey.xu on 2017/09/29.
 */
public class SshConnectDto {

    String serverIp;
    String userName;
    String password;
    File keyFile;
    String keyFilePass;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public File getKeyFile() {
        return keyFile;
    }

    public void setKeyFile(File keyFile) {
        this.keyFile = keyFile;
    }

    public String getKeyFilePass() {
        return keyFilePass;
    }

    public void setKeyFilePass(String keyFilePass) {
        this.keyFilePass = keyFilePass;
    }
}
