package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.configure.Constants;
import com.example.backend.entity.Images;
import com.example.backend.entity.PodInfo;
import com.example.backend.k3s.K3s;
import com.example.backend.k3s.K3sPod;
import com.example.backend.mapper.PodInfoMapper;
import com.example.backend.utils.Regexp;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class PodService {

    @Autowired
    private UosService uosService;

    @Autowired
    private PodInfoMapper podInfoMapper;

    @Autowired
    private K3s k3s;

    @Autowired
    private Regexp regexp;

    @Autowired
    private IImagesService imagesService;


    public int create(K3sPod k3sPod) throws Exception{
        if (k3sPod.getImageName().equals(Constants.UOS_IMAGE_NAME)){
            return uosService.create(k3sPod);
        }
        return -1;
    }
    public int insert(K3sPod pod) throws Exception{
        // 将pod信息存入数据库
        PodInfo podInfo = new PodInfo();
        V1PodList podList = k3s.listPod();
        String podImage=null;
        Integer podId = null;
        for (var item : podList.getItems()){
            // 需要条件1
            String containerName = Objects.requireNonNull(item.getSpec()).getContainers().get(0).getName();

            if(containerName == null)continue;
            if(Objects.requireNonNull(item.getMetadata()).getDeletionTimestamp() != null)continue;

            String tmpPodImage = regexp.getPodImage(containerName);
            //todo:建立image表，并插入2个image数据
            podImage = pod.getImageName();
            if (tmpPodImage.equals(podImage)){
                podId = regexp.getPodId(containerName);
            }
        }

        podInfo.setPodId(podId);
        podInfo.setPodName(pod.getPodName());
        podInfo.setCpuReq(BigDecimal.valueOf(pod.getCpuReq()));
        podInfo.setCpuLimit(BigDecimal.valueOf(pod.getCpuLimit()));
        podInfo.setMemoryReq(BigDecimal.valueOf(pod.getMemoryReq()));
        podInfo.setMemoryLimit(BigDecimal.valueOf(pod.getMemoryLimit()));
        podInfo.setPasswd(pod.getPasswd());
        podInfo.setHostName(pod.getHostName());
        podInfo.setContainerName(pod.getContainerName());
        podInfo.setImagePullPolicy(pod.getImagePullPolicy());
//        podInfo.setImageId(imagesService.getOne(new LambdaQueryWrapper<Images>().eq(Images::getImageName, podImage)).getImageId());
        podInfo.setLabels(pod.getLabels().toString());
        Integer res=podInfoMapper.insert(podInfo);
        if(res==1){
            return 1;
        }
        return 0;
    }

    public int deleteInDatabase(int num) throws Exception{
        // 从数据库中删除pod信息
        LambdaQueryWrapper<PodInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PodInfo::getPodId,num);
        if(podInfoMapper.delete(wrapper)==1){
            return 1;
        }
        return 0;
    }
    public List<PodInfo> listAll() throws Exception{
        // 从数据库中获取pod信息
        podInfoMapper.selectList(null).forEach(System.out::println);
        return podInfoMapper.selectList(null);

    }

    public PodInfo getPodInfo(int podId) throws Exception{
        // 从数据库中获取指定pod信息
        LambdaQueryWrapper<PodInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PodInfo::getPodId,podId);
        return podInfoMapper.selectOne(wrapper);
    }

    public boolean delete(String imageName,int num) throws Exception{
        if (imageName.equals(Constants.UOS_IMAGE_NAME)){
            return uosService.delete(num);
        }
        return false;
    }
    public boolean deleteAllInDatabase() throws Exception{
        boolean res = podInfoMapper.deleteAll();
        // 从数据库中删除所有pod信息
        if(res){
            return true;
        }
        return false;
    }

    public List<K3sPod> list() throws Exception{
        return uosService.list();
    }

    public Object get(int num) {
        return podInfoMapper.selectById(num);
    }

    public boolean updateInDatabase(K3sPod k3sPod) {
        PodInfo podInfo = new PodInfo();
        podInfo.setPodId(k3sPod.getPodId());
        podInfo.setPodName(k3sPod.getPodName());
        podInfo.setCpuReq(BigDecimal.valueOf(k3sPod.getCpuReq()));
        podInfo.setCpuLimit(BigDecimal.valueOf(k3sPod.getCpuLimit()));
        podInfo.setMemoryReq(BigDecimal.valueOf(k3sPod.getMemoryReq()));
        podInfo.setMemoryLimit(BigDecimal.valueOf(k3sPod.getMemoryLimit()));
        podInfo.setPasswd(k3sPod.getPasswd());
        podInfo.setHostName(k3sPod.getHostName());
        podInfo.setContainerName(k3sPod.getContainerName());
        podInfo.setImagePullPolicy(k3sPod.getImagePullPolicy());
        podInfo.setLabels(k3sPod.getLabels().toString());
        Integer res = podInfoMapper.updateById(podInfo);
        if (res == 1){
            return true;
        }
        return false;
    }
}
