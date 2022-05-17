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
    public List<YunFile> showNormalFiles(String presentPath);//state=1的
    public List<YunFile> showDeletedFiles(String presentPath);//state=0的
    public YunFile downloadFile(int id);//通过id拿到对于的数据库中的文件信息 file_path
    public void makeFolder(YunFile yunFile);//创建一个文件夹
    public String getFolderPath(int id);//根据folder的id拿到他的路径
    public void throwInBin(String filePath);//根据id,拿到他的路径，并且这个路径下的所有都set=0.当然用like咯。而且要用连表哦哦哦哦哦哦哦（这里为了让sql语句更加方便，所以搞个中间商）
    public void changeFileName(int id,String newFileName);//根据id的去修改文件名字，sure后缀不能改变。所以要在survice层做一点操作。
    public void restoreFile(int id);//就是恢复嘛！
}
