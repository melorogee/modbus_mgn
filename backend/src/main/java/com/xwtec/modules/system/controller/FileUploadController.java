package com.xwtec.modules.system.controller;

import com.xwtec.common.api.vo.Result;
import com.xwtec.common.util.FileUtils;
import com.xwtec.common.util.FtpUploadUtil;
import com.xwtec.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: RunningLake
 * @description: 文件上传controller
 * @author: spj
 * @create: 2019-04-02 17:45
 **/
@RestController
@Slf4j
public class FileUploadController {

    @Autowired
    private FtpUploadUtil ftpUploadUtil;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/file/fileUpload")
    @ApiOperation(value = "文件上传", notes = "文件上传", produces = "application/json")
    public Result<Map<String, String>> fileUpload(MultipartFile file, HttpServletRequest request) {
        log.info("FileUploadController - fileUpload() : start ......");
        Result<Map<String, String>> result = new Result<>();
        String fileType = null;
        if (file.isEmpty()) {
            result.setSuccess(false);
            result.setMessage("系统异常，请稍后再试！");
            return result;
        }
        String fileName = file.getOriginalFilename();
        // 获取文件后缀名
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileUploadPath = "";
        // 判断文件类型
        try {
            if (FileUtils.isPicture(prefix)) {
                fileType = "isPicture";
                fileUploadPath = File.separator + "pic";
            } else if (FileUtils.isVideo(prefix)) {
                fileType = "isVideo";
                fileUploadPath = File.separator + "video";
            } else if (FileUtils.isAudio(prefix)) {
                fileType = "isAudio";
                fileUploadPath = File.separator + "audio";
            } else if (FileUtils.isOffice(prefix)) {
                fileType = "isOffice";
                fileUploadPath = File.separator + "office";
            } else {
                fileType = "isFile";
                fileUploadPath = File.separator + "file";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fileUploadPath.contains("\\")) {
            fileUploadPath = fileUploadPath.replace("\\", "/");
        }
        // 生成新的文件名称
        String newFileName = UUIDUtil.getUUID() + "." + prefix;
        log.info("FileUploadController - fileUpload() : fileName = " + fileName + ", newFileName = " + newFileName);
        try {
            log.info("FileUploadController - fileUpload() : upload file start ......");
            boolean flag = false;
            flag = ftpUploadUtil.uploadToFtp(file.getInputStream(), newFileName, fileUploadPath, false, fileType, true);
            log.info("FileUploadController - fileUpload() : upload file end ......");
            String ip = ftpUploadUtil.getFileReadIp();
            String filePath = "http://" + ip + "/static" + fileUploadPath + "/" + newFileName;
            log.info("FileUploadController - fileUpload() : filePath = " + filePath);
            result.success("操作成功");
            // 返回参数
            Map<String, String> params = new HashMap<>();
            // 获取上传文件的大小
            Long size = file.getSize();
            if (FileUtils.isPicture(prefix)) {
                BufferedImage image = ImageIO.read(file.getInputStream());
                int srcWidth = image .getWidth();      // 源图宽度
                int srcHeight = image .getHeight();    // 源图高度
                params.put("picWidth", String.valueOf(srcWidth));
                params.put("picHeight", String.valueOf(srcHeight));
            }
            params.put("fileSize", String.format("%.2f", (double) size / 1048576));
            params.put("fileName", fileName);
            params.put("filePath", filePath);
            //index不为空
            if (StringUtils.isNotBlank(request.getParameter("index"))){
                params.put("index", request.getParameter("index"));
            }
            result.setResult(params);
            return result;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("系统异常，请稍后再试！");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("系统异常，请稍后再试！");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("系统异常，请稍后再试！");
            return result;
        }
    }

}
