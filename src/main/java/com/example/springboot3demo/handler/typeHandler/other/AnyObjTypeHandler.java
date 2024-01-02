package com.example.springboot3demo.handler.typeHandler.other;

import com.alibaba.fastjson2.TypeReference;

@SuppressWarnings("unused")
public class AnyObjTypeHandler extends ObjTypeHandler<Object> {

    @Override
    protected TypeReference<Object> specificType() {
        return new TypeReference<Object>() {
        };
    }
}

