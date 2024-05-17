package com.example.steamThird.handler.typeHandler.list;

import com.alibaba.fastjson2.TypeReference;

import java.util.List;

public class StringListTypeHandler extends ListTypeHandler<String> {

    @Override
    protected TypeReference<List<String>> specificType() {
        return new TypeReference<List<String>>() {
        };
    }
}

