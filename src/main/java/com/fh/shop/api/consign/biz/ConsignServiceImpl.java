package com.fh.shop.api.consign.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.consign.mapper.ConsignMapper;
import com.fh.shop.api.consign.po.Consign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("consign")
public class ConsignServiceImpl implements ConsignService {

    @Autowired
    private ConsignMapper consignMapper;

    @Override
    public List<Consign> findList(Long id) {

        QueryWrapper<Consign> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("memberId",id);
        List<Consign> consigns = consignMapper.selectList(objectQueryWrapper);
        return consigns;
    }

    @Override
    public ServerResponse addConsign(Consign consign, Long id) {
        consign.setMemberId(id);
        consignMapper.insert(consign);
        return ServerResponse.success();
    }
}
