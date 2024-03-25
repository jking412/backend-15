package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "image_id", type = IdType.AUTO)
    private Integer imageId;

    private String imageName;

    private String imageTag;

    private Long imageSizeInBytes;

    private String architecture;

    private String os;

    private String author;

    private LocalDateTime createdAt;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public Long getImageSizeInBytes() {
        return imageSizeInBytes;
    }

    public void setImageSizeInBytes(Long imageSizeInBytes) {
        this.imageSizeInBytes = imageSizeInBytes;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Images{" +
            "imageId = " + imageId +
            ", imageName = " + imageName +
            ", imageTag = " + imageTag +
            ", imageSizeInBytes = " + imageSizeInBytes +
            ", architecture = " + architecture +
            ", os = " + os +
            ", author = " + author +
            ", createdAt = " + createdAt +
        "}";
    }
}
