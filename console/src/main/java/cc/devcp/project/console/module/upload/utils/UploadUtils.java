package cc.devcp.project.console.module.upload.utils;

import cn.hutool.extra.ssh.ChannelType;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2020/1/9 15:43
 */
@Slf4j
public class UploadUtils extends JschUtil {

    /**
     * 上传文件
     *
     * @param file        文件
     * @param fileName    文件名
     * @param packageName 子目录名
     * @param props       目标服务器属性
     * @return 相对路径
     * @throws IOException
     * @throws SftpException
     */
    public static String upload(MultipartFile file, String fileName, String packageName, UploadProps props) throws IOException, SftpException {
//        String classPath = PathUtil.instance();
//        log.info("-----classPath : {}", classPath);
//        String sshKeyFilePath = classPath + props.getKey();
//        log.info("sshKeyFilePath : {}", sshKeyFilePath);
        Session session = getSession(props.getHost(), props.getPort(), props.getUsername(), props.getKey(), (byte[]) null);
        ChannelSftp channelSftp = getSftp(session);
        existsDir(channelSftp, props.getPath() + packageName);
        channelSftp.put(file.getInputStream(), props.getPath() + packageName + "/" + fileName);
        if (channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        return packageName + "/" + fileName;
    }

    /**
     * 上传文件
     *
     * @param file        文件
     * @param fileName    文件名
     * @param packageName 子目录名
     * @param props       目标服务器属性
     * @return 相对路径
     * @throws IOException
     * @throws SftpException
     */
    public static String upload(File file, String fileName, String packageName, UploadProps props) throws IOException, SftpException {
//        String classPath = PathUtil.instance();
//        log.info("-----classPath : {}", classPath);
//        String sshKeyFilePath = classPath + props.getKey();
//        log.info("sshKeyFilePath : {}", sshKeyFilePath);
        Session session = getSession(props.getHost(), props.getPort(), props.getUsername(), props.getKey(), (byte[]) null);
        ChannelSftp channelSftp = getSftp(session);
        existsDir(channelSftp, props.getPath() + packageName);
        InputStream in = new FileInputStream(file);
        channelSftp.put(in, props.getPath() + packageName + "/" + fileName);
        if (channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        return packageName + "/" + fileName;
    }

    /**
     * 上传文件
     *
     * @param outputStream 流
     * @param fileName     文件名
     * @param packageName  子目录名
     * @param props        目标服务器属性
     * @return 相对路径
     * @throws IOException
     * @throws SftpException
     */
    public static String upload(ByteArrayOutputStream outputStream, String fileName, String packageName, UploadProps props) throws IOException, SftpException {
//        String classPath = PathUtil.instance();
//        log.info("-----classPath : {}", classPath);
//        String sshKeyFilePath = classPath + props.getKey();
//        log.info("sshKeyFilePath : {}", sshKeyFilePath);
        Session session = getSession(props.getHost(), props.getPort(), props.getUsername(), props.getKey(), (byte[]) null);
        ChannelSftp channelSftp = getSftp(session);
        existsDir(channelSftp, props.getPath() + packageName);
        ByteArrayInputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
        channelSftp.put(inStream, props.getPath() + packageName + "/" + fileName);
        if (channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        return packageName + "/" + fileName;
    }


    /**
     * 判断packageName是否存在
     *
     * @param channelSftp
     * @param dir
     * @return
     */
    private static boolean existsDir(ChannelSftp channelSftp, String dir) {
        try {
            channelSftp.cd(dir);
            return true;
        } catch (SftpException e1) {
            log.error("即将尝试创建文件夹:{}", dir);
            try {
                channelSftp.mkdir(dir);
                log.info("创建文件夹成功:{}", dir);
                return true;
            } catch (SftpException e2) {
                e2.printStackTrace();
                log.error("创建文件夹失败，原因：{}", e2.getMessage());
                return false;
            }
        }
    }

    /**
     * 打开SFTP连接
     *
     * @param session Session会话
     * @return ChannelSftp
     */
    public static ChannelSftp getSftp(Session session) {
        return (ChannelSftp) openChannel(session, ChannelType.SFTP);
    }

}
