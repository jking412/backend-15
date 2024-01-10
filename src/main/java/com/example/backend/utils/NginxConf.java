package com.example.backend.utils;


import lombok.Data;

import java.io.*;
import java.util.Map;

@Data
public class NginxConf {

    // service_domain: service_port
    private Map<String,Integer> upstream;
    // service_domain: location_prefix
    private Map<String,String> locationPrefix;
    private File nginxConfFile;

    //    upstream desktop {
    //        server uos-svc:80;
    //    }
    private static final String upstreamTemplate = "upstream desktop {\n" +
            "%s" +
            "}\n";

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
    private static final String serverTemplate = "server {\n" +
            "    listen 80;\n" +
            "%s\n" +
            "}\n";

    private static final String locationTemplate = "\tlocation %s {\n" +
            "\t\tproxy_pass http://desktop/;\n" +
            "\t\tproxy_set_header Host $http_host;\n" +
            "\t\tproxy_set_header Accept-Encoding gzip;\n" +
            "\t}\n";

    private static final String locationWebsockifyTemplate = "\tlocation %s/websockify {\n" +
            "\t\tproxy_pass http://desktop/;\n" +
            "\t\tproxy_http_version 1.1;\n" +
            "\t\tproxy_set_header Upgrade $http_upgrade;\n" +
            "\t\tproxy_set_header Connection \"Upgrade\";\n" +
            "\t\tproxy_set_header Host $host;\n" +
            "\t}\n";

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
        for(Map.Entry<String,Integer> entry: upstream.entrySet()){
            upstreamStringBuilder.append("\tserver ").
                    append(entry.getKey()).append(":").
                    append(entry.getValue()).append(";\n");
        }
        bufferedWriter.write(String.format(upstreamTemplate,upstreamStringBuilder.toString()));

        // 写入server
        StringBuilder serverStringBuilder = new StringBuilder();
        for(Map.Entry<String,String> entry: locationPrefix.entrySet()){
            serverStringBuilder.append(String.format(locationTemplate,entry.getValue()));
            serverStringBuilder.append(String.format(locationWebsockifyTemplate,entry.getValue()));
        }

        bufferedWriter.write(String.format(serverTemplate,serverStringBuilder.toString()));

        bufferedWriter.close();
    }

    public void addUpstream(String serverName,Integer serverPort){
        this.upstream.put(serverName,serverPort);
    }

    public void addLocationPrefix(String serverName,String locationPrefix){
        this.locationPrefix.put(serverName,locationPrefix);
    }
}
