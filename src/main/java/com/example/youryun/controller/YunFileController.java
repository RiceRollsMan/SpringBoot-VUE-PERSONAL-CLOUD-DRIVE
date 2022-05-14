package com.example.youryun.controller;

import com.example.youryun.service.YunFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
public class YunFileController {
    /*
    * 规定日期格式
    * "yyyy-MM-dd HH:mm"
    * 2022/5/22-22:35
    * */
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    /*
    * */
    @Autowired
    private YunFileService yunFileService;
    /*
    * 2022/5/14
    * 上传文件
    * 请求方法 post
    * 请求路径 /toUpLoadFile
    * 参数1 upLoadFile-->upLoadFile
    * 参数2 upLoadUser-->username
    * */
    @PostMapping("/toUploadFile")
    public void toUpLoadFile(@RequestParam("toUploadFile") MultipartFile uploadFile,
                             @RequestParam("toUploadUser") String username,
                             HttpServletRequest req){
            //服务器保存路径
            String storagePath = "I:\\myYunStoragePath\\"+username+"\\";
            //这是保存到服务器的相对路径，可以实现在线浏览
            //String realPath = req.getSession().getServletContext().getRealPath("\\upload\\");

            //根据日期去命名存储文件夹
            String date = sdf1.format(new Date());
            File folder = new File(storagePath + date);

            //如果没有当日文件夹，就创建一个
            if(!folder.isDirectory()){
                folder.mkdirs();
            }
            //oldName获取到被上传文件的名字
            String oldName=uploadFile.getOriginalFilename();
            //通过oldName去获取到该文件的类型。
            String fileType=oldName.substring(oldName.lastIndexOf("."),oldName.length());
            //newName通过字符串拼接的模式
            //UUID可以看成生成一个唯一的名字。
            String newName= UUID.randomUUID().toString()+ fileType;

            //这个时候就利用springboot的工具类在服务器上进行存储了。
            String filePath="";
            try{
                uploadFile.transferTo(new File(folder,newName));//transferto()方法，是springmvc封装的方法，把文件写入磁盘。
                filePath=storagePath+date + "\\"+ newName;//文件绝对路径
                System.out.println(filePath);
            }
            catch (IOException e){
            }
            //调用service
            String user_name=username;
            String file_name=oldName;
            String file_path=filePath;
            String file_size=""+uploadFile.getSize();
            String file_type=fileType;
            yunFileService.addFile(user_name,file_name,file_path,file_size,file_type);
            //这里会报错，因为暂时没有return值，且前面是form需要跳转哦。
    }
}
