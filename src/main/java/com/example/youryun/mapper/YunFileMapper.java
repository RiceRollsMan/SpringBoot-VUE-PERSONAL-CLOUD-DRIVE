package com.example.youryun.mapper;

import com.example.youryun.pojo.YunFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface YunFileMapper {
    public void addFile(YunFile yunFile);//添加一个文件
}
