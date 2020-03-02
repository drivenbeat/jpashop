package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 직접 만들지 못하게 제약
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 둘다 사용 (개발용) 인 경우 연관관계의 주인을 설정해야 하며 주로 fk가 가까이 있는 객체를 주인으로 함 (1:N 중의 N) 주인이 변경되는 쪽
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 연관테이블을 같이 묶어서 처리 (persist 설정하지 않을 경우 각각 해줘야 함)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //배송정보

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // enum 타입은 숫자로 넣지 말것
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // ============= 연관 관계 메서드 ===============
    public void setMember(Member member){
        this.member = member;
        member.getOrder().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 생성 메서드 ==
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        return order;
    }

    //== 비즈니스 로직==
    /**
     * 주문 취소
     */
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료 된 상품은 취소가 불가능합니다");
        }
        this.setStatus(OrderStatus.CANCEL);
        // 재고 원상 복구
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    //== 조회 로직==
    /**
     * 주문상품 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
