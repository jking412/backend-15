FROM uos:latest
LABEL authors="jking"

# 下载依赖
# 因为下载依赖的过程中会出现unix syslog delivery error的问题，不影响使用，所以这里先忽略错误
RUN apt-get update && \
    apt-get install -y xfce4 tigervnc-standalone-server novnc websockify || true


# 配置桌面环境
RUN mkdir /root/.vnc && \
    touch /root/.vnc/xstartup && \
    chmod +x /root/.vnc/xstartup && \
    echo "#! /bin/bash\nunset SESSION_MANAGER\nstartxfce4" > /root/.vnc/xstartup


# 复制启动脚本
COPY ./docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 6080

ENTRYPOINT "/docker-entrypoint.sh"