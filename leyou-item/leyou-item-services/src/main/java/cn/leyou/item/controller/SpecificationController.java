package cn.leyou.item.controller;

import cn.leyou.item.pojo.SpecGroup;
import cn.leyou.item.pojo.SpecParam;
import cn.leyou.item.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spec")
public class SpecificationController {

    @Autowired
    private ISpecificationService specificationService;

    /**
     * 查询分组信息
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupByCid(@PathVariable(name = "cid")Long cid){
        List<SpecGroup> groups=specificationService.querySpecGroupByCid(cid);
        if(CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParamByGid(
            @RequestParam(name = "gid",required = false)Long gid,
            @RequestParam(name = "cid",required = false)Long cid,
            @RequestParam(name = "generic",required = false)Boolean generic,
            @RequestParam(name = "searching",required = false)Boolean searching
    ){
        List<SpecParam> params=specificationService.querySpecParamByGid(gid,cid,generic,searching);
        if(CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

}
