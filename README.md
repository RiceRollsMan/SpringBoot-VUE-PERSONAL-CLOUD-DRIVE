**个人使用的网盘**
功能1.文件上传
功能2.文件下载

文件展示逻辑
1.用户首次进入（登录）的时候，服务器会拿到用户的username，并且拼接成一串字符，作为默认的parentPath
like：I:/myYunStoragePath/{username}/*/   (其中*为日期格式，因为存储的时候，会按照存储日期去划分文件)
只能访问到一级目录哦哦哦哦哦哦哦哦哦！！！！！！
并且此时设置一个全局变量havaParent=false，即这是用户能看到的最高层的文件夹。
对应的sql语句：
因为用like的模糊查询试了好多次都没对，所以我直接采用
（不能用notlike）
（焯！）
因为虽然能够找到只是一级目录的，但是file_path仍然需要满足一些基本要求。。。例如是该用户的文件，。。。但是可不指这一个歧义，so别想用这个好了，，，，
NOT LIKE：select * from file where file_path not like 'I:/myYunStoragePath/ft/%(任意文件夹，就是时间嘛)/ %/%(包含有/的，就相当于与不是1级目录了嘛) ';
        具体含义从file表里找到file_path不像   
so，lets explore how to use LIKE
LIKE:

逻辑：
    一.用户登录动作完成后。
        1.设置presentPath=I:/myYunStoragePath/{username}/%/ (这个路径下面用户能够看到所有不同时间里自己上传的所有文件)。就是当前的路径。
        2.设置havaParent=false;并且前台的返回上一级按钮根据这个的boolean值去设置自己是否可点。
        3.设置parentPath[]=null; 也就是当前的路径的父路径是自己;做成一个数组行不行啊！！！！！
        4.前台会自动发起请求要求服务器去访问当前路径下的所有一级目录。
            即:select * from file where file_path like "当前路径下的所有一级目录"
        5.返回所有当前目录下的一级文件。
        6.通过yunFile中的isDir属性可以判断该file是否是文件夹，如果是文件夹的话那么就会展示一个文件夹头像，如果不是那就展示普通的头像。
        7.针对文件夹来说，如果双击就会进入该目录。如I:/myYunStoragePath/{username}/2022-5-10/demo1这个就是这个文件夹的路径。
        8.那么在双击进入文件夹后，把 parentPath[parentPath.length-1]=presentPath 就是把当前目录赋给父路径，用于退回操作。
        9.把haveParent=true，用于退回按钮的操作。
        10.进入路径后 把presentPath=presentPath+“file_name”+"/".相当于当前的路径
        11.然后再在数据库里找到当前路径下的一级文件并展示出来。
        12.如果双击的是非文件夹，那么直接根据id去数据库里找到该文件下载下来就完了。
        13.在执行退回按钮的时候。
            1.点击退回按钮，将presentPath=parentPath[]
        