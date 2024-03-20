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
@TableName("volume_info")
public class VolumeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "volume_id", type = IdType.AUTO)
    private Integer volumeId;

    private String volumeName;

    private Integer podId;

    private Integer podName;

    private Integer podPath;

    private Long sizeMb;

    public Integer getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(Integer volumeId) {
        this.volumeId = volumeId;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public Integer getPodId() {
        return podId;
    }

    public void setPodId(Integer podId) {
        this.podId = podId;
    }

    public Integer getPodName() {
        return podName;
    }

    public void setPodName(Integer podName) {
        this.podName = podName;
    }

    public Integer getPodPath() {
        return podPath;
    }

    public void setPodPath(Integer podPath) {
        this.podPath = podPath;
    }

    public Long getSizeMb() {
        return sizeMb;
    }

    public void setSizeMb(Long sizeMb) {
        this.sizeMb = sizeMb;
    }

    @Override
    public String toString() {
        return "VolumeInfo{" +
            "volumeId = " + volumeId +
            ", volumeName = " + volumeName +
            ", podId = " + podId +
            ", podName = " + podName +
            ", podPath = " + podPath +
            ", sizeMb = " + sizeMb +
        "}";
    }
}
