package com.fh.shop.api.mq;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailMessage implements Serializable {

    private String email;

    private String title;

    private String content;

    private String realName;
}
