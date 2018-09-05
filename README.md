# jvm-sandbox-demo
阿里巴巴正式开源自研动态非侵入AOP解决方案：JVM-Sandbox  学习demo


### JVM-SANDBOX用户手册
```
https://github.com/alibaba/jvm-sandbox/wiki/USER-GUIDE

```


### 使用手册
```
查看class类下对应的方法,暂时不支持静态方法
./sandbox.sh -p 18982 -d "debug-watch/watch?class=com.ruijie.cvm.task.service.impl.ThriftTaskServiceImpl&method=delImageTask"

查看 sandbox启动模块情况
./sandbox.sh -p 18982 -l

强制刷新sandbox用户模块 (-F：强制刷新用户模块  -f：刷新用户模块)
./sandbox.sh -p 18982 -F

-u：卸载指定模块
./sandbox.sh -p 18982 -u "debug-watch"

-m：查看模块详细信息
./sandbox.sh -p 18982 -m "debug-watch"

```


