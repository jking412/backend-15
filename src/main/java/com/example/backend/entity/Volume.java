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
 * @since 2024-03-18
 */
@TableName("volume_info")
public class Volume implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "volume_id", type = IdType.AUTO)
    private Integer volumeId;

    private String volumeName;

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

    public Long getSizeMb() {
        return sizeMb;
    }

    public void setSizeMb(Long sizeMb) {
        this.sizeMb = sizeMb;
    }

    @Override
    public String toString() {
        return "Volume{" +
            "volumeId = " + volumeId +
            ", volumeName = " + volumeName +
            ", sizeMb = " + sizeMb +
        "}";
    }
}
