package com.bfchengnuo.ssmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 冰封承諾Andy on 2018/7/12.
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
