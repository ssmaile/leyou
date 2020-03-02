package cn.leyou.item.service;


import cn.leyou.item.pojo.Category;

import java.util.List;

public interface ICategoryService {

    List<Category> findById(Integer pid);
}
