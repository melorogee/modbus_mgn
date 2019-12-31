package com.xwtec.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: RunningLake
 * @description: 文件上传工具类
 * @author: spj
 * @create: 2019-04-02 17:33
 **/
@Component
public class FtpUploadUtil {

    public static final int imageCutSize = 300;
    public static final String DIRSPLIT = "/";
    private static final Logger log = LoggerFactory.getLogger(FtpUploadUtil.class);
//    @Value("${ftp.username}")
    private String userName;
//    @Value("${ftp.password}")
    private String passWord;
//    @Value("${ftp.host}")
    private String ip;
//    @Value("${ftp.port}")
    private int port;
//    @Value("${ftp.fileReadIp}")
    private String fileReadIp;
//    @Value("${ftp.filepath}")
    private String CURRENT_DIR;     // 文件存放的目录
    // 下载的文件目录
    private String DOWNLOAD_DIR;
    // ftp客户端
    private FTPClient ftpClient = new FTPClient();

    /**
     * 带点的
     *
     * @param fileName
     * @return
     */
    public static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    /**
     * 不带点
     *
     * @param fileName
     * @return
     */
    public static String getNoPointExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }

    /**
     * 功能：根据当前时间获取文件目录
     *
     * @return String
     */
    public static String getDateDir(Date dateParam) {
        Calendar cal = Calendar.getInstance();
        if (null != dateParam) {
            cal.setTime(dateParam);
        }
        int currentYear = cal.get(Calendar.YEAR);
        int currentMouth = cal.get(Calendar.MONTH) + 1;
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        return currentYear + FtpUploadUtil.DIRSPLIT + currentMouth + FtpUploadUtil.DIRSPLIT + currentDay;
    }

    public String getCURRENT_DIR() {
        return CURRENT_DIR;
    }

    public void setCURRENT_DIR(String cURRENTDIR) {
        CURRENT_DIR = cURRENTDIR;
    }

    public String getFileReadIp() {
        return fileReadIp;
    }

    public String getIp() {
        return ip;
    }

    /**
     * 功能：上传文件附件到文件服务器
     *
     * @param buffIn:上传文件流
     * @param fileName：保存文件名称
     * @param fileUploadPath：文件夹
     * @param needDelete：是否同时删除
     * @return
     * @throws IOException
     */
    public boolean uploadToFtp(InputStream buffIn, String fileName, String fileUploadPath, boolean needDelete, String fileType, boolean iscompress)
            throws FTPConnectionClosedException, IOException, Exception {
        log.info("uploadToFtp INFO: uploadToFtp() start ......");
        boolean returnValue = false;
        // 上传文件
        try {
            log.info("uploadToFtp INFO: create connect to server start ......");
            // 建立连接
            connectToServer();
            String filePath = CURRENT_DIR + fileUploadPath;

            // 判断是否存在该文件夹
            if (!existDirectory(filePath)) {
                createDirectory(filePath);
            }
            // 指定上传路径
            ftpClient.changeWorkingDirectory(filePath);
            // 获取文件
            log.info("uploadToFtp INFO: create connect to server end ......");
            // 设置传输二进制文件
            setFileType(FTP.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new IOException("failed to connect to the FTP Server:" + ip);
            }
            ftpClient.enterLocalPassiveMode();
            if (StringUtils.isNotBlank(fileType)) {
                if (fileType.equals("isPicture") && iscompress) {
                    // 压缩图片并上传
                    // 压缩基数  从前台获取，如果未传入，则默认768
                    // 压缩限制(宽/高)比例  一般用1
                    buffIn = this.compressPic(buffIn, fileName, 768, 1d);
                }
            }
            // 上传文件到ftp
            log.info("uploadToFtp INFO: upload file start ......");
            returnValue = ftpClient.storeFile(fileName, buffIn);
            log.info("uploadToFtp INFO: upload file end ......");
            if (needDelete) {
                ftpClient.deleteFile(fileName);
            }
            // 输出操作结果信息
            if (returnValue) {
                log.info("uploadToFtp INFO: upload file  to ftp : succeed!");
            } else {
                log.info("uploadToFtp INFO: upload file  to ftp : failed!");
            }
            buffIn.close();
            // 关闭连接
            closeConnect();
        } catch (FTPConnectionClosedException e) {
            log.error("ftp连接被关闭！", e);
            throw e;
        } catch (Exception e) {
            returnValue = false;
            log.error("ERR : upload file  to ftp : failed! ", e);
            throw e;
        } finally {
            try {
                if (buffIn != null) {
                    buffIn.close();
                }
            } catch (Exception e) {
                log.error("ftp关闭输入流时失败！", e);
            }
            if (ftpClient.isConnected()) {
                closeConnect();
            }
            log.info("uploadToFtp INFO: uploadToFtp() end ......");
        }
        return returnValue;
    }

    public void download(String fileRealName, String path, HttpServletResponse response) {
        try {
            if (org.apache.commons.lang.StringUtils.isNotBlank(path)) {
                File file = new File(path);
                // 取得文件名。
                String fileName = file.getName();
                // 以流的形式下载文件。
                InputStream fis = this.downloadFile(fileName);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                String uncod = URLDecoder.decode(fileName, "UTF-8");
                fileName = new String(uncod.getBytes("UTF-8"), "iso-8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(fileRealName)));
                // 设置response的Header
                response.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(
                        response.getOutputStream());
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            }
            // path是指欲下载的文件的路径。
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 功能：根据文件名称，下载文件流
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public InputStream downloadFile(String fileName)
            throws IOException {
        InputStream in = null;
        try {

            // 建立连接
            connectToServer();
            ftpClient.enterLocalPassiveMode();
            // 设置传输二进制文件
            setFileType(FTP.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new IOException("failed to connect to the FTP Server:" + ip);
            }
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String fileUploadPath = CURRENT_DIR;
            if (FileUtils.isPicture(prefix)) {
                fileUploadPath += "/pic";
            } else if (FileUtils.isVideo(prefix)) {
                fileUploadPath += "/video";
            } else if (FileUtils.isAudio(prefix)) {
                fileUploadPath += "/audio";
            } else if (FileUtils.isOffice(prefix)) {
                fileUploadPath += "/office";
            } else {
                fileUploadPath += "/file";
            }
            ftpClient.changeWorkingDirectory("pic");

            // ftp文件获取文件
            in = ftpClient.retrieveFileStream(fileName);

        } catch (FTPConnectionClosedException e) {
            log.error("ftp连接被关闭！", e);
            throw e;
        } catch (Exception e) {
            log.error("ERR : upload file " + fileName + " from ftp : failed!", e);
        }
        return in;
    }

    /**
     * 设置传输文件的类型[文本文件或者二进制文件]
     *
     * @param fileType --BINARY_FILE_TYPE、ASCII_FILE_TYPE
     */
    private void setFileType(int fileType) {
        try {
            ftpClient.setFileType(fileType);
        } catch (Exception e) {
            log.error("ftp设置传输文件的类型时失败！", e);
        }
    }

    /**
     * 功能：关闭连接
     */
    public void closeConnect() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            log.error("ftp连接关闭失败！", e);
        }
    }

    /**
     * 连接到ftp服务器
     */
    private void connectToServer() throws FTPConnectionClosedException, Exception {
        if (!ftpClient.isConnected()) {
            int reply;
            try {
                ftpClient = new FTPClient();
                ftpClient.connect(ip, port);
                ftpClient.login(userName, passWord);
                reply = ftpClient.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    log.info("connectToServer FTP server refused connection.");
                }

            } catch (FTPConnectionClosedException ex) {
                log.error("服务器:IP：" + ip + "没有连接数！there are too many connected users,please try later", ex);
                throw ex;
            } catch (Exception e) {
                log.error("登录ftp服务器【" + ip + "】失败", e);
                throw e;
            }
        }
    }

    // Check the path is exist; exist return true, else false.
    public boolean existDirectory(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isDirectory()
                    && ftpFile.getName().equalsIgnoreCase(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 创建FTP文件夹目录
     *
     * @param pathName
     * @return
     * @throws IOException
     */
    public boolean createDirectory(String pathName) throws IOException {
        boolean isSuccess = false;
        try {
            isSuccess = ftpClient.makeDirectory(pathName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 等比例压缩算法：
     * 算法思想：根据压缩基数和压缩比来压缩原图，生产一张图片效果最接近原图的缩略图
     *
     * @param comBase 压缩基数
     * @param scale   压缩限制(宽/高)比例  一般用1：
     *                当scale>=1,缩略图height=comBase,width按原图宽高比例;若scale<1,缩略图width=comBase,height按原图宽高比例
     * @throws Exception
     * @author zhuchen
     * @createTime 2019-4-9
     */
    public InputStream compressPic(InputStream buffIn, String fileName, double comBase,
                                   double scale) throws IOException {
        log.info("       ......  压缩图片开始：" + DateUtils.now());
        InputStream inputStream = null;

        log.info(JSONObject.toJSONString(buffIn));
        Image src = ImageIO.read(buffIn);
        int srcHeight = src.getHeight(null);
        int srcWidth = src.getWidth(null);
        int deskHeight = 0;// 缩略图高
        int deskWidth = 0;// 缩略图宽
        double srcScale = (double) srcHeight / srcWidth;
        /**缩略图宽高算法*/
        if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
            if (srcScale >= scale || 1 / srcScale > scale) {
                if (srcScale >= scale) {
                    deskHeight = (int) comBase;
                    deskWidth = srcWidth * deskHeight / srcHeight;
                } else {
                    deskWidth = (int) comBase;
                    deskHeight = srcHeight * deskWidth / srcWidth;
                }
            } else {
                if ((double) srcHeight > comBase) {
                    deskHeight = (int) comBase;
                    deskWidth = srcWidth * deskHeight / srcHeight;
                } else {
                    deskWidth = (int) comBase;
                    deskHeight = srcHeight * deskWidth / srcWidth;
                }
            }
        } else {
            deskHeight = srcHeight;
            deskWidth = srcWidth;
        }
        BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
        tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); //绘制缩小后的图
        // 获取文件后缀名
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        // BufferedImage 转换为 InputStream
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        ImageIO.write(tag, prefix, output);
        byte[] buff = output.toByteArray();
        inputStream = new ByteArrayInputStream(buff);
        log.info("       ......  压缩图片结束：" + DateUtils.now());
        return inputStream;
    }
}
