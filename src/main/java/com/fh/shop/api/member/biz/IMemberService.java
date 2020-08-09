package com.fh.shop.api.member.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.po.Member;

public interface IMemberService {
    ServerResponse addMember(Member member);

    ServerResponse validaterMemName(String memName);

    ServerResponse validaterEmail(String email);

    ServerResponse validaterPhone(String phone);

    ServerResponse login(String memName, String password);
}
