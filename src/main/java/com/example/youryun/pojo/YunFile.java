package com.example.youryun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
/*
* 文件类的pojo
* */
public class YunFile {
    private Integer id;//对应id（自增主键） PK 我们只需要将主键设置为null，0或者不设置该字段，数据库就会为我们自动生成一个主键值。so在插入式默认0就行了
    private String user_name;//文件所有人
    private String file_name;//文件名称
    private String file_path;//文件路径
    private String file_size;//文件大小
    private String file_type;//文件类型，例如txt
    private Integer state;//文件状态 1：正常 0：已删除
    private Integer isDir;//文件是否为文件夹 1：是文件夹 0：不是文件夹
    private Date add_data;//文件被添加日期，应该在插入式默认为new Date();
    private Date delete_data;//文件被删除日期，应该在删除文件时默认为new Date();
    /*
    *非全参构造器1:用于addFile()使用
    * */
    public YunFile(Integer id,
                   String user_name,
                   String file_name,
                   String file_path,
                   String file_size,
                   String file_type,
                   Integer state,
                   Integer isDir,
                   Date add_data) {
            this.id=id;
            this.user_name=user_name;
            this.file_name=file_name;
            this.file_path=file_path;
            this.file_size=file_size;
            this.file_type=file_type;
            this.state=state;
            this.isDir=isDir;
            this.add_data=add_data;

    }
}
