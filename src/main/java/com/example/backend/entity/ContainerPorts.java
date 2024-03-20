package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kunkun
 * @since 2024-03-20
 */
@TableName("container_ports")
public class ContainerPorts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("port_id")
    private Integer portId;

    private Integer portNum;

    private Integer podId;

    private String protocol;

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public Integer getPortNum() {
        return portNum;
    }

    public void setPortNum(Integer portNum) {
        this.portNum = portNum;
    }

    public Integer getPodId() {
        return podId;
    }

    public void setPodId(Integer podId) {
        this.podId = podId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "ContainerPorts{" +
            "portId = " + portId +
            ", portNum = " + portNum +
            ", podId = " + podId +
            ", protocol = " + protocol +
        "}";
    }
}
