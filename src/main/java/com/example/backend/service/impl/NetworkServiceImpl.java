package com.example.backend.service.impl;

import com.example.backend.entity.Network;
import com.example.backend.mapper.NetworkMapper;
import com.example.backend.service.INetworkService;
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
public class NetworkServiceImpl extends ServiceImpl<NetworkMapper, Network> implements INetworkService {

}
