package com.example.backend;

import com.example.backend.k3s.disk.Disk;
import com.example.backend.service.DiskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;

@SpringBootTest
public class DiskTests {

    @Autowired
    private DiskService diskService;

    @Test
    void diskTest() throws IOException, InterruptedException {
        Disk disk = new Disk("disk-1","uos","/home/test");
        assert  diskService.create(disk);

    }

    @Test
    void deleteDiskTest() {
        Disk disk = new Disk("disk-1","uos","/home/test");
        assert diskService.delete(disk);
    }
}
