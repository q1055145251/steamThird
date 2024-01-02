package com.example.springboot3demo.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot3demo.utils.bean.BeanUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Schema(description = "分页对象")
@Data
public class MyPage<T> implements Serializable {

    @Schema(description = "数据")
    @NotNull
    private List<T> records;

    @Schema(description = "总数")
    @NotNull
    private long total;


    @Schema(description = "每页总长度")
    @NotNull
    private long size;

    @Schema(description = "当前页数")
    @NotNull
    private long current;

    @Schema(description = "排序字段信息")
    private List<OrderItem> orders;

    @Schema(description = "是否查询总数")
    @NotNull
    private Boolean searchCount;

    @Schema(description = "总页数")
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
