package com.example.backend.dao;

import com.example.backend.entity.PodInfo;
import com.example.backend.entity.Pod_old;
import io.kubernetes.client.openapi.ApiException;

import java.util.List;

public interface PodDao {
    public List<PodInfo> listPods(String podImage) throws ApiException;
    public PodInfo getPod(String podImage, String podName) throws ApiException;
    public void createPod(Pod_old podOld);
    public void deletePod(String podName);
}
