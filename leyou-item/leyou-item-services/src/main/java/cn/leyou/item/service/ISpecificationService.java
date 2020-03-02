package cn.leyou.item.service;

import cn.leyou.item.pojo.SpecGroup;
import cn.leyou.item.pojo.SpecParam;

import java.util.List;

public interface ISpecificationService {


    List<SpecGroup> querySpecGroupByCid(Long cid);

    List<SpecParam> querySpecParamByGid(Long gid,Long cid, Boolean generic, Boolean searching);
}
