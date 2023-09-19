package com.oz.ozorder.mapper;

import com.oz.ozorder.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    List<Order> retrieveOrders(Map params);

    void insertOrder(Order oder);

    void deleteAll();
}
