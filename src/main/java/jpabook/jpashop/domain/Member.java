package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 내장 타입
    private Address address;

//    @OneToMany
    @OneToMany(mappedBy = "member")// 주인이 아닌 쪽에 설정 (읽기전용)
    private List<Order> order = new ArrayList<>();

}
