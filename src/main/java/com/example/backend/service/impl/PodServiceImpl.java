package com.example.backend.service.impl;

import com.example.backend.entity.Pod;
import com.example.backend.mapper.PodMapper;
import com.example.backend.service.IPodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kunkun
 * @since 2024-03-18
 */
@Service
public class PodServiceImpl extends ServiceImpl<PodMapper, Pod> implements IPodService {

}
