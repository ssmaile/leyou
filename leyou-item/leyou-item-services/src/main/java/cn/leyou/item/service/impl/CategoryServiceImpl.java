package cn.leyou.item.service.impl;

import cn.leyou.item.mapper.CategoryMapper;
import cn.leyou.item.pojo.Category;
import cn.leyou.item.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findById(Integer pid) {
        Category category=new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    public List<String> queryNamesByIds(List<Long> ids){
        List<Category> categoryList = categoryMapper.selectByIdList(ids);
        return categoryList.stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
