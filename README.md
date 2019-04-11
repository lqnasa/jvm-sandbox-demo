# jvm-sandbox-demo
阿里巴巴正式开源自研动态非侵入AOP解决方案：JVM-Sandbox  学习demo


### JVM-SANDBOX用户手册
```
https://github.com/alibaba/jvm-sandbox/wiki/USER-GUIDE

```


### 使用手册
```
查看class类下对应的方法
./sandbox.sh -p 18982 -d "debug-watch/watch?class=com.ruijie.cvm.task.service.impl.ThriftTaskServiceImpl&method=delImageTask"

查看内部类方式
sh sandbox.sh -p 6310 -d "debug-watch/watch?class=com.ruijie.cvm.task.thrift.rcd_shell\$Client&method=exec_shell_cmd"
\$ 需要转义$符号，因为class可以写正则表达式

查看 sandbox启动模块情况
./sandbox.sh -p 18982 -l

强制刷新sandbox用户模块 (-F：强制刷新用户模块  -f：刷新用户模块)
./sandbox.sh -p 18982 -F

-u：卸载指定模块
./sandbox.sh -p 18982 -u "debug-watch"

-m：查看模块详细信息
./sandbox.sh -p 18982 -m "debug-watch"

-S : 关闭jvm-sandbox 服务
./sandbox.sh -p 18982 -S

```

### 优化
```
1. 修改sandbox-core中ModuleHttpServlet类中doGet/ doPost方法添加
          req.setCharacterEncoding("UTF-8");
          resp.setCharacterEncoding("UTF-8");
修复Linux控制台显示中文乱码问题。
基于 sandbox 1.1.0 版本修订！（注意版本一致性）

2. 将jvm-sandbox-demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar包集成到
sandbox-stable-bin.zip中。目录结构：sandbox/sandbox-module/

3. 修改sandbox-stable-bin.zip解压缩后，在 sanbox/install-local.sh 中增加
        && cp -r ./sandbox-module ${SANDBOX_INSTALL_LOCAL}/ \
    用户自定义模块拷贝到跟所要安装模块路径一致。  /root/.opt/下。

4. 修改sandbox-stable-bin.zip中的配置
   目录结构为：sandbox/cfg/ 中 sandbox.properties，
   user_module=~/.opt/sandbox/sandbox-module；
   此处修改加载用户模块的默认配置。

```
