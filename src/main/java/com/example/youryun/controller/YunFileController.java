package com.example.youryun.controller;

import com.example.youryun.mapper.YunFileMapper;
import com.example.youryun.pojo.YunFile;
import com.example.youryun.service.Impls.YunFileServiceImpl;
import com.example.youryun.service.YunFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class YunFileController {
    /*
     * */
    @Autowired
    private YunFileService yunFileService;
    /*
    * 规定日期格式
    * "yyyy-MM-dd HH:mm"
    * 2022/5/22-22:35
    * */
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//    private String userRootPath="I:/myYunStoragePath/"+"ft"+"/";
//    private String presentPath="I:/myYunStoragePath/"+"ft"+"/";
//    private boolean havaParentPath=false;
//    private String parentPath=userRootPath;//默认相同，如果相同则haveParentPath=false，如果不同就是true。
      private String username="ft";

    /*
    * 2022/5/14
    * 上传文件
    * 请求方法 post
    * 请求路径 /toUpLoadFile
    * 参数1 upLoadFile-->upLoadFile
    * 参数2 upLoadUser-->username
    * 注意：这里的分隔符全部采用 / 而不是 \\ 为了方便数据库对于路径的模糊查询
    * */
//    @ResponseBody
//    @PostMapping("/toUploadFile")
//    public void toUpLoadFile(@RequestParam("toUploadFile") MultipartFile uploadFile,
//                             @RequestParam("toUploadUser") String username,
//                             HttpServletRequest req){
//            //服务器保存路径所有文件均保存在这
//            String storagePath = "I:/myYunStoragePath/"+"ft"+"/";
//            //这是保存到服务器的相对路径，可以实现在线浏览
//            //String realPath = req.getSession().getServletContext().getRealPath("\\upload\\");
//
//            //根据日期去命名存储文件夹
////            String date = sdf1.format(new Date());
////            File folder = new File(storagePath + date);
//
//            //oldName获取到被上传文件的名字
//            String oldName=uploadFile.getOriginalFilename();
//            //通过oldName去获取到该文件的类型。
//            String fileType=oldName.substring(oldName.lastIndexOf("."),oldName.length());
//            /*与上面的区别，上面那个会保存. 这个只有文件后缀*/
////            String fileType=oldName.substring(oldName.lastIndexOf(".")+1,oldName.length());
//            //newName通过字符串拼接的模式
//            //UUID可以看成生成一个唯一的名字。
//            String newName= UUID.randomUUID().toString()+ fileType;
//
//            //这个时候就利用springboot的工具类在服务器上进行存储了。
//            String filePath="";
//            try{
//                uploadFile.transferTo(new File(storagePath,newName));//transferto()方法，是springmvc封装的方法，把文件写入磁盘。
//                filePath=storagePath + newName;//文件绝对路径
//                System.out.println(filePath);
//            }
//            catch (IOException e){
//            }
//            //调用service
//            String user_name=username;
//            String file_name=oldName;
//            String file_path=filePath;
//            String file_size=""+uploadFile.getSize();
//            String file_type=fileType;
//            yunFileService.addFile(user_name,file_name,file_path,file_size,file_type);
//            //这里会报错，因为暂时没有return值，且前面是form需要跳转哦。
//    }

    /*vue版文件上传*/
    @ResponseBody
    @PostMapping("/toUploadFile")
    public List<YunFile> toUpLoadFile(@RequestBody @RequestParam("uploadFile") MultipartFile uploadFile,
                             @RequestBody @RequestParam("presentPath") String presentPath,
                             HttpServletRequest req){
        //服务器保存路径所有文件均保存在这
        System.out.println(presentPath);
        String storagePath = presentPath;
        //这是保存到服务器的相对路径，可以实现在线浏览
        //String realPath = req.getSession().getServletContext().getRealPath("\\upload\\");

        //根据日期去命名存储文件夹
//            String date = sdf1.format(new Date());
//            File folder = new File(storagePath + date);

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
            uploadFile.transferTo(new File(storagePath,newName));//transferto()方法，是springmvc封装的方法，把文件写入磁盘。
            filePath=storagePath + newName;//文件绝对路径
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
        return toShowFiles(presentPath);
    }

    /*
    * 2022/5/15
    * 功能:通过传入文件id下载文件
    * */
    /*后台功能版
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
    */

    /*整合VUE版*/
    @ResponseBody
    @GetMapping("/toDownLoadFile")
    public void toDownLoadFile(@RequestBody @RequestParam("id") int id,
                               HttpServletResponse response){
        System.out.println(response);
        try {
            //通过文件的id获取到指定下载文件的对象
            System.out.println("1");
            YunFile yunFile= yunFileService.downloadFile(id);
           // System.out.println(yunFile);
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

            System.out.println("2");

            // 清空response
//            response.reset();
//            // 设置response的Header
//            response.setCharacterEncoding("UTF-8");
//            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
//            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
//            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
//
//            //没区别啊
//            //response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
//            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
//            // 告知浏览器文件的大小
//            response.addHeader("Content-Length", "" + file.length());
//            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
//            System.out.println(response);
//           // HttpServletResponse res=response;
//            response.setContentType("application/octet-stream");
//            outputStream.write(buffer);
//            outputStream.flush();
//            System.out.println("3");
//            //return res;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(4);
        //return null;

    }

    /*
     * 2022/5/15
     * 功能:文件展示*/
    /*这是后端功能版本*/
    /*
    @GetMapping("/toShowFiles")
    public ModelAndView toShowFiles(@RequestParam("presentPath") String presentPath,
                                    ModelAndView mv){
        List<YunFile> yunFiles = new ArrayList<>();
        yunFiles=yunFileService.showFiles(presentPath);
        mv.addObject("files",yunFiles);
        mv.setViewName("fileList");
        return mv;
    }
    */

    /*这是整合VUE版*/
   /* @ResponseBody
    @GetMapping("/toShowFiles")

    public List<YunFile> toShowFiles(@RequestBody @RequestParam("presentPath") String presentPath){
        List<YunFile> yunFiles = new ArrayList<>();
        yunFiles=yunFileService.showFiles(presentPath);
        for(YunFile yunFile:yunFiles){
            //yunFiles.add(yunFile);
            System.out.println(yunFile.toString());
        }
        return yunFiles;
    }
    */
    @ResponseBody
    @GetMapping("/toShowFiles")
    public List<YunFile> toShowFiles(@RequestBody @RequestParam("presentPath") String presentPath){
        System.out.println(presentPath);
        List<YunFile> yunFiles = new ArrayList<>();
        yunFiles=yunFileService.showFiles(presentPath);
        for(YunFile yunFile:yunFiles){
            //yunFiles.add(yunFile);
            System.out.println(yunFile.toString());
        }
        return yunFiles;
    }
    /*与上配套的*/
    @ResponseBody
    @GetMapping("/toGetFolderById")
    public String getFolderPathById(@RequestBody @RequestParam("folderId") int folderId){
        return yunFileService.getFolderPath(folderId)+'/';//注意这里添加‘/’哦哦哦哦哦哦哦哦哦，逻辑搞通就好啦！
    }

    /*
     * 2022/5/16
     * 创建folder*/
    @ResponseBody
    @GetMapping("/toMakeAFolder")
    public List<YunFile> toMakeFolder(@RequestParam("newFolderName")String newFolderName,
                                      @RequestParam("presentPath")String presentPath){/*注意，这里前端还没改哦*/
        String storagePath=presentPath;//当前位置
        String oldName=newFolderName;//文件夹的名字
        String newName= UUID.randomUUID().toString();

        File folder = new File(storagePath+newName);
        System.out.println(folder.getName());
        folder.mkdirs();

        //调用service
        String user_name=username;
        String file_name=oldName;
        String file_path=storagePath+newName;
        yunFileService.makeFolder(user_name,file_name,file_path);
        return toShowFiles(presentPath);
    }

    /*
     * 2022/5/16*/
    @ResponseBody
    @GetMapping("/toThrowInBin")
    public List<YunFile> toThrowInBin(@RequestParam("id")int id,
                                      @RequestParam("presentPath")String presentPath){
        yunFileService.throwInBin(id);
        return toShowFiles(presentPath);
    }

    /*
    * 2022/5/17
    * 修改文件名*/
    @ResponseBody
    @GetMapping("/toChangeFileName")
    public List<YunFile> toChangeFileName(@RequestParam("id") int id,
                                          @RequestParam("newFileName") String newFileName,
                                          @RequestParam("fileType") String fileType,
                                          @RequestParam("presentPath")String presentPath){
        yunFileService.changeFileName(id,newFileName,fileType);
        System.out.println("ja");
        return toShowFiles(presentPath);
    }


    /*
    * 2022/5/15
    * vueTest*/
//    @ResponseBody
//    @GetMapping("/vueTest")
//    public String vueTest(@RequestBody @RequestParam("key")String test){
//        return test+"OK";
//    }




}
