package com.example.springboot3demo.handler.typeHandler.list;

import com.alibaba.fastjson2.TypeReference;

import java.util.List;

@SuppressWarnings("unused")
public class LongListTypeHandler extends ListTypeHandler<Long> {

    @Override
    protected TypeReference<List<Long>> specificType() {
        return new TypeReference<List<Long>>() {};
    }
}

