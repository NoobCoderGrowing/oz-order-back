package com.oz.ozorder.service;
import com.alibaba.fastjson2.JSONObject;
import com.oz.ozorder.entity.Order;
import com.oz.ozorder.mapper.OrderMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class OrderService {
    String allphanumeric= "^[a-zA-Z0-9. ]+$";

    @Resource
    SqlSessionFactory sqlSessionFactory;

    public List<Order> retrieveOrder(String customerPO, String onfCO, String customerName, Date date){
        int paramsCount = 0;
        HashMap<String, String> params = new HashMap<String, String>();
        if(StringUtils.isNotEmpty(customerPO) && customerPO.matches(allphanumeric)){
            paramsCount++;
            params.put("customerPO", customerPO);
        }
        if(StringUtils.isNotEmpty(onfCO) && onfCO.matches(allphanumeric)){
            paramsCount++;
            params.put("onfCO", onfCO);
        }
        if(StringUtils.isNotEmpty(customerName) && customerName.matches(allphanumeric)){
            paramsCount++;
            params.put("customerName", customerName);
        }
        if(paramsCount==0){
            return new ArrayList<Order>();
        }

        if(date!=null && !date.toString().equals("0001-01-01")){
            params.put("date", date.toString());
        }
        SqlSession session = sqlSessionFactory.openSession();
        OrderMapper orderMapper = session.getMapper(OrderMapper.class);
//        List<Order> result = session.selectList("retrieveOrders", params);
        List<Order> result = orderMapper.retrieveOrders(params);
        session.close();
        return result;
    }

    public Boolean insertOrders(final List<Map<String,String>> orders) throws SQLException {
        SqlSession session = sqlSessionFactory.openSession();
        Transaction transaction = new JdbcTransactionFactory().newTransaction(session.getConnection());
        OrderMapper orderMapper = session.getMapper(OrderMapper.class);
        try {
            for (int i = 0; i < orders.size(); i++) {
                HashMap<String, String> orderInfo = (HashMap)orders.get(i);
                Order order = JSONObject.parseObject(JSONObject.toJSONString(orderInfo),Order.class);
                orderMapper.insertOrder(order);
            }
            transaction.commit();
            session.close();
        }catch (Exception e){
            transaction.rollback();
            log.info("rollback with params: " + JSONObject.toJSONString(orders));
            e.printStackTrace();
            session.close();
            return false;
        }
        return true;
    }

    public void deleteALL(){
        SqlSession session = sqlSessionFactory.openSession();
        OrderMapper orderMapper = session.getMapper(OrderMapper.class);
        orderMapper.deleteAll();
        session.commit();
        session.close();
    }

}
