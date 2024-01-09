package com.example.backend;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BackendApplication {

	static CoreV1Api api;

	public static void main(String[] args) throws Exception {
//		SpringApplication.run(BackendApplication.class, args);

		// load config from kube_config
		ApiClient client = Config.fromConfig("/home/jking/.kube/config");
		Configuration.setDefaultApiClient(client);

		api = new CoreV1Api();

		createPod();

		// list pod
		V1PodList list = api.listPodForAllNamespaces(null, null, null,
				null, null, null, null,
				null, null, null);
		for (V1Pod item : list.getItems()) {
			System.out.println(item.getMetadata().getName());
		}
	}

	public static void createPod() throws Exception {
		//      yaml文件
		//		apiVersion: v1
		//			kind: Pod
		//		metadata:
		//			name: uos
		//		spec:
		//		containers:
		//			- name: uos
		//			image: uos:v0.1.0
		//			imagePullPolicy: IfNotPresent

		// create pod object
		V1Pod pod = new V1Pod();
		pod.setApiVersion("v1");
		pod.setKind("Pod");

		// create metadata
		V1ObjectMeta meta = new V1ObjectMeta();
		meta.setName("uos");
		pod.setMetadata(meta);

		// create pod spec
		V1PodSpec spec = new V1PodSpec();
		spec.addContainersItem(new V1Container()
				.name("uos")
				.image("uos:v0.1.0")
				.imagePullPolicy("IfNotPresent")
		);
		pod.setSpec(spec);

		// create k3s pod
		api.createNamespacedPod("default", pod, null, null, null,null);


	}

}
