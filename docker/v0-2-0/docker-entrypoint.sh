#!/bin/bash

# 设置vnc密码，默认为123456，如果存在环境变量VNC_PW，则使用环境变量的值
if [ -z $VNC_PW ]; then
    VNC_PW="123456"
fi

# 设置vnc密码
echo $VNC_PW | vncpasswd -f > /root/.vnc/passwd

# 启动vnc服务
vncserver \

    -localhost no \
    -geometry 1024x768 \
    -SecurityTypes VNCAUTH -passwd /root/.vnc/passwd \
    :1

# 启动websockify代理服务
websockify --web /usr/share/novnc/ \
    6080 \
    localhost:5901