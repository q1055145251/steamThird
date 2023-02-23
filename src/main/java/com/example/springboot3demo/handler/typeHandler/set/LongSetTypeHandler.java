package com.example.springboot3demo.handler.typeHandler.set;

import com.alibaba.fastjson2.TypeReference;

import java.util.Set;

public class LongSetTypeHandler extends SetTypeHandler<Long> {

    @Override
    protected TypeReference<Set<Long>> specificType() {
        return new TypeReference<Set<Long>>() {};
    }
}

