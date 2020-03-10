package jpabook.jpashop.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * xToOne (ManyToOne, OneToOne) 성능 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class SimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleRepository orderSimpleRepository;

    // 맨땅 entity 사용 - 극도로 지양
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // LAZY 강제 초기화
            order.getDelivery().getAddress(); // LAZY 강제 초기화
        }

        return all;
    }

    // dto 사용...그러나.. for문 안에서 member, delivery entity를 계속 조회한다-_-)
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        // 1 + N 번 실행 (N * 각각의 Entity)
        // order 조회 1번 => order -> member 지연로딩 N번 => order => delivery 지연로딩 N번 (최악의 경우 - 영속성컨텍스트에 들어있는 경우 조회하지 않음)
        List<SimpleOrderDto> result = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(toList());

        return result;
    }

    // fetch join 100% 꼭 이해할 것
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {

        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(toList());

        return result;
    }

    // repository 에서 dto 직접 접근 - v3와 v4간의 장단점이 있음 - 필요한경우 package 별도 분리하여 사용 from 절은 fetch와 같으며 select절의 최적화 가능하나 범용성 떨어짐
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }

}
