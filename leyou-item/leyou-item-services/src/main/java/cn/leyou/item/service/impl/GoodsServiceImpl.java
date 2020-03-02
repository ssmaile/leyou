package cn.leyou.item.service.impl;

import cn.leyou.common.pojo.PageResult;
import cn.leyou.item.mapper.*;
import cn.leyou.item.pojo.*;
import cn.leyou.item.bo.SpuBo;
import cn.leyou.item.service.IGoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;
    /**
     * 查询spu分页结果集
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        //设置查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        //添加上下架的过滤条件
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }
        //添加分页
        PageHelper.startPage(page,rows);
        //查询结果集
        List<Spu> spus = spuMapper.selectByExample(example);
        //将结果集转换为SpuBo对象
        PageInfo<Spu> pageInfo = new PageInfo<Spu>(spus);

        List<SpuBo> spuBos = spus.stream().map(spu -> {
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            List<String> names = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            spuBo.setBname(brand.getName());
            spuBo.setCname(StringUtils.join(names, "-"));
            return spuBo;
        }).collect(Collectors.toList());
        //返回分页结果集
        PageResult<SpuBo> result = new PageResult<SpuBo>();
        result.setTotal(pageInfo.getTotal());
        result.setItems(spuBos);
        return result;
    }

    /**
     * 新增商品
     * @param spuBo
     */
    @Transactional
    @Override
    public void saveGoods(SpuBo spuBo) {
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        //添加spu的信息
        spuMapper.insertSelective(spuBo);
        SpuDetail spuDetail = spuBo.getSpuDetail();
        //添加spuDetail信息
        spuDetail.setSpuId(spuBo.getId());
        spuDetailMapper.insertSelective(spuDetail);

        saveSkuAndStock(spuBo);

    }

    private void saveSkuAndStock(SpuBo spuBo){
        spuBo.getSkus().forEach(sku -> {
            //添加sku信息
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insertSelective(sku);

            //添加stock的信息
            Stock stock = new Stock();
            stock.setStock(sku.getStock());
            stock.setSkuId(sku.getId());
            stockMapper.insertSelective(stock);
        });
    }

    @Override
    public SpuDetail querySpuDetailBySpuId(Long pid) {
        return this.spuDetailMapper.selectByPrimaryKey(pid);
    }

    @Override
    public List<Sku> querySkusBySpuId(Long pid) {
        Sku scored = new Sku();
        scored.setSpuId(pid);
        List<Sku> skus = this.skuMapper.select(scored);
        skus.forEach(sku -> {
            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }

    @Transactional
    @Override
    public void updateGoods(SpuBo spuBo) {
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = skuMapper.select(record);
        skus.forEach(sku -> {
            //删除stock
            stockMapper.deleteByPrimaryKey(sku.getId());
        });
        //删除sku
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        skuMapper.deleteByPrimaryKey(sku);

        //新增sku
        List<Sku> skuList = spuBo.getSkus();
        saveSkuAndStock(spuBo);

        //更新spu 表
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setSaleable(null);
        spuBo.setValid(null);
        spuMapper.updateByPrimaryKeySelective(spuBo);

        //更新spuDetail
        spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
    }
}
