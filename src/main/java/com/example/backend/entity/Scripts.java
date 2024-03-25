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
public class Scripts implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("script_id")
    private Integer scriptId;

    private String scriptContent;

    public Integer getScriptId() {
        return scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
    }

    public String getScriptContent() {
        return scriptContent;
    }

    public void setScriptContent(String scriptContent) {
        this.scriptContent = scriptContent;
    }

    @Override
    public String toString() {
        return "Scripts{" +
            "scriptId = " + scriptId +
            ", scriptContent = " + scriptContent +
        "}";
    }
}
