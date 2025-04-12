package com.contrue.webapp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author confff
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    //总记录数
    private int total;
    //当前查询页
    private int currentPage;
    //页大小
    private int size;
    //查询结果
    private List<T> results;
    //偏移量：(currentPage-1)*size
    private int offset = (currentPage-1)*size;
    //排序方式
    private String sortBy;



}
