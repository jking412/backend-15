package com.example.backend;

import com.example.backend.utils.NginxConf;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.FileWriter;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() throws Exception {


	}

	@Test
	void nginxConfTest() throws Exception {
		NginxConf nginxConf = new NginxConf("data/nginx/test.conf");
		nginxConf.addUpstream("uos-svc1",80);
		nginxConf.addUpstream("uos-svc2",80);
		nginxConf.addLocationPrefix("uos-svc","/env/1");
		nginxConf.addLocationPrefix("uos-svc2","/env/2");
		nginxConf.writeToNginxConfFile();
	}



}
