package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kunkun
 * @since 2024-03-18
 */
@TableName("network_info")
public class Network implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serviceName;

    private String clusterIp;

    private String targetPort;

    private LocalDateTime createTime;

    private String port;

    private String labels;

    @TableId(value = "network_id", type = IdType.AUTO)
    private Long networkId;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    public String getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(String targetPort) {
        this.targetPort = targetPort;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public Long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

    @Override
    public String toString() {
        return "Network{" +
            "serviceName = " + serviceName +
            ", clusterIp = " + clusterIp +
            ", targetPort = " + targetPort +
            ", createTime = " + createTime +
            ", port = " + port +
            ", labels = " + labels +
            ", networkId = " + networkId +
        "}";
    }
}
