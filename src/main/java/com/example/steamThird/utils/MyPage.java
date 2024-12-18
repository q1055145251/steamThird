package com.example.steamThird.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.steamThird.utils.bean.BeanUtils;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MyPage<T> implements Serializable {

    @NotNull
    private List<T> records;

    @NotNull
    private long total;

    @NotNull
    private long size;

    @NotNull
    private long current;

    private List<OrderItem> orders;

    @NotNull
    private Boolean searchCount;

    @NotNull
    private long pages;

    public void setTotal(long total) {
        this.total = total;
        if (size != 0) {
            pages = (long) Math.ceil((double) total / size);
        }
    }

    public MyPage() {
    }

    /**
     * 将page转换成myPage
     *
     * @param page page
     * @param <T>  数据类型
     * @return myPage
     */
    public static <T> MyPage<T> to(Page<T> page) {
        MyPage<T> myPage = new MyPage<>();
        BeanUtils.copyBeanProp(myPage, page);
        myPage.records = page.getRecords();
        return myPage;
    }

    /**
     * 将IPage转换成myPage
     *
     * @param page IPage
     * @param <T>  数据类型
     * @return myPage
     */
    public static <T> MyPage<T> to(IPage<T> page) {
        MyPage<T> myPage = new MyPage<>();
        BeanUtils.copyBeanProp(myPage, page);
        myPage.records = page.getRecords();
        return myPage;
    }
}
