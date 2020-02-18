package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item", // N:N의 경우 중간테이블로 해결 (여기서 지정 - 실무에선 쓰지 않음- 필드추가 불가능)
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))  // 증간테이블에 들어가는 키값
    private List<Item> items = new ArrayList<>();

    // 셀프 참조 entity
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // ============= 연관 관계 메서드 ===============
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
