package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue
    @Column(name="cateogry_id")
    private Long id;

    private String name;

    @ManyToMany // 실전에서는 X, 예제용
    @JoinTable(name="category_item",
        joinColumns= @JoinColumn(name="category_id"),
            inverseJoinColumns = @JoinColumn(name="item_id"))
    private List<Item> items = new ArrayList<>();


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

}
