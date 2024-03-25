package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.backend.utils.Regexp;
import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.models.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 
 * </p>
 *
 * @author kunkun
 * @since 2024-03-20
 */

@TableName("pod_info")
@NoArgsConstructor
public class PodInfo implements Serializable {


    private static final long serialVersionUID = 1L;

    @TableId(value = "pod_id", type = IdType.AUTO)
    private Integer podId;

    private BigDecimal cpuReq;

    private BigDecimal cpuLimit;

    private BigDecimal memoryReq;

    private BigDecimal memoryLimit;

    private String passwd;

    private String podName;

    private String hostName;

    private String containerName;

    private Integer imageId;

    private String imagePullPolicy;

    private String labels;



    public Integer getPodId() {
        return podId;
    }

    public void setPodId(Integer podId) {
        this.podId = podId;
    }

    public BigDecimal getCpuReq() {
        return cpuReq;
    }

    public void setCpuReq(BigDecimal cpuReq) {
        this.cpuReq = cpuReq;
    }

    public BigDecimal getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(BigDecimal cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public BigDecimal getMemoryReq() {
        return memoryReq;
    }

    public void setMemoryReq(BigDecimal memoryReq) {
        this.memoryReq = memoryReq;
    }

    public BigDecimal getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(BigDecimal memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImagePullPolicy() {
        return imagePullPolicy;
    }

    public void setImagePullPolicy(String imagePullPolicy) {
        this.imagePullPolicy = imagePullPolicy;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "PodInfo{" +
            "podId = " + podId +
            ", cpuReq = " + cpuReq +
            ", cpuLimit = " + cpuLimit +
            ", memoryReq = " + memoryReq +
            ", memoryLimit = " + memoryLimit +
            ", passwd = " + passwd +
            ", podName = " + podName +
            ", hostName = " + hostName +
            ", containerName = " + containerName +
            ", imageId = " + imageId +
            ", imagePullPolicy = " + imagePullPolicy +
            ", labels = " + labels +
        "}";
    }
    public PodInfo(V1Pod item, int podId){
        this.podId = podId;
        V1PodSpec spec = item.getSpec();
        if (spec != null) {
            List<V1Container> containers = spec.getContainers();
            if (containers != null && !containers.isEmpty()) {
                V1Container container = containers.get(0);
                if (container != null) {
                    V1ResourceRequirements resources = container.getResources();
                    if (resources != null) {
                        Map<String, Quantity> requests = resources.getRequests();
                        if (requests != null && requests.containsKey("cpu")) {
                            this.cpuReq = new BigDecimal(requests.get("cpu").getNumber().toString());
                        }
                    }
                    List<V1EnvVar> env = container.getEnv();
                    if (env != null && !env.isEmpty()) {
                        this.passwd = env.get(0).getValue();
                    }
                    this.containerName = container.getName();
                    String image = container.getImage();
                    if (image != null) {
                        if (image.contains("uos")) this.imageId = 1;
                        else if (image.contains("kylin")) this.imageId = 2;
                        else this.imageId = 3;
                    }
                    this.imagePullPolicy = container.getImagePullPolicy();
                }
            }
            this.hostName = spec.getNodeName();
        }

        V1ObjectMeta metadata = item.getMetadata();
        if (metadata != null) {
            this.podName = metadata.getName();
            Map<String, String> labels = metadata.getLabels();
            if (labels != null) {
                this.labels = labels.toString();
            }
        }
    }
}
