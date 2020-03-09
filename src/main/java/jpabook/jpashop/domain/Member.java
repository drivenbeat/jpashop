package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded // 내장 타입
    private Address address;

//    @OneToMany
//    @JsonIgnore // return시 json 객체를 무시할 수 있지만 서로다른 api를 사용하기 위하여 지양 v1 예제
    @OneToMany(mappedBy = "member")// 주인이 아닌 쪽에 설정 (읽기전용)
    // 반드시 아래와 같이 초기화 하고 변경하지 않는게 좋음
    private List<Order> order = new ArrayList<>();

}
