package com.example.backend.service;

import com.example.backend.k3s.disk.Disk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.List;

@Service
public class DiskService {

   @Value("${disk.path}")
    private String diskPath;

    public boolean create(Disk disk) {
        // 在diskPath下创建一个名为disk.getName()的文件夹，如果存在则报错
        // 先检查diskPath是否存在，不存在则创建
        File file = new File(diskPath);
        if (!file.exists()) {
            boolean res = file.mkdirs();
            if(!res)return false;
        }
        // 创建diskPath/disk.getName()文件夹
        File diskFile = new File(diskPath + "/" + disk.getName());
        if (diskFile.exists()) {
            return false;
        }
        boolean res = diskFile.mkdirs();
        if(!res)return false;
        // 将podPath下的文件复制到diskPath/disk.getName()文件夹下
        // 用kubectl cp podName:podPath rootPath/diskPath/disk.getName()
        // 获取程序运行的rootPath
        String rootPath = System.getProperty("user.dir");
        List<String> command = List.of("kubectl", "cp", disk.getPodName() + ":" + disk.getPodPath(), rootPath + "/" + diskPath + "/" + disk.getName());
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            // output stdout and stderr
//            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
//            BufferedReader errorReader = new BufferedReader(new java.io.InputStreamReader(process.getErrorStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            while ((line = errorReader.readLine()) != null) {
//                System.out.println(line);
//            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Disk disk) {
        // 删除diskPath/disk.getName()文件夹及其下所有文件
        File diskFile = new File(diskPath + "/" + disk.getName());
        if (!diskFile.exists()) {
            return false;
        }
        return deleteAllFileInDirectory(diskFile.getAbsolutePath());
    }

    public String getDiskPath(Disk disk) {
        return System.getProperty("user.dir") + "/" + diskPath + "/" + disk.getName();
    }

    private boolean deleteAllFileInDirectory(String filePath) {
        // 删除diskPath下所有文件夹及其下所有文件
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteAllFileInDirectory(f.getAbsolutePath());
                }else{
                    boolean res;
                    res = f.delete();
                    if (!res){
                        return false;
                    }
                }
            }
        }

        return file.delete();
    }


}