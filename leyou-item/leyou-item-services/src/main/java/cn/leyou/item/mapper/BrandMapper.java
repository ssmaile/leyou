package cn.leyou.item.mapper;

import cn.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{bid}) ")
    void saveBrandAndCategory(@Param("cid") Long id,@Param("bid")Long bid);

    @Select("select * from tb_category_brand c_t INNER JOIN tb_brand b ON c_t.brand_id=b.id where c_t.category_id=#{cid}")
    List<Brand> selectBrandsByCid(Long cid);
}
