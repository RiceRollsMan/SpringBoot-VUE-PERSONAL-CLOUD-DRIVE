package com.example.youryun.service;

import com.example.youryun.pojo.YunFile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface YunFileService {
    /*
    * 1.2022/5/14
    * 2.功能：用于上传单个文件
    * */
    //id不用写
    //username需要前台传
    //file_name需要前台传
    //file_path需要传
    //file_size需要传
    //file_type需要传
    //state不用传。添加的时候一定为1，即正常咯。
    //isDir不用传。能上传的都不是文件夹。So 默认为0.
    //add_data不用传
    //delete_data 默认为NULL嘛。添加的时候一定为NULL咯。
    public void addFile(String user_name,
                        String file_name,
                        String file_path,
                        String file_size,
                        String file_type);
    /*
    * 2022/5/15
    * 展示文件*/
    public List<YunFile> showFiles(String presentPath);
    /*
    * 2022/5/15
    * 通过id获取到文件目录进而对其进行下载*/
    public YunFile downloadFile(int id);//通过id拿到对于的数据库中的文件信息 file_path

    /*
    * 2022/5/16
    * 创建一个文件夹*/
    public void makeFolder(String user_name,
                           String file_name,
                           String file_path);//
    /*2022/5/17
    * 根据folderId拿到路径*/
    public String getFolderPath(int id);//根据folder的id拿到他的路径

    /*
    * 2022/5/17
    * 扔进回收站*/
    public void throwInBin(int id);//根据ID

    /*2022/5/17
    * 修改文件名*/
    public void changeFileName(int id,String newFileName,String file_type);//这里要根据后缀名去拼名字
}
