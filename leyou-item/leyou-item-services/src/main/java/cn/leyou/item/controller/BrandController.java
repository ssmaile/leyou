package cn.leyou.item.controller;

import cn.leyou.common.pojo.PageResult;
import cn.leyou.item.pojo.Brand;
import cn.leyou.item.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> selectBrandByPage(
            @RequestParam(name = "key", required = false) String key,   //搜索条件
            @RequestParam(name = "page", defaultValue = "1") Integer page,    //分页的页数
            @RequestParam(name = "rows", defaultValue = "5") Integer rows,    //每页条数
            @RequestParam(name = "sortBy", required = false) String sortBy,  //排序字段
            @RequestParam(name = "desc", required = false) Boolean desc    //升降序
    ) {

        PageResult<Brand> result= brandService.selectBrandByPage(key, page, rows, sortBy, desc);
        if(result==null|| CollectionUtils.isEmpty(result.getItems())){
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);

    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids")List<Long> cids){  //当前端返回的是json数据的时候只能用相应的对象去接受
        brandService.saveBrand(brand,cids);
        return ResponseEntity.ok().build();
    }
    /**
     * 根据cid查询品牌
     * @param cid
     * @return
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid")Long cid){
        List<Brand> brands=brandService.queryBrandsByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

}
