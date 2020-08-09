package com.fh.shop.api.brand.mapper;

import com.fh.shop.api.brand.po.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface IBrandMapper {


    @Select("select id,brandName from t_brand")
    List<Brand> findList();

    void deleteBatch(List<Long> list);

    @Update("update t_brand set name=#{brandName} where id=#{id}")
    void update(Brand brand);

    @Delete("delete from t_brand where id=#{v}")
    void delete(Long id);

    @Insert("insert into t_brand (name) values (#{brandName})")
    void addBrand(Brand brand);
}
