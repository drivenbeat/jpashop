package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 둘다 사용 (개발용) 인 경우 연관관계의 주인을 설정해야 하며 주로 fk가 가까이 있는 객체를 주인으로 함 (1:N 중의 N) 주인이 변경되는 쪽
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //배송정보

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // enum 타입은 숫자로 넣지 말것
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]
}
