package com.example.youryun.controller;

import com.example.youryun.mapper.YunFileMapper;
import com.example.youryun.pojo.YunFile;
import com.example.youryun.service.Impls.YunFileServiceImpl;
import com.example.youryun.service.YunFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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
    * 注意：这里的分隔符全部采用 / 而不是 \\ 为了方便数据库对于路径的模糊查询
    * */
    @PostMapping("/toUploadFile")
    public void toUpLoadFile(@RequestParam("toUploadFile") MultipartFile uploadFile,
                             @RequestParam("toUploadUser") String username,
                             HttpServletRequest req){
            //服务器保存路径所有文件均保存在这
            String storagePath = "I:/myYunStoragePath/"+username+"/";
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
            /*与上面的区别，上面那个会保存. 这个只有文件后缀*/
//            String fileType=oldName.substring(oldName.lastIndexOf(".")+1,oldName.length());
            //newName通过字符串拼接的模式
            //UUID可以看成生成一个唯一的名字。
            String newName= UUID.randomUUID().toString()+ fileType;

            //这个时候就利用springboot的工具类在服务器上进行存储了。
            String filePath="";
            try{
                uploadFile.transferTo(new File(folder,newName));//transferto()方法，是springmvc封装的方法，把文件写入磁盘。
                filePath=storagePath+date + "/"+ newName;//文件绝对路径
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

    /*
    * 2022/5/15
    * 功能:文件展示*/
    @GetMapping("/toShowFiles")
    public ModelAndView toShowFiles(@RequestParam("parentPath") String parentPath,
                                    ModelAndView mv){
        YunFile[] yunFiles=null;
        yunFiles=yunFileService.showFiles(parentPath);
        for(YunFile yunFile:yunFiles){
            System.out.println(yunFile.toString());
        }
        mv.addObject("files",yunFiles);
        mv.setViewName("fileList");
        return mv;
    }
    /*
    * 2022/5/15
    * 功能:通过传入文件id下载文件
    * */
    @GetMapping("/toDownLoadFile")
    public void toDownLoadFile(@RequestParam("id") int id,
                               HttpServletResponse response){
        try {
            //通过文件的id获取到指定下载文件的对象
            YunFile yunFile= yunFileService.downloadFile(id);
            System.out.println(yunFile);
            // path是指想要下载的文件的路径
            File file = new File(yunFile.getFile_path());
            // 获取文件名
            String filename = yunFile.getFile_name();
            // 获取文件后缀名
            //String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称

            //没区别啊
            //response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(filename, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
