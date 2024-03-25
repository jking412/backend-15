package com.example.backend.entity;

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
 * @since 2024-03-20
 */
@TableName("network_info")
public class NetworkInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("network_id")
    private Integer networkId;

    private String networkName;

    private String clusterIp;

    private Integer securityGroupId;

    private LocalDateTime createTime;

    private Integer podId;

    public Integer getNetworkId() {
        return networkId;
    }

    public void setNetworkId(Integer networkId) {
        this.networkId = networkId;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getClusterIp() {
        return clusterIp;
    }

    public void setClusterIp(String clusterIp) {
        this.clusterIp = clusterIp;
    }

    public Integer getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(Integer securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getPodId() {
        return podId;
    }

    public void setPodId(Integer podId) {
        this.podId = podId;
    }

    @Override
    public String toString() {
        return "NetworkInfo{" +
            "networkId = " + networkId +
            ", networkName = " + networkName +
            ", clusterIp = " + clusterIp +
            ", securityGroupId = " + securityGroupId +
            ", createTime = " + createTime +
            ", podId = " + podId +
        "}";
    }
}
