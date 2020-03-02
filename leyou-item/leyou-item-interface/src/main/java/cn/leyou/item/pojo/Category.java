package cn.leyou.item.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "is_parent")
    private Boolean isParent;
    private Integer sort;

}
