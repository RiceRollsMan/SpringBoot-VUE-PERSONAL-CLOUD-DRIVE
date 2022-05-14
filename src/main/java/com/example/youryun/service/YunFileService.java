package com.example.youryun.service;

import org.springframework.stereotype.Service;

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
}
