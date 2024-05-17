package com.example.steamThird.handler.typeHandler.list;

import com.alibaba.fastjson2.TypeReference;

import java.util.List;

@SuppressWarnings("unused")
public class IntegerListTypeHandler extends ListTypeHandler<Integer> {

    @Override
    protected TypeReference<List<Integer>> specificType() {
        return new TypeReference<List<Integer>>() {};
    }
}

