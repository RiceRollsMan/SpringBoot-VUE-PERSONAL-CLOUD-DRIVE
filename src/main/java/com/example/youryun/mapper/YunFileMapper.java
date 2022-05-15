package com.example.youryun.mapper;

import com.example.youryun.pojo.YunFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface YunFileMapper {
    public void addFile(YunFile yunFile);//添加一个文件
    public List<YunFile> showFiles(String presentPath);//通过parent路径找到该路径下的一级文件，而且返回整个YunFile文件。
    public YunFile downloadFile(int id);//通过id拿到对于的数据库中的文件信息 file_path
}
