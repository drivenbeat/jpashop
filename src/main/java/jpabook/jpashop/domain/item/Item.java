package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.standard.expression.EqualsExpression;

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

    //=== 비즈니스 로직 ==//

    /**
     * stock 증가
     * @param quantity
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     * @param quantity
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", categories=" + categories +
                '}';
    }
}
