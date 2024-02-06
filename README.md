# Backend-15

第15届服务外包大赛后端代码

## 功能特性


## 软件架构



## 快速开始

### 依赖检查
如果需要在本机上完全独立的启动该项目，需要确保系统内有以下软件或资源:

1. k3s 1.28.5+k3s1
2. containerd 1.7.11-k3s2
3. image: uos:v0.1.0
4. nginx:alpine
5. java 17
6. k3s config

### 构建


1. 克隆源码

```bash
git clne https://github.com/jking412/backend-15.git
# 将k3s config文件放入backend-15目录下
mv path/config backend-15/config
```

2. 编译
```bash
cd backend-15
mvn install
```

### 快速部署

1. 下载Jar包
2. 将config文件放在jar包目录下
3. 启动运行

```bash
java -jar XXX.jar
```

## 使用指南


## 如何贡献

请阅读我们的[贡献指南](https://www.yuque.com/skynesser/whisrm/xgm9godof7n6z5zf)。


