package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
// 상속관계 설정 (1개의 테이블)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 상속관계 테이블 구분 컬럼명
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item { // 상속을 위한 추상 클래스 생성
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;


    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
