package cn.leyou.item.service;

import cn.leyou.common.pojo.PageResult;
import cn.leyou.item.bo.SpuBo;
import cn.leyou.item.pojo.Sku;
import cn.leyou.item.pojo.SpuDetail;

import java.util.List;

public interface IGoodsService {
    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);
    /**
     * 新增商品
     * @param spuBo
     */
    void saveGoods(SpuBo spuBo);

    SpuDetail querySpuDetailBySpuId(Long pid);

    List<Sku> querySkusBySpuId(Long pid);

    void updateGoods(SpuBo spuBo);
}
