package com.fh.shop.api.payLog.biz;

import com.fh.shop.api.common.ServerResponse;

public interface IPayLogService {
    ServerResponse createNative(Long memberId);

    ServerResponse queryStatus(Long memberId);
}
