package com.fh.shop.api.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.member.po.Member;
import org.apache.ibatis.annotations.Insert;


public interface IMemberMapper extends BaseMapper<Member> {
    @Insert("insert into t_member(memName,password,realName,birthday,email,phone,shengId,shiId,xianId,areaName) " +
            "values(#{memName},#{password},#{realName},#{birthday},#{email},#{phone},#{shengId},#{shiId},#{xianId},#{areaName})")
    void addMember(Member member);
}
