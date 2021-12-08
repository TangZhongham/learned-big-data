# 1. Linux 大全

👍👍👍👍todo

👍 印象笔记的内容
👍 其他wiki整理

> 打算把所有当前用过、好用的命令放到这个地方方便使用

<!-- TOC -->

- [1. Linux 大全](#1-linux-大全)
  - [1.1. 高频](#11-高频)
    - [1.1.1. Basic info](#111-basic-info)
  - [1.2. 文件](#12-文件)
  - [1.3. 用户](#13-用户)
  - [1.4. IO](#14-io)
    - [1.4.1. TODO 检测磁盘坏道](#141-todo-检测磁盘坏道)
    - [1.4.2. 磁盘测速](#142-磁盘测速)
  - [1.5. 网络](#15-网络)
    - [1.5.1. tcpdump](#151-tcpdump)
    - [1.5.2. nc](#152-nc)
  - [1.6. Java](#16-java)
  - [1.7. Docker + K8s](#17-docker--k8s)
  - [1.8. Useful scripts](#18-useful-scripts)
    - [1.8.1. 自动端口扫描+ps](#181-自动端口扫描ps)
  - [1.9. Linux 文件监控](#19-linux-文件监控)
    - [1.9.1. 方法1](#191-方法1)
    - [1.9.2. 方法2-用户记录监控](#192-方法2-用户记录监控)

<!-- /TOC -->

```shell


```

## 1.1. 高频


```shell
ps -ef see every process on the system using standard syntax
ps aux see every process on the system using BSD syntax
top
kill -9 pid
grep
netstat -anp | grep port
iostat 1s : report Central Processing Unit (CPU) statistics and input/output statistics for devices,
partitions and network filesystems
vmstat 2s 2 – Report virtual memory statistics
sar Collect, report, or save system activity information
-b :report I/O and transfer rate statistics
– -n {keyword} :report net work statistics. keywords are DEV,TCP,UDP,IP,IP6…
– -r :report memory utilization statistics
– -u report CPU utilization
```


### 1.1.1. Basic info

```shell
lsb_release -a :prints certain linux standard base and distribution information
uname –a :print system information
hostname :show or set the system’s host name
cat /proc/cpuinfo
cat /proc/meminfo
cat /proc/swaps
free –g :display amount of free and used memory in the system
df –hT :report file system disk space usage
ifconfig :configure a network interface
cat /etc/fstab
```



## 1.2. 文件


## 1.3. 用户

```bash
# linux 查看账户信息
cat /etc/passwd

# linux 查看账户密码
cat /etc/shadow
# 字段含义
root:$6$3zvHxrjK6Tlj/e9I$IjF5lIU69j8441SeFSrQ3bG.oEDqGYR070xi1SCEhCP0adAbv3iQ3uqewE8fhCi.Y9uoyICvoF7ShbA0xnITy/::0:99999:7:::
用户名：加密密码：最后一次修改时间：最小修改时间间隔：密码有效期：密码需要变更前的警告天数：密码过期后的宽限时间：账号失效时间：保留字段

# 查看密码加密手段
authconfig --test | grep hashing
password hashing algorithm is sha512
```

## 1.4. IO

```shell
# 寻找大文件
find /var/lib/docker -xdev -type f -size +1G -exec du -Sh {} +
```

### 1.4.1. TODO 检测磁盘坏道


### 1.4.2. 磁盘测速

$ dd if=path/to/input_file of=/path/to/output_file bs=block_size count=number_of_blocks


## 1.5. 网络

```shell
/etc/hosts

/etc/resolv.conf
每行以一个关键字开头，后接配置参数
– nameserver 定义DNS服务器的IP地址
– domain 定义本地域名
– search 定义域名的搜索列表
– sortlist 对返回的域名进行排序

# 网卡配置文件
cat /etc/sysconfig/network-scripts

# 后面接的名字就是网卡
ethtool device_name(上面的网卡名) 可以看是不是千兆网卡

# 查看网络接口信息
ifconfig –a 

# 查看某个监听

netstat -anp | grep 8180

# 查看本机所有 tcp 端口

netstat -nltp

# 查看本机所有 udp 端口

netstat -nlup

# 端口随机范围
sysctl -a | grep range
net.ipv4.ip_local_port_range = 32768    61000
```

### 1.5.1. tcpdump

```bash
ifconfig

tcpdump -i ens32 -s 0  -w ./target-all.cap

tcpdump -i ens32 -s 0 port 8553 -w ./target-8553.cap

tcpdump -i ens32 -s 0 port 10000 -w ./target-10000.cap

```

tcpdump -i ens32 -s 0  -w ./target-all.cap
tcpdump -i lo -s 0 port 10000 -w ./target-10000.cap
cap 文件直接导入wireshark
wireshark 过滤方法：udp.port == 8001 tcp.port == 8001

172.16.158.46:51050

tcpdump -i lo -s 0 port 10000 -w ./target-10000.cap
### 1.5.2. nc


nc -znv  172.18.120.28 1-1024 2>&1 | grep succeeded

https://program-think.blogspot.com/2019/09/Netcat-Tricks.html

nc 临时传文件

假设你有两台主机 A 与 B，你要把 A 主机上的文件 file1 传输到 B 主机上，保存为 file2

　　你先在【接收端】（B 主机）运行如下命令（其中的 xxx 是端口号）
nc -l -p xxx > file2

　　然后在【发送端】（A 主机）运行如下命令。
nc x.x.x.x xxx < file1

用 nc 传输文件，相当于是：直接在【裸 TCP】层面传输。你可以通俗理解为：【没有】应用层。
　　（不熟悉网络分层的同学，再去复习一下本文开头的 OSI 模型）
　　如果你传输的文件【超级大】或者文件数量【超级多】，用 nc 传输文件的性能优势会很明显（相比“FTP、SSH、共享目录...”而言）

高级：nc + dd 整磁盘备份

假设你要把 A 主机 /dev/sda 磁盘的【原始数据】整个复制到 B 主机的 /dev/sdb 磁盘。

　　你先在【接收端】（B 主机）运行如下命令（其中的 xxx 是端口号）
nc -l -p xxx | dd of=/dev/sdb

　　然后在【发送端】（A 主机）运行如下命令。
dd if=/dev/sda | nc x.x.x.x xxx
　　第二条命令中的 xxx 是端口号，要与第一条命令中的端口号相同；第二条命令中的 x.x.x.x 是【主机 B】的 IP 地址。

## 1.6. Java

```shell
jps -v | more | grep xxx

jinfo pid >> xxx.jinfo

# jstat [ option vmid [interval[s|ms] [count]] ]

# 每200毫秒查询一次 进程43 的垃圾收集情况，一共查询5次
jstat -gc 43 200 5
# 2 秒
jstat -gc 43 2s 5

jstat -gccause 43 2 5

# 显示堆中对象统计信息，包括类、实例数量、合计容量
jmap -histo:live ${JAVA_PID}     
# 【一般简称为jmap（统计信息）】相关效果： 触发一次常规full gc

jmap -dump:live,format=b,file=${FILE_PATH} ${JAVA_PID}   
#【一般简称为heapdump】
# 相关效果： 触发一次常规full gc，将所有live对象写入文件。
# 操作及注意事项：
  # a. 由于live对象总大小最大可能达到堆大小#(-Xmx指定)，甚至由于存储格式问题达到更大，注意存储文件的磁盘可用空间。
  # b. 一般可先使用histo分析一下活对象总大小，以此估计文件大小。
  # c. 网络传输该文件强烈建议先采用常见压缩.tar.gz等，压缩率可达10倍。

jstack pid
sudo -u hive /usr/java/latest/bin/jstack {PID}
```

LGCC：Cause of last Garbage Collection
GCC：Cause of current Garbage Collection
YGC ：对新生代堆进行GC。频率比较高，因为大部分对象的存活寿命较短，在新生代里被回收。性能耗费较小。
FGC ：全堆范围的GC。默认堆空间使用到达80%(可调整)的时候会触发FGC。


## 1.7. Docker + K8s

1、docker安装失败

查看manager的agent报错，如果是Disassociated则尝试重启manager和agent；

之前安装过docker，再次安装之前的docker环境没有删除干净；

使用findmnt | grep docker查看docker相关挂载信息是否正确；

yum search docker和yum list installed | grep docker查看是否有docker安装包以及安装的docker版本；

 

2、docker起不来

使用systemctl status docker -l查看docker状态及启动文件参数等；

使用journalctl -u docker -n 1000查看docker的详细日志信息；

使用findmnt | grep docker查看docker相关挂载信息是否正确；

查看findmnt | grep cgroup查看cgroup是否正常挂载了；（可能人为umount -a导致cgroup被卸载了）重启机器或者手动将cgroup挂载上去；

使用dmesg查看docker对应的磁盘是否坏了；

使用xfs_info查看docker磁盘格式是否正确（ftype=1）；

3、docker磁盘满了

正常情况下/var/lib/docker是单独挂载磁盘的，并且磁盘越大越好；比较常见的问题是docker磁盘满了，然后就会导致docker各种状态不正常，比如卡住了、容器状态不正常等。

比较快速的查找方法是：find /var/lib/docker -xdev -type f -size +1G -exec du -Sh {} + 查找docker盘里面文件大于1G的大文件；

4、docker磁盘损坏

使用dmesg查看磁盘状态信息；

换盘操作参考wiki：http://172.16.1.168:8090/pages/viewpage.action?pageId=20255237

 

5、docker容器起不来

docker ps -a | grep xxx查看起不来的容器的id和状态（一般为exited），使用docker logs {容器id}查看启动日志；

docker磁盘满了导致容器起不来；

镜像本身损坏或者被认为错误修改了；

容器里面文件权限不对（客户自己动过用户目录权限等），使用docker大招修复；

 

6、docker push或者pull镜像失败

查看registry服务是否正常；

查看registry-data目录空间是不是满了；

查看registry-data对应目录下面是否存在该镜像；

docker pull或者push的时候没有指定registry的地址，一般镜像都带有类似tdh-81:5000/的前缀；

 

7、docker 卡住没有响应

/var/lib/docker目录满了可能会导致docker hand住，可以重启机器解决；

docker的bug，systemctl restart docker 看是否能解决；

 

重要：Docker大招

在遇到docker hand住，容器启不来等其他异常情况，可以尝试使用docker大招修复；

systemctl stop docker

systemctl stop kubelet

kill  $(ps -aux|grep docker |awk '{print $2}')

kill $(ps -ef | grep docker-containerd-shim | awk '{ print $2 }')

rm -rf /var/lib/docker/*

docker-storage-setup --reset

docker-storage-setup

systemctl restart docker

docker load -i /etc/tos/conf/tos.tar.gz

systemctl start kubelet

原理说明：docker创建的容器是无状态的，一些状态信息（例如数据、配置文件等）其实都已经挂载进宿主机或者持久化到其他地方；这个时候可以通过上述大招，关闭docker相关容器和进程，删除/var/lib/docker/下面容器相关的无状态信息；然后重启docker和kubelet，这个时候k8s会自动将之前的容器调度起来；

http://172.16.1.168:8090/pages/viewpage.action?pageId=22679196

## 1.8. Useful scripts

### 1.8.1. 自动端口扫描+ps

有时候不仅需要扫端口，还需要根据端口确定是哪个服务，该脚本简易的使用 netstat 去拿到所有端口，然后挨个使用 ps 去拿进程信息


```bash
#/bin/bash

time=`date +%Y%m%d%H%M`

netstat -nltp >> ${time}zz.log;

cat ${time}zz.log | grep tcp | awk '{print $4}' > ${time}port.log;

cat ${time}zz.log | grep tcp | awk '{print $7}' | tr -cd "[0-9]\n" >> ${time}pid.log;

paste -d# ${time}port.log ${time}pid.log >> ${time}raw.log;

# 会生成 这个格式 的raw.log
# 如果要看udp 的话改成 netstat -nlup 并且grep udp （可能不行）

# :::4002,141200
# :::8323,15176

# 然后每个 执行 ps -ef | grep 15176, 添加到该行。

# for line in `cat ${time}pid.log`; do  ps aux | grep $line | head -n 1 >> ${time}c.log;  done;

for line in `cat ${time}pid.log`; do  ps $line | more | tail -n 1  >> ${time}c.log;  done;

# 最后再把都整合起来，就可以打开csv开始看了
paste -d# ${time}raw.log ${time}c.log >> ${time}-final.csv

echo "Final report in ${time}-final.csv"
```

## 1.9. Linux 文件监控

### 1.9.1. 方法1

可以使用 stat /etc/krb5.conf 查看最近更改时间。

脚本：

auditctl -w /etc/krb5.conf -p war
 echo "" >> /etc/krb5.conf
ausearch -f /etc/krb5.conf

如果报错：

```bash
[root@linux-4-36 ~] ausearch -f /etc/krb5.conf
<no matches>

则重启一下
查看进程
systemctl status auditd

关闭
systemctl stop  auditd （报错 Failed to stop auditd.service: Operation refused, unit auditd.service may be requested by dependency only (it is configured to refuse manual start/stop).

应该使用：
service auditd stop

service auditd start

然后
重新执行上面的添加条件
auditctl -w /etc/krb5.conf -p war
 echo "" >> /etc/krb5.conf
ausearch -f /etc/krb5.conf

此时应该有消息记录。
time->Mon Aug 16 09:44:16 2021
type=PROCTITLE msg=audit(1629078256.271:14131179): proctitle=636174002F6574632F6B7262352E636F6E66
type=PATH msg=audit(1629078256.271:14131179): item=0 name="/etc/krb5.conf" inode=134230045 dev=fd:00 mode=0100644 ouid=0 ogid=0 rdev=00:00 obj=system_u:object_r:krb5_conf_t:s0 objtype=NORMAL cap_fp=0000000000000000 cap_fi=0000000000000000 cap_fe=0 cap_fver=0
type=CWD msg=audit(1629078256.271:14131179):  cwd="/root"
type=SYSCALL msg=audit(1629078256.271:14131179): arch=c000003e syscall=2 success=yes exit=3 a0=7fff2d1227db a1=0 a2=1fffffffffff0000 a3=7fff2d1208a0 items=1 ppid=24041 pid=128926 auid=0 uid=0 gid=0 euid=0 suid=0 fsuid=0 egid=0 sgid=0 fsgid=0 tty=pts0 ses=10063 comm="cat" exe="/usr/bin/cat" subj=unconfined_u:unconfined_r:unconfined_t:s0-s0:c0.c1023 key=(null)

如果报错
[root@linux-4-36 ~]# ausearch -f /etc/krb5.conf
/var/log/audit/audit.log permissions should be 0600 or 0640
<no matches>

sudo chmod 0640 /var/log/audit/audit.log
```

其他使用方式：

auditctl示例
auditctl -w /etc/passwd -p war -k password_file
auditctl -w /tmp -p e -k webserver_watch_tmp
-w 监控文件路径 /etc/passwd, 
-p 监控文件筛选 r(读) w(写) x(执行) a(属性改变)
-k 筛选字符串，用于查询监控日志
auditctl -a exit,never -S mount
auditctl -a entry,always -S all -F pid=1005
-S 监控系统调用
-F 给出更多监控条件(pid/path/egid/euid等)


日志查询
设置了监控后，会在/var/log/audit/audit.log里出现日志。
可以用此命令查看日志：
ausearch -f /etc/passwd -x rm
-k  利用auditctl指定的key查询
-x  执行程序
 ausearch -ts today -k password-file
  ausearch -ts 3/12/07 -k password-file
-ts 指定时间后的log (start time)
-te 指定时间前的log (end time)

https://www.cnblogs.com/ahuo/archive/2012/08/24/2653905.html

监控删除

[root@CentOS-7-2 /var/log/test]# auditctl -l
-w /var/log/test/ -p rwxa
[root@CentOS-7-2 /var/log/test]# auditctl -W /var/log/test
[root@CentOS-7-2 /var/log/test]# auditctl -l
No rules
[root@CentOS-7-2 /var/log/test]# 
————————————————
版权声明：本文为CSDN博主「Blue summer」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u010039418/article/details/81038744


### 1.9.2. 方法2-用户记录监控

（该配置对所有用户生效）
通过在/etc/profile里面加入以下代码就可以实现：

```bash
# history ip record

history
USER_IP=`who -u am i 2>/dev/null| awk '{print $NF}'|sed -e 's/[()]//g'`
if [ "$USER_IP" = "" ]
then
USER_IP=`hostname`
fi
if [ ! -d /tmp/dbasky ]
then
mkdir /tmp/dbasky
chmod 777 /tmp/dbasky
fi
if [ ! -d /tmp/dbasky/${LOGNAME} ]
then
mkdir /tmp/dbasky/${LOGNAME}
chmod 300 /tmp/dbasky/${LOGNAME}
fi
export HISTSIZE=4096
DT=`date "+%Y-%m-%d_%H:%M:%S"`
export HISTFILE="/tmp/dbasky/${LOGNAME}/${USER_IP} dbasky.$DT"
chmod 600 /tmp/dbasky/${LOGNAME}/*dbasky* 2>/dev/null
```

source /etc/profile 使用脚本生效

退出用户，重新登录

上面脚本在系统的/tmp新建个dbasky目录，记录所有登陆过系统的用户和IP地址（文件名），每当用户登录/退出会创建相应的文件，该文件保存这段用户登录时期内操作历史，可以用这个方法来监测系统的安全性。



