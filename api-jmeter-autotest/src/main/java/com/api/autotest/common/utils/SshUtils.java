package com.api.autotest.common.utils;

import ch.ethz.ssh2.*;
import com.api.autotest.dto.SshConnectDto;
import org.apache.commons.lang3.StringUtils;
import java.io.*;

/**
 * Created by harvey.xu on 2017/9/29.
 */
public class SshUtils {

    private static Connection connection;

    /**
     * 使用公私密钥连接和认证远程Linux主机
     * @return boolean
     */
    public static boolean connectAndAuthByKey(SshConnectDto sshConnectDto)
    {
        boolean isConnAndAuth = false;
        if (null != sshConnectDto.getServerIp()) {
            connection = new Connection(sshConnectDto.getServerIp());
            try {
                connection.connect();
                isConnAndAuth = connection.authenticateWithPublicKey(sshConnectDto.getUserName(), sshConnectDto.getKeyFile(), sshConnectDto.getKeyFilePass());
            } catch (Exception e) {
                System.out.println("无法连接和认证以下主机,IP:" + sshConnectDto.getServerIp() + ",keyFile:" + sshConnectDto.getKeyFile() + "，请检查IP，公钥或者密码是否正确!");
                e.printStackTrace();
                //如果存在主机连接或者认证失败，那么设置连接是否的标识为false
                isConnAndAuth = false;
            }
        }
        return isConnAndAuth;
    }

    /**
     * 使用用户名密码连接和认证远程Linux主机
     * @return boolean
     */
    public static boolean connectAndAuth(String serverIp, String userName, String password)
    {
        boolean isConnAndAuth = false;
        if (null != serverIp) {
            connection = new Connection(serverIp);
            try {
                connection.connect();
                isConnAndAuth = connection.authenticateWithPassword(userName, password);
            } catch (Exception e) {
                System.out.println("无法连接和认证以下主机,IP:" + serverIp + ",用户名:" + userName + "，请检查IP，用户名或者密码是否正确!");
                e.printStackTrace();
                //如果存在主机连接或者认证失败，那么设置连接是否的标识为false
                isConnAndAuth = false;
            }
        }
        return isConnAndAuth;
    }

    /**
     * 使用用户密码上传本地文件到远程服务器端，即将本地的文件localFile上传到远程Linux服务器中的remoteTargetDirectory目录下
     * @param localFile
     * @param remoteTargetDirectory
     */
    public static void uploadFile(String serverIp, String userName, String password, String localFile, String remoteTargetDirectory) {
        if (connectAndAuth(serverIp, userName, password)) {
            Connection conn = connection;
            try {
                SCPClient scpClient = conn.createSCPClient();
                scpClient.put(localFile, remoteTargetDirectory,"0644");
            } catch (Exception e) {
                System.out.println("获取ssh上传客户端失败!");
                e.printStackTrace();
            }
        }
        closeConnection();
    }

    /**
     * 使用公私密钥上传本地文件到远程服务器端，即将本地的文件localFile上传到远程Linux服务器中的remoteTargetDirectory目录下
     * @param localFile
     * @param remoteTargetDirectory
     */
    public static boolean uploadFile(SshConnectDto sshConnectDto, String localFile, String remoteTargetDirectory) {
        Boolean uploadResult = false;
        try {
            if (connectAndAuthByKey(sshConnectDto)) {
                System.out.println("使用公钥连接和认证远程服务器成功!");
                Connection conn = connection;

                System.out.println("************上传文件到远程服务器端开始************");
                System.out.println("FileName：" + localFile);
                SCPClient scpClient = conn.createSCPClient();
                scpClient.put(localFile, remoteTargetDirectory, "0644");
                uploadResult = true;
                System.out.println("************上传文件到远程服务器端完成************");
            } else {
                System.out.println("使用公钥连接和认证远程服务器失败!");
            }
        } catch (Exception e) {
            System.out.println("上传文件到远程服务器端失败!");
            e.printStackTrace();
            uploadResult = false;
        } finally {
            try {
                closeConnection();
            } catch (Exception e) {
                System.out.println("关闭SSH连接异常!");
                e.printStackTrace();
            }
        }
        return uploadResult;
    }

    /**
     * 从远程服务器端下载文件到本地指定的目录中
     * @param remoteFile
     * @param localTargetDirectory
     */
    public void downloadFile(String serverIp, String userName, String password, String remoteFile,String localTargetDirectory)
    {
        if (connectAndAuth(serverIp, userName, password))
        {
            try {
                SCPClient scpClient = connection.createSCPClient();
                scpClient.get(remoteFile, localTargetDirectory);
            } catch (Exception e) {
                System.out.println("获取ssh下载客户端失败!");
                e.printStackTrace();
            }
            closeConnection();
        }
    }

    /**
     * 在远端linux上创建文件夹
     * @param dirName 文件夹名称
     * @param posixPermissions 目录或者文件夹的权限
     */
    public void mkDir(String serverIp, String userName, String password, String dirName,int posixPermissions)
    {
        if (connectAndAuth(serverIp, userName, password))
        {
            System.out.println("************开始创建目录:" + dirName + "************");
            Connection conn = connection;
            try {
                SFTPv3Client sftpClient = new SFTPv3Client(conn);
                sftpClient.mkdir(dirName, posixPermissions);
            } catch (Exception e) {
                System.out.println("************目录:" + dirName + "已经存在!************");
                e.printStackTrace();
            }
        }
        closeConnection();
    }

