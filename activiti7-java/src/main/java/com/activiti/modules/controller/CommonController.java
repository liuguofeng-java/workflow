package com.activiti.modules.controller;

import com.activiti.utils.R;
import com.activiti.utils.constant.Constants;
import com.activiti.utils.exception.AException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用请求
 *
 * @author liuguofeng
 * @date 2023/12/12 14:24
 **/
@RestController
@RequestMapping("/common")
public class CommonController {


    @Value("${system.uploadPath}")
    private String uploadPath;

    /**
     * 简单文件上传
     */
    @PostMapping("/upload")
    public R<Map<String, Object>> uploadFile(MultipartFile file) {
        //获取项目访问路径
        if (file.isEmpty()) return null;
        //获取文件名
        String fileName = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))
                + file.getOriginalFilename();
        File dest = new File(uploadPath + File.separator + fileName);
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            //保存文件
            file.transferTo(dest);
            //返回图片访问路径
            Map<String, Object> map = new HashMap<>();
            map.put("name", fileName);
            map.put("url", "/" + Constants.RESOURCE_PREFIX + "/" + fileName);
            return R.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AException("保存失败:" + e.getMessage());
        }

    }
}
