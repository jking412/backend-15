package com.example.backend.mapper;

import com.example.backend.entity.PodInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kunkun
 * @since 2024-03-20
 */
public interface PodInfoMapper extends BaseMapper<PodInfo> {
    @Delete("delete from pod_info")
    boolean deleteAll();

}
