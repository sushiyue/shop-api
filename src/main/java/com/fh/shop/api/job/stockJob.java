package com.fh.shop.api.job;

import com.fh.shop.api.product.biz.IProductService;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.util.MailUtil;
import org.apache.commons.net.nntp.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class stockJob {

    @Autowired
    private MailUtil mailUtil;

    @Resource(name="productService")
    private IProductService productService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Scheduled(cron = "0/20 * * * * ?")
    public void a(){
        List<Product>list= productService.findPorduct();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\">\n" +
                        "     <thead>\n" +
                        "         <tr>\n" +
                        "             <th>商品名</th>\n" +
                        "             <th>价格</th>\n" +
                        "             <th>库存</th>\n" +
                        "         </tr>\n" +
                        "     </thead>\n" +
                        "    <tbody>"
                );
        for (Product product : list) {
            stringBuffer.append("<tr>\n" +
                    "        <td>"+product.getProductName()+"</td>\n" +
                    "        <td>"+product.getPrice().toString()+"</td>\n" +
                    "        <td>"+product.getStock()+"</td>\n" +
                    "        </tr>");
        }
        stringBuffer.append("</tbody>\n" +
                "</table>");
        String s = stringBuffer.toString();
        mailUtil.sendMail("3452603469@qq.com","库存不足",s);
    }
}
