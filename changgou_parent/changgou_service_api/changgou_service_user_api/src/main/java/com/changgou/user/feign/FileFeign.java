package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.file.util.UploadConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file", configuration = UploadConfig.class)
@RequestMapping("/file")
public interface FileFeign {

    @PostMapping(value="/upload",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Result uploadFile(@RequestPart(value = "file")MultipartFile file);
}
