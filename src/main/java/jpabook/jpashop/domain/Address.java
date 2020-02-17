package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // 값 타입
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // 임베디드 타입은 자바 기본 생성자를 protected로 사용
    protected Address() {
    }

    // 데이터를 변동할 수 없게 생성자에서 값을 받아서 사용
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
