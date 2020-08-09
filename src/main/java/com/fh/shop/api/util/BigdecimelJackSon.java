package com.fh.shop.api.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

public class BigdecimelJackSon extends JsonSerializer<BigDecimal> {


    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(null==bigDecimal){
            jsonGenerator.writeString("");
        }else{
            jsonGenerator.writeString(bigDecimal.toString());
        }
    }
}
