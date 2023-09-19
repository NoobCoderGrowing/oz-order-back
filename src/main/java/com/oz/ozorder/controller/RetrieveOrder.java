package com.oz.ozorder.controller;


import com.alibaba.fastjson2.JSONObject;
import com.oz.ozorder.entity.Order;
import com.oz.ozorder.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/oz/order")
@Log4j2
public class RetrieveOrder {
    @Resource
    OrderService orderService;



    @RequestMapping(value = "/retrieve", method = RequestMethod.GET)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ResponseBody
    public List<Order> retrieveOrder(@RequestParam(defaultValue ="", required = false) String customerName,
                                     @RequestParam(defaultValue ="", required = false) String customerPO,
                                     @RequestParam(defaultValue ="", required = false) String onfCO,
                                     @RequestParam(defaultValue ="0001-01-01", required = false) Date date){

        log.info("request params are: " + customerName + ", " + customerPO + ", " + onfCO + ", " + date.toString());
        return orderService.retrieveOrder(customerPO,onfCO,customerName,date);
    }


    @RequestMapping(value = "/submitMultiple", method = RequestMethod.POST)
    @ResponseBody
    public Map submitMultiple(@RequestBody List<Map<String,String>> params){
        log.info("submit multiole params: " + JSONObject.toJSONString(params));
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        Boolean result = false;
        try {
            result = orderService.insertOrders(params);
        }catch (Exception e){
            log.error("rollback failed with params: " + JSONObject.toJSONString(params));
        }
        response.put("success",result);
        return response;
    }

    @RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
    @ResponseBody
    public Map deleteALL(){
        log.info("request to delete all records");
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        orderService.deleteALL();
        response.put("success",true);
        return response;
    }

    @RequestMapping(value = "/isHealthy", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Boolean> isHealthy(){
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("success",true);
        return response;
    }
}
