package com.bfchengnuo.ssmall.service.impl;

import com.bfchengnuo.ssmall.service.IFileService;
import com.bfchengnuo.ssmall.util.FTPUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by 冰封承諾Andy on 2018/7/12.
 */
@Service("fileService")
public class FileServiceImpl implements IFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        String filename = file.getOriginalFilename();
        String fileExtensionName = filename.substring(filename.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        LOGGER.info("开始上传文件，原始文件名：{}；上传路径：{}；新文件名：{}", filename, path, newFileName);

        File file1Dir = new File(path);
        if (!file1Dir.exists()) {
            file1Dir.setWritable(true);
            file1Dir.mkdirs();
        }
        File targetFle = new File(file1Dir, newFileName);
        try {
            file.transferTo(targetFle);
            // 上传到 FTP 服务器，成功后删除临时文件
            boolean flag = FTPUtil.uploadFile(Lists.newArrayList(targetFle));
            targetFle.delete();
            if (!flag) {
                return null;
            }
            return targetFle.getName();
        } catch (IOException e) {
            LOGGER.error("上传文件异常", e);
        }
        return null;
    }
}
