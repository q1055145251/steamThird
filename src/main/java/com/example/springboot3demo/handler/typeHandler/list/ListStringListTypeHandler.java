package com.example.springboot3demo.handler.typeHandler.list;

import com.alibaba.fastjson2.TypeReference;

import java.util.List;

public class ListStringListTypeHandler extends ListListTypeHandler<String> {

    @Override
    protected TypeReference<List<List<String>>> specificType() {
        return new TypeReference<List<List<String>>>() {};
    }
}

