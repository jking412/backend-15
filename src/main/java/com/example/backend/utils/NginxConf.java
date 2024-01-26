package com.example.backend.utils;


import lombok.Data;

import java.io.*;
import java.util.List;
import java.util.Map;

@Data
public class NginxConf {

    // upstreamName: serverDomain
    private Map<String,String> upstream;
    // upstreamName: locationPrefix
    private Map<String,String> locationPrefix;
    private File nginxConfFile;

    //    upstream desktop {
    //        server uos-svc:80;
    //    }
    private static final String upstreamTemplate = """
            upstream %s {
                server %s;
            }
            """;

    //    server {
    //        listen 80;
    //        location /env/1/ {
    //        proxy_pass http://desktop/;
    //        proxy_set_header Host $http_host;
    //        proxy_set_header Accept-Encoding gzip;
    //    }
    //
    //
    //        location /env/1/websockify {
    //            proxy_pass http://desktop/;
    //            proxy_http_version 1.1;
    //            proxy_set_header Upgrade $http_upgrade;
    //            proxy_set_header Connection "Upgrade";
    //            proxy_set_header Host $host;
    //        }
    //
    //    }
    private static final String serverTemplate = """
            server {
                listen 80;
            %s
            }
            """;

    private static final String locationTemplate = """
            \tlocation %s {
            \t\tproxy_pass http://%s/;
            \t\tproxy_set_header Host $http_host;
            \t\tproxy_set_header Accept-Encoding gzip;
            \t}
            """;

    private static final String locationWebsockifyTemplate = """
            \tlocation %s/websockify {
            \t\tproxy_pass http://%s/;
            \t\tproxy_http_version 1.1;
            \t\tproxy_set_header Upgrade $http_upgrade;
            \t\tproxy_set_header Connection "Upgrade";
            \t\tproxy_set_header Host $host;
            \t}
            """;

    public NginxConf(String nginxConfFilePath) throws IOException {
        this.nginxConfFile = new File(nginxConfFilePath);
        if(!nginxConfFile.exists()){
            throw new IOException("nginx conf file not exists");
        }

        upstream = new java.util.HashMap<>();
        locationPrefix = new java.util.HashMap<>();
    }

    public void writeToNginxConfFile() throws IOException {
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
    }

    public void addUpstream(String upstreamName,String serverDomain){
        this.upstream.put(upstreamName,serverDomain);
    }

    public void addLocationPrefix(String upstreamName,String locationPrefix){
        this.locationPrefix.put(upstreamName,locationPrefix);
    }

//    public static void reloadNginx() throws IOException, InterruptedException {
//        List<String> command = List.of("kubectl","exec","nginx","--","nginx","-s","reload");
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        Process process = processBuilder.start();
//        process.waitFor();
//    }
}
