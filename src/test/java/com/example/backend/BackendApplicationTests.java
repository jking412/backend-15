package com.example.backend;

import com.example.backend.service.UosService;
import com.example.backend.utils.NginxConf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.FileWriter;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() throws Exception {


	}

	@Autowired
	private NginxConf nginxConf;
	@Test
	void nginxConfTest() throws Exception {
		nginxConf.addUpstream("desktop-1","uos-svc:80");
		nginxConf.addUpstream("desktop-2","uos-svc2:80");
		nginxConf.addLocationPrefix("desktop-1","/env/1");
		nginxConf.addLocationPrefix("desktop-2","/env/2");
		nginxConf.writeToNginxConfFile();
	}



}
