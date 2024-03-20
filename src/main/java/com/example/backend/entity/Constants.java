package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kunkun
 * @since 2024-03-20
 */
public class Constants implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("constant_key")
    private String constantKey;

    private String constantValue;

    private String description;

    private String dataType;

    public String getConstantKey() {
        return constantKey;
    }

    public void setConstantKey(String constantKey) {
        this.constantKey = constantKey;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "Constants{" +
            "constantKey = " + constantKey +
            ", constantValue = " + constantValue +
            ", description = " + description +
            ", dataType = " + dataType +
        "}";
    }
}
