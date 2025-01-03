package com.example.qiniuoss;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * @author freeok
 */
public class QiniuOssService {

    @Autowired
    private Auth auth;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private QiniuOssProperties qiniuOssProperties;

    public String upload(File file) {
        return uploadFile(file);
    }

    public String upload(byte[] data) {
        return uploadFile(data);
    }

    private String uploadFile(Object o) {
        try {
            Response response;
            if (o instanceof File) {
                File file = (File) o;
                // 默认不指定key的情况下，以文件内容的hash值作为文件名
                response = uploadManager.put(file, null, auth.uploadToken(qiniuOssProperties.getBucket()));
            } else {
                byte[] data = Convert.toPrimitiveByteArray(o);
                response = uploadManager.put(data, null, auth.uploadToken(qiniuOssProperties.getBucket()));
            }
            // 解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            return qiniuOssProperties.getDomain() + putRet.key;

        } catch (QiniuException e) {
            System.err.println("七牛云上传文件异常：" + e.getMessage());
        }

        return null;
    }

}
