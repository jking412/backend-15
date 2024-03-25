package com.example.backend;

import com.example.backend.k3s.K3s;
import com.example.backend.service.UosService;
import com.example.backend.utils.NginxConf;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() throws Exception {


	}


	@Autowired
	private K3s k3s;
	@Test
	void nginxConfTest() throws Exception {
		// 正则表达式
		String regex = "\\w+";
		// 用正则表达式捕获1
		String str = "uos-12";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		matcher.find();
		System.out.println(matcher.group());

	}




}
