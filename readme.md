# 启动步骤

- 解压baiyin-exam.zip

- 修改mysql配置文件【mysql/my.ini】中的配置项【basedir】和【datadir】成实际地址

- 执行【bin/start-mysql.bat】启动数据库

- 以上两步可以自行安装mysql，

- 用root/root登录mysql【mysql/bin/mysql.exe -uroot -proot】

- 导入表结构【source init-database.sql】

- 修改系统配置文件【app.properties】中的配置项成正式配置

- 启动系统【bin/start.bat】


# 注意事项

- 系统自带64位1.8版本的jre，如果操作系统是32位则无法运行成功，如需运行，则需要自行下载32位1.8版本的jre
覆盖掉jre目录即可

- 浏览器要求chrome、firefox、ie11+、edge

- 不要破坏系统内各目录（log目录除外，如日志过大可酌情删除）