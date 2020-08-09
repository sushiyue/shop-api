package com.fh.shop.api.member.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.mapper.IMemberMapper;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.mq.MQSend;
import com.fh.shop.api.mq.MailMessage;
import com.fh.shop.api.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service("memberService")
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl implements IMemberService{

    @Autowired
    private IMemberMapper memberMapper;
    @Autowired
    private MQSend mqSend;

    @Autowired
    private MailUtil mailUtil;
    @Override
    public ServerResponse addMember(Member member) {
        String memName = member.getMemName();
        String password = RSAUtil.decrypt(member.getPassword());
        String email = member.getEmail();
        String phone = member.getPhone();
        if(StringUtils.isEmpty(memName)||StringUtils.isEmpty(password)
                ||StringUtils.isEmpty(email)||StringUtils.isEmpty(phone)
        ){
            return ServerResponse.error(ResponseEnum.REG_MEMBER_IS_NULL);
        }
        QueryWrapper<Member> objectQueryWrapper = new QueryWrapper();
        objectQueryWrapper.eq("memName",memName);
        Member member1 = memberMapper.selectOne(objectQueryWrapper);
        if(member1!=null){
            return ServerResponse.error(ResponseEnum.REG_MEMNAME_IS_EIXIS);
        }
        QueryWrapper<Member> objectQueryWrapper1 = new QueryWrapper();
        objectQueryWrapper1.eq("email",email);
        Member member2 = memberMapper.selectOne(objectQueryWrapper1);
        if(member2!=null){
            return ServerResponse.error(ResponseEnum.REG_EMAIL_IS_EIXIS);
        }
        QueryWrapper<Member> objectQueryWrapper2 = new QueryWrapper();
        objectQueryWrapper2.eq("phone",phone);
        Member member3 = memberMapper.selectOne(objectQueryWrapper2);
        if(member3!=null){
            return ServerResponse.error(ResponseEnum.REG_PHONE_IS_EIXIS);
        }
        memberMapper.addMember(member);
            mailUtil.sendMail(email,"注册成功","恭喜"+member.getRealName()+"注册成功");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse validaterMemName(String memName) {
        if(StringUtils.isEmpty(memName)){
            return ServerResponse.error(ResponseEnum.REG_MEMBER_IS_NULL);
        }
        QueryWrapper<Member> objectQueryWrapper = new QueryWrapper();
        objectQueryWrapper.eq("memName",memName);
        Member member1 = memberMapper.selectOne(objectQueryWrapper);
        if(member1!=null){
            return ServerResponse.error(ResponseEnum.REG_MEMNAME_IS_EIXIS);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse validaterEmail(String email) {
        if(StringUtils.isEmpty(email)){
            return ServerResponse.error(ResponseEnum.REG_MEMBER_IS_NULL);
        }
        QueryWrapper<Member> objectQueryWrapper1 = new QueryWrapper();
        objectQueryWrapper1.eq("email",email);
        Member member2 = memberMapper.selectOne(objectQueryWrapper1);
        if(member2!=null){
            return ServerResponse.error(ResponseEnum.REG_EMAIL_IS_EIXIS);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse validaterPhone(String phone) {
        if(StringUtils.isEmpty(phone)){
            return ServerResponse.error(ResponseEnum.REG_MEMBER_IS_NULL);
        }
        QueryWrapper<Member> objectQueryWrapper2 = new QueryWrapper();
        objectQueryWrapper2.eq("phone",phone);
        Member member3 = memberMapper.selectOne(objectQueryWrapper2);
        if(member3!=null){
            return ServerResponse.error(ResponseEnum.REG_PHONE_IS_EIXIS);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(String memName, String password) {
        //判断会员名或密码是否为空
        if(StringUtils.isEmpty(memName)||StringUtils.isEmpty(password)){
            return ServerResponse.error(ResponseEnum.LOGIN_MEMBER_IS_EIXIS);
        }
        //根据会员名来查询是否有这个会员名
        QueryWrapper<Member> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("memName",memName);
        Member member = memberMapper.selectOne(objectQueryWrapper);
        if(member==null){
            return ServerResponse.error(ResponseEnum.LOGIN_MEMBER_NAME_IS_NOTEIXIS);
        }
        String pass = RSAUtil.decrypt(password);
        //判断密码是否正确
        if(!pass.equals(member.getPassword())){
            return ServerResponse.error(ResponseEnum.LOGIN_MEMBER_PASSWORD_IS_ERROR);
        }
        //生成token
        //生成token的样子类似 用户信息.用户信息签名
        //签名的目的：是为了保证用户信息不被篡改
        //生成签名md5(用户信息结合密钥)
        //sign代表签名secret/secretKey代表密钥
        //密钥是在服务器上的，黑客获取不到
        //首先生成用户信息的json
        MemberVo memberVo = new MemberVo();
        Long id = member.getId();
        memberVo.setId(id);
        memberVo.setMemName(memName);
        String uuid = UUID.randomUUID().toString();
        memberVo.setUuid(uuid);
        memberVo.setRealName(member.getRealName());
        //将java对象转换为json格式的字符串
        String memJson = JSONObject.toJSONString(memberVo);

        //将json数据进行Base64编译
        try {
            String Base64MemJson = Base64.getEncoder().encodeToString(memJson.getBytes("utf-8"));
            //生成sign签名
            String sign = MD5Util.sign(Base64MemJson, MD5Util.SECRET);
            //对签名进行Base64编译
            String Base64Sign = Base64.getEncoder().encodeToString(sign.getBytes("utf-8"));
            //处理登录超时
            RedisUtil.setex(KeyUtil.buidMember(uuid,id),"",KeyUtil.Login_sconds);
           // mailUtil.sendMail(member.getEmail(),"登录成功","恭喜"+member.getRealName()+"在"+ DateUtil.se2te(new Date(),DateUtil.FULL_TIME)+"登陆了");
            MailMessage mailMessage = new MailMessage();
            mailMessage.setEmail(member.getEmail());
            mailMessage.setRealName(member.getRealName());
            mailMessage.setContent("恭喜"+member.getRealName()+"在"+ DateUtil.se2te(new Date(),DateUtil.FULL_TIME)+"登陆了");
            mailMessage.setTitle("登录成功");
            String massageJson = JSONObject.toJSONString(mailMessage);
            mqSend.sendGoodsMessage(massageJson);
            return ServerResponse.success(Base64MemJson+"."+Base64Sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ServerResponse.error();
        }

    }
}
