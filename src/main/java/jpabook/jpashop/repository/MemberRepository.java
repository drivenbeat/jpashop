package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor // 생성자 주입
public class MemberRepository {

//    @PersistenceContext // jpa entity manager에 주입
    private final EntityManager em;

    // 저장
    public void save(Member member){
        em.persist(member);
    }

    // 단건 조회
    public Member findOne(Long id){
        return em.find(Member.class, id); // type, pk
    }

    // 전체 조회
    public List<Member> findAll() {
        // jpql from 대상이 테이블이 아니라 entity
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 이름으로 조회
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
