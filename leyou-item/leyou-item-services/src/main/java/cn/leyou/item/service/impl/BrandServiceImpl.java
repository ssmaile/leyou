package cn.leyou.item.service.impl;

import cn.leyou.common.pojo.PageResult;
import cn.leyou.item.mapper.BrandMapper;
import cn.leyou.item.pojo.Brand;
import cn.leyou.item.service.IBrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> selectBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //增加查询条件
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        //模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }
        //添加分页
        PageHelper.startPage(page, rows);
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        List<Brand> brandList = brandMapper.selectByExample(example);
        //将结果给PageInfo
        PageInfo<Brand> brandPageInfo = new PageInfo<>(brandList);

        //放入到通用结果集合中
        PageResult<Brand> result=new PageResult<>();
        result.setTotal(brandPageInfo.getTotal());
        result.setItems(brandPageInfo.getList());
        result.setTotalPage(brandPageInfo.getPages());
        return result;
    }

    @Transactional
    @Override
    public void saveBrand(Brand brand,List<Long> cids) {

        brandMapper.insert(brand);
        cids.forEach(id->{
            brandMapper.saveBrandAndCategory(brand.getId(),id);
        });


    }

    /**
     * 根据cid查询品牌
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return brandMapper.selectBrandsByCid(cid);
    }

}
