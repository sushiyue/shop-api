package com.fh.shop.api.consign.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.consign.po.Consign;

import java.util.List;

public interface ConsignService {
    List<Consign> findList(Long id);

    ServerResponse addConsign(Consign consign, Long id);
}
