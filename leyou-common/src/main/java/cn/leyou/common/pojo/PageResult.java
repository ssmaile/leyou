package cn.leyou.common.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private Long total;
    private Integer totalPage;
    private List<T> items;
}
