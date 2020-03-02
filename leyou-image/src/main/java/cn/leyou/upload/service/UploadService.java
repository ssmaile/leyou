package cn.leyou.upload.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    private static final List<String> CONTENT_TYPE = Arrays.asList("image/jpeg","image/gif");
    private static final Logger log= LoggerFactory.getLogger(UploadService.class);

    public String uploadImage(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String last = StringUtils.substringAfterLast(originalFilename, ".");
            //校验文件类型
            if(!CONTENT_TYPE.contains(last)){
                log.info("文件类型不合法:{}",originalFilename);
                return null;
            }
            //校验文件是否为图片
            BufferedImage read = ImageIO.read(file.getInputStream());
            if(read==null){
                log.info("文件内容不合法{}",originalFilename);
            }

            //保存到服务器
            file.transferTo(new File("E:\\Java_tools\\image"+originalFilename));
            return "file:///E:/Java_tools/image/"+originalFilename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
