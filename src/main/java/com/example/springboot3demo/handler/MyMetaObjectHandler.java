package com.example.springboot3demo.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Date date=new Date();
        metaObject.setValue("createdDate",date);
        metaObject.setValue("updateDate",date);
        metaObject.setValue("updateTimestamp",(date.getTime()));
        metaObject.setValue("createdTimestamp",(date.getTime()));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date date=new Date();
        metaObject.setValue("updateDate",date);
        metaObject.setValue("updateTimestamp",(date.getTime()));
    }
}
