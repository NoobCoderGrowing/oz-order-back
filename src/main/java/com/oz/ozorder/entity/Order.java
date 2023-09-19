package com.oz.ozorder.entity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.sql.Date;
@Data

public class Order {
    private int orderID;
    private String customerPO;
    private String onfCO;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date date;
    private String customerName;
    private String basecode;
    private float width;
    private float length;
    private String bundleNo;
    private int rollNo;
    private float area;
    private float netWeight;
    private String remarks;
}

