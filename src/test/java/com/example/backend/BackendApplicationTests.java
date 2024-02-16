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

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() throws Exception {


	}

	@Autowired
	private NginxConf nginxConf;
	@Autowired
	private K3s k3s;
	@Test
	void nginxConfTest() throws Exception {
//		apiVersion: v1
//		kind: Pod
//		metadata:
//		name: busybox
//		spec:
//		containers:
//		- name: busy
//		image: busybox
//		imagePullPolicy: IfNotPresent
//		volumeMounts:
//		- mountPath: /etc/nginx/conf.d
//		name: ngx-conf
//		volumes:
//		- name: ngx-conf
//		emptyDir: {}

		V1Pod pod = new V1Pod();
		V1ObjectMeta metadata = new V1ObjectMeta();
		metadata.setName("busybox");
		pod.setMetadata(metadata);


		V1PodSpec podSpec = new V1PodSpec();
		V1Container container = new V1Container();
		container.setName("busy");
		container.setImage("busybox");
		container.setImagePullPolicy("IfNotPresent");

		// set command
		List<String> command = new ArrayList<>();
		command.add("sh");
		command.add("-c");

		// read shell from scripts/conf-gen.sh
		File file = new File("scripts/conf-gen.sh");
		FileReader fileReader = new FileReader(file);
		char[] buffer = new char[(int) file.length()];
		fileReader.read(buffer);
		fileReader.close();
		String shell = new String(buffer);
		command.add(shell);
		command.add("--");
		command.add("1");
		command.add("2");
		container.setCommand(command);

		V1VolumeMount volumeMount = new V1VolumeMount();
		volumeMount.setMountPath("/etc/nginx/conf.d");
		volumeMount.setName("ngx-conf");
		container.setVolumeMounts(List.of(volumeMount));
		podSpec.setContainers(List.of(container));
		V1Volume volume = new V1Volume();
		volume.setName("ngx-conf");
		V1EmptyDirVolumeSource emptyDir = new V1EmptyDirVolumeSource();
		volume.setEmptyDir(emptyDir);
		podSpec.setVolumes(List.of(volume));
		pod.setSpec(podSpec);
		CoreV1Api coreV1Api = k3s.getCoreV1Api();
		coreV1Api.createNamespacedPod("default", pod, null, null, null,null);


	}




}
