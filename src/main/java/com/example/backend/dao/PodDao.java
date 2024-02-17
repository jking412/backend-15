package com.example.backend.dao;

import com.example.backend.entity.Pod;
import io.kubernetes.client.openapi.ApiException;

import java.util.List;

public interface PodDao {
    public List<Pod> listPods(String podImage) throws ApiException;
    public Pod getPod(String podImage,String podName) throws ApiException;
    public void createPod(Pod pod);
    public void deletePod(String podName);
}
