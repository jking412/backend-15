#!/bin/bash

# 启动vnc服务
vncserver \
    -localhost no \
    -geometry 1024x768 \
    -SecurityTypes None --I-KNOW-THIS-IS-INSECURE \
    :1

# 启动websockify代理服务
websockify --web /usr/share/novnc/ \
    6080 \
    localhost:5901