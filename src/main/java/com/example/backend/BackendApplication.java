package com.example.backend;

import com.example.backend.k3s.K3s;
import com.example.backend.k3s.K3sPod;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.proto.V1;
import io.kubernetes.client.util.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
public class BackendApplication {



	public static void main(String[] args) throws Exception {

		K3s k3s = new K3s();

//		SpringApplication.run(BackendApplication.class, args);

		K3sPod pod = new K3sPod();
		pod.setPodName("uos");
		pod.setContainerName("uos");
		pod.setImageName("uos:v0.1.0");

		pod.create(k3s.getCoreV1Api(),"default");

		// sleep 3 seconds for wait uos pod ready
		Thread.sleep(3000);

		V1PodList podList =  k3s.listPod();
		for(V1Pod item : podList.getItems()){
			System.out.println(item.getMetadata().getName());
			if (item.getMetadata().getName().equals("uos")){
				System.out.println(item.getStatus().getPodIP());
			}
		}

	}

}
