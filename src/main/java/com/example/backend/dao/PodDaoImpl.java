package com.example.backend.dao;

import com.example.backend.entity.Pod;
import com.example.backend.k3s.K3s;
import com.example.backend.utils.Regexp;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PodDaoImpl implements PodDao{

    @Autowired
    private K3s k3s;

    @Autowired
    private Regexp regexp;
    // 这个函数的正确性需要以下1点来保证
    // 1. 一个pod中只有一个container
    @Override
    public List<Pod> listPods(String podImage) throws ApiException {

        List<Pod> res = new ArrayList<>();

        V1PodList podList = k3s.listPod();
        for (var item : podList.getItems()){
            // 需要条件1
            String containerName = item.getSpec().getContainers().get(0).getName();

            if(containerName == null)continue;
            if(item.getMetadata().getDeletionTimestamp() != null)continue;

            String tmpPodImage = regexp.getPodImage(containerName);

            if (tmpPodImage.equals(podImage)){

                int podId = regexp.getPodId(containerName);
                if(podId == -1)continue;
                Pod pod = new Pod(item,podId);

                res.add(pod);
            }
        }

        return res;
    }

    @Override
    public Pod getPod(String podImage,String podName) throws ApiException {
        List<Pod> podList = listPods(podImage);
        Pod res = null;
        for (var item : podList){
            if (item.getV1Pod().getMetadata() == null)continue;
            if (item.getV1Pod().getMetadata().getName().equals(podName)){
                res = item;
                break;
            }
        }

        return res;
    }

    @Override
    public void createPod(Pod pod) {
        return;
    }

    @Override
    public void deletePod(String podName) {
        return;
    }
}
