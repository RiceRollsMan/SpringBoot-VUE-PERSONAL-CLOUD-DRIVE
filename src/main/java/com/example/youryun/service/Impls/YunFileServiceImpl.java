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
        for(YunFile yunFile:showNormalFiles(presentPath))
            yunFiles.add(yunFile);//先加正常的
        for(YunFile yunFile:showDeletedFiles(presentPath))
            yunFiles.add(yunFile);//再加已经被删了的，ok不
        return yunFiles;
    }
    @Override
    public List<YunFile> showNormalFiles(String presentPath) {
        List<YunFile> allYunFiles =  yunFileMapper.showNormalFiles(presentPath);//此时返回的是该路径下所有的文件，不仅限于一级
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
    @Override
    public List<YunFile> showDeletedFiles(String presentPath) {
        List<YunFile> allYunFiles =  yunFileMapper.showDeletedFiles(presentPath);//此时返回的是该路径下所有的文件，不仅限于一级
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


    /*
     * 2022/5/16
     *
     * 1.不需要id 默认为0
     * 2.需要user_name 即文件所有人
     * 3.需要file_name，即 文件夹名字
     * 4.需要file_path
     * 5.不需要file_size
     * 6.不需要file_type 默认为null
     * 7.不需要state 1
     * 8.不需要isDir 默认为1
     * 9.不需要add_data
     * 创建一个文件夹*/

    public void makeFolder(String user_name,
                           String file_name,
                           String file_path)
    {
        //某些参数的定义
        Integer id = 0;//默认为0嘛。具体原因请看pojo层。
        Integer state = 1;//上传文件的时候，文件状态默认为1(正常)。
        Integer isDir=1;//能上传的都不是文件夹。So 默认为1.
        Date add_data=new Date();//上传文件的时候，上传时间默认为当前时间
        String file_type="";
        String file_size=""+0;
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
        yunFileMapper.makeFolder(yunFile);
    }
    /*2022/5/16
    * 根据folderId拿到路径*/
    public String getFolderPath(int id){
        return yunFileMapper.getFolderPath(id);
    }
    /*2022/5/17
    * 删除文件*/
    public void throwInBin(int id){
        yunFileMapper.throwInBin(yunFileMapper.getFolderPath(id));
    }

    /*2022/5/17
     * 修改文件名*/
    public void changeFileName(int id,String newFileName,String file_type) {
        newFileName=newFileName+file_type;//拼接后缀
        yunFileMapper.changeFileName(id,newFileName);
    }
}
