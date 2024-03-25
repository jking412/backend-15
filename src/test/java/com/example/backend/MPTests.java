package com.example.backend;

import com.example.backend.service.PodService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MPTests {

    @Resource
    private PodService podService;

    @Test
    public void selectAllContainer() throws Exception {
        System.out.println(podService.listAll());
    }
//    }

}
