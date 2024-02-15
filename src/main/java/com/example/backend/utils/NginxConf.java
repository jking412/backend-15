package com.example.backend.utils;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;

@Data
@Component
public class NginxConf {

    // upstreamName: serverDomain
    private Map<String,String> upstream;
    // upstreamName: locationPrefix
    private Map<String,String> locationPrefix;
    private File nginxConfFile;

//    private String upstreamTemplate = """
//            upstream %s {
//                server %s;
//            }
//            """;
//
//    private String serverTemplate = """
//            server {
//                listen 80;
//            %s
//            }
//            """;
//
//    private String locationTemplate = """
//            \tlocation %s {
//            \t\tproxy_pass http://%s/;
//            \t\tproxy_set_header Host $http_host;
//            \t\tproxy_set_header Accept-Encoding gzip;
//            \t}
//            """;
//
//    private String locationWebsockifyTemplate = """
//            \tlocation %s/websockify {
//            \t\tproxy_pass http://%s/;
//            \t\tproxy_http_version 1.1;
//            \t\tproxy_set_header Upgrade $http_upgrade;
//            \t\tproxy_set_header Connection "Upgrade";
//            \t\tproxy_set_header Host $host;
//            \t}
//            """;

    @Value("${nginx.template.upstream}")
    private String upstreamTemplate;

    @Value("${nginx.template.server}")
    private String serverTemplate;

    @Value("${nginx.template.location}")
    private String locationTemplate;

    @Value("${nginx.template.location-websockify}")
    private String locationWebsockifyTemplate;

    @Value("${nginx.conf.file.path}")
    private String nginxFilePath;

    @Value("${nginx.conf.file.name}")
    private String nginxFileName;

    public NginxConf(){
        upstream = new java.util.HashMap<>();
        locationPrefix = new java.util.HashMap<>();
    }

    public boolean writeToNginxConfFile() throws IOException {
        // 检查path是否存在
        File nginxFilePathFile = new File(nginxFilePath);
        if (!nginxFilePathFile.exists()){
            boolean res = nginxFilePathFile.mkdirs();
            if (!res)return false;
        }

        // 检查file是否存在
        nginxConfFile = new File(nginxFilePathFile,nginxFileName);
        if (!nginxConfFile.exists()){
            boolean res = nginxConfFile.createNewFile();
            if (!res)return false;
        }

        // 下面代码默认情况为截断写入
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nginxConfFile));

        // 写入upstream
        StringBuilder upstreamStringBuilder = new StringBuilder();
        for(Map.Entry<String,String> entry: upstream.entrySet()){
            upstreamStringBuilder.append(String.format(upstreamTemplate,entry.getKey(),entry.getValue()));
        }
        bufferedWriter.write(upstreamStringBuilder.toString());

        // 写入server
        StringBuilder serverStringBuilder = new StringBuilder();
        for(Map.Entry<String,String> entry: locationPrefix.entrySet()){
            serverStringBuilder.append(String.format(locationTemplate,entry.getValue(),entry.getKey()));
            serverStringBuilder.append(String.format(locationWebsockifyTemplate,entry.getValue(),entry.getKey()));
        }

        bufferedWriter.write(String.format(serverTemplate,serverStringBuilder));

        bufferedWriter.close();
        return true;
    }

    public void addUpstream(String upstreamName,String serverDomain){
        this.upstream.put(upstreamName,serverDomain);
    }

    public void addLocationPrefix(String upstreamName,String locationPrefix){
        this.locationPrefix.put(upstreamName,locationPrefix);
    }

    public void deleteUpstream(String upstreamName){
        this.upstream.remove(upstreamName);
    }

    public void deleteLocationPrefix(String upstreamName){
        this.locationPrefix.remove(upstreamName);
    }

//    public static void reloadNginx() throws IOException, InterruptedException {
//        List<String> command = List.of("kubectl","exec","nginx","--","nginx","-s","reload");
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        Process process = processBuilder.start();
//        process.waitFor();
//    }
}
