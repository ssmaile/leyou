package cn.leyou.item.service;

import cn.leyou.common.pojo.PageResult;
import cn.leyou.item.pojo.Brand;

import java.util.List;

public interface IBrandService {

    PageResult<Brand> selectBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    void saveBrand(Brand brand,List<Long> cids);
    /**
     * 根据cid查询品牌
     * @param cid
     * @return
     */
    List<Brand> queryBrandsByCid(Long cid);
}