    /**
     * 删除远端Linux服务器上的文件
     * @param filePath
     */
    public void rmFile(String serverIp, String userName, String password, String filePath)
    {
        if (connectAndAuth(serverIp, userName, password))
        {
            System.out.println("************开始删除文件:" + filePath + "************");
            Connection conn = connection;
            try {
                SFTPv3Client sftpClient = new SFTPv3Client(conn);
                sftpClient.rm(filePath);
            } catch (Exception e) {
                System.out.println("************文件:" + filePath + "不存在!************");
                e.printStackTrace();
            }
        }
        closeConnection();
    }

    /**
     * 删除远端Linux服务器上的一个空文件夹
     * @param dirName
     */
    public void rmEmptyDir(String serverIp, String userName, String password, String dirName)
    {
        if (connectAndAuth(serverIp, userName, password)) {
            Connection conn = connection;
            try
            {
                SFTPv3Client sftpClient = new SFTPv3Client(conn);
                sftpClient.rmdir(dirName);
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            closeConnection();
        }
    }

    /**
     * 在远程Linux服务器端移动文件或者文件夹到新的位置
     * @param oldPath
     * @param newPath
     */
    public void moveFileOrDir(String serverIp, String userName, String password, String oldPath,String newPath)
    {
        if (connectAndAuth(serverIp, userName, password))
        {
            Connection conn = connection;
                try
                {
                    SFTPv3Client sftpClient = new SFTPv3Client(conn);
                    sftpClient.mv(oldPath,newPath);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            closeConnection();
        }
    }

    /**
     * 在远程Linux服务器上执行命令
     * @param cmd
     * @param isQuery
     */
    public static String execCommand(SshConnectDto sshConnectDto, String cmd, boolean isQuery)
    {
        StringBuffer sb = new StringBuffer();
        try {
            boolean isAuthenticated;
            Connection conn = new Connection(sshConnectDto.getServerIp());
            conn.connect();
            if (StringUtils.isNotEmpty(sshConnectDto.getPassword())) {
                isAuthenticated = conn.authenticateWithPassword(sshConnectDto.getUserName(), sshConnectDto.getPassword());
            } else {
                isAuthenticated = conn.authenticateWithPublicKey(sshConnectDto.getUserName(), sshConnectDto.getKeyFile(), sshConnectDto.getKeyFilePass());
            }
            if (isAuthenticated) {
                Session session;
                if (isQuery) {
                    try {
                        session = conn.openSession();
                        InputStream stdout = new StreamGobbler(session.getStdout());
                        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                        String content;
                        while ((content = br.readLine()) != null)
                        {
                            sb.append(content);
                        }
                        //获得退出状态
                        System.out.println("ExitCode: " + session.getExitStatus());
                        session.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        session = conn.openSession();
                        session.execCommand(cmd);
                        session.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 关闭连接
     *
     */
    public static void closeConnection() {
        if (null != connection) {
            Connection conn = connection;
            conn.close();
        }
    }

    /**
     * 远程 执行命令并返回结果调用过程 是同步的（执行完才会返回）
     * @param sshConnectDto
     * @param command
     * @return
     */
    public static Integer exec(SshConnectDto sshConnectDto, String command){
        int result = 0;
        try {
            boolean isAuthenticated;
            Connection conn = new Connection(sshConnectDto.getServerIp());
            conn.connect();
            if (StringUtils.isNotEmpty(sshConnectDto.getPassword())) {
                isAuthenticated = conn.authenticateWithPassword(sshConnectDto.getUserName(), sshConnectDto.getPassword());
            } else {
                isAuthenticated = conn.authenticateWithPublicKey(sshConnectDto.getUserName(), sshConnectDto.getKeyFile(), sshConnectDto.getKeyFilePass());
            }

            if (isAuthenticated == false) {
                throw new IOException("Authentication failed.");
            }

            Session session = conn.openSession();

            session.execCommand(command);

            System.out.println("****************Begin execute command : " + command);

            InputStream stdout = new StreamGobbler(session.getStdout());

            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                System.out.println(line);
            }

            System.out.println("****************Finish execute command : " + command);
            session.waitForCondition(ChannelCondition.EXIT_STATUS, 60L);
            result = session.getExitStatus();

            System.out.println("ExitCode: " + session.getExitStatus());

            session.close();

            conn.close();

        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(2);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {

        String ip = "47.100.31.205";
        String user = "wuser";
//        String pwd = "123456";
//        String localFile = "D:\\log\\TRR-20160315.01.001.csv";
//        String remote = "/data/paypal";
//        String command = "cp /opt/files/sftp_site/channels/citic/20170601/total_20170601.txt /home/wuser/tmp/channel/20170601/";
        File keyFile = new File("C:\\Users\\admin\\.ssh\\id_rsa");
        String keyFilePass = "123456";

        SshConnectDto sshConnectDto = new SshConnectDto();
        sshConnectDto.setServerIp(ip);
        sshConnectDto.setUserName(user);
        sshConnectDto.setKeyFile(keyFile);
        sshConnectDto.setKeyFilePass(keyFilePass);

//        SshUtils.execCommand(sshConnectDto, command, true);
//        SshUtils.exec(sshConnectDto, command);

        String localFile = "D:\\测试归档\\自动对账\\最新对账文件模板\\自动化测试对账文件\\平台000000\\20170601_000000_001.txt";
        String rtd = "/home/wuser/tmp/channel/20170601/";
        SshUtils.uploadFile(sshConnectDto, localFile, rtd);
    }
}
