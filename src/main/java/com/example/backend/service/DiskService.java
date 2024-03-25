package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.k3s.disk.Disk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DiskService {

   @Value("${disk.path}")
    private String diskPath;
//
//   @Autowired
//   private VolumeMapper volumeMapper;

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
            process.waitFor();
//            Volume volume = new Volume();
//            volume.setVolumeName(disk.getName());
//            volume.setSizeMb((getDirectorySize(diskFile)/1024/1024));
//            if(volumeMapper.insert(volume) <= 0){
//                return false;
//            }
//             output stdout and stderr
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
//
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(Disk disk) {
        // 删除diskPath/disk.getName()文件夹及其下所有文件

        File diskFile = new File(diskPath + "/" + disk.getName());

        System.out.println(diskFile.exists()+" "+diskFile.isDirectory());
        boolean flag = diskFile.canRead();
//        QueryWrapper<Volume> wrapper = new QueryWrapper<>();
//        wrapper.eq("volume_name",disk.getName());
//        if (!diskFile.getAbsoluteFile().exists() || volumeMapper.delete(wrapper) <= 0) {
//            return false;
//        }
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

    public List<Object> getAllPodDiskPaths() {
        List<Object> disks = getAllDisks();
        return disks;
    }

    public List<Object> getAllDisks() {
        File file = new File(diskPath);
        File[] files = file.listFiles();
        List<Object> disks = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    HashMap<String,String> index = new HashMap<>();
                    index.put("name", f.getName());
                    index.put("path", f.getAbsolutePath());
                    index.put("size", getDirectorySize(f).toString());
                    disks.add(index);
                }
            }
        }
        return disks;
    }

    public static Long getDirectorySize(File directory) {
        Long size = 0L;
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        size += file.length();
                    } else {
                        size += getDirectorySize(file);
                    }
                }
            }
        } else if (directory.isFile()) {
            size += directory.length();
        }
        return size;
    }

}
