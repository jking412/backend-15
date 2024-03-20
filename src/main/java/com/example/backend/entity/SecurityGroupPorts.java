package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("security_group_ports")
public class SecurityGroupPorts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "port_id", type = IdType.AUTO)
    private Integer portId;

    private String portName;

    private Integer port;

    private String protocol;

    private String description;

    private Integer groupId;

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "SecurityGroupPorts{" +
            "portId = " + portId +
            ", portName = " + portName +
            ", port = " + port +
            ", protocol = " + protocol +
            ", description = " + description +
            ", groupId = " + groupId +
        "}";
    }
}
