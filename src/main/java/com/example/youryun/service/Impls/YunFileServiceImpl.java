package com.example.youryun.service.Impls;

import com.example.youryun.mapper.YunFileMapper;
import com.example.youryun.pojo.YunFile;
import com.example.youryun.service.YunFileService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class YunFileServiceImpl implements YunFileService {

    @Autowired
    private YunFileMapper yunFileMapper;
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
                        String file_type) {
        //某些参数的定义
        Integer id = 0;//默认为0嘛。具体原因请看pojo层。
        Integer state = 1;//上传文件的时候，文件状态默认为1(正常)。
        Integer isDir=0;//能上传的都不是文件夹。So 默认为0.
        Date add_data=new Date();//上传文件的时候，上传时间默认为当前时间
        //YunFile对象封装
        YunFile yunFile=new YunFile(id,//id默认为0，数据库实现主键自增长
                user_name,
                file_name,
                file_path,
                file_size,
                file_type,
                state,
                isDir,
                add_data
                );
        //调用mapper进行数据库的insert
        yunFileMapper.addFile(yunFile);
    }

    /*
    *2022/5/15
    * 展示文件*/
    @Override
    public List<YunFile> showFiles(String presentPath) {
        List<YunFile> allYunFiles =  yunFileMapper.showFiles(presentPath);//此时返回的是该路径下所有的文件，不仅限于一级
        //此时还要进行的是筛选就是筛选出只有一级目录的。
        /*
        * 1.进行分割 把presentPath后面的部分提出来*/
        List<YunFile> yunFiles = new ArrayList<>();
        for(YunFile yunFile:allYunFiles){
            if(yunFile.getFile_path().split("/").length-presentPath.split("/").length==1){//这个就是筛选到了满足一级条件的对象
                yunFiles.add(yunFile);
            }
        }
        return yunFiles;
    }
    /*
    * 2022/5/15
    * 下载文件
    * */
    @Override
    public YunFile downloadFile(int id) {
        return yunFileMapper.downloadFile(id);
    }
}
