package com.example.backend;

import com.example.backend.k3s.*;
import com.example.backend.service.UosService;
import com.example.backend.utils.NginxConf;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.proto.V1;
import io.kubernetes.client.util.Config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootApplication()
@MapperScan("com.example.backend.mapper")
public class BackendApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(BackendApplication.class, args);
	}

}
