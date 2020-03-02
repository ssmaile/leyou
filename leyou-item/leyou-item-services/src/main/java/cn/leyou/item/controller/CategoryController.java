package cn.leyou.item.controller;

import cn.leyou.item.pojo.Category;
import cn.leyou.item.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> QueryCategoryByPid(@RequestParam(name = "pid",defaultValue = "0")Integer pid){
        //400参数不合法
        if(pid==null || pid<0){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.badRequest().build();
        }
        //404  找不到
        List<Category> categoryList = categoryService.findById(pid);
        if(CollectionUtils.isEmpty(categoryList)){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.notFound().build();
        }
        //200 查询成功
        return ResponseEntity.ok(categoryList);
    }


}
