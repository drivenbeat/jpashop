package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional(readOnly = true) // 기본, lazy 등 위해 필요 - spring 의 transactional의 기능이 더 많다
@RequiredArgsConstructor // final 필드만 가지고 생성자 생성
public class MemberService {

    private final MemberRepository memberRepository; // final

//    @Autowired // 생성자로 쓰는게 좋으며 최신버전에서는 autowired 까지 해준다
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 최후의 방어로 name 을 unique 제약조건을 주는 것이
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     * @return
     */
//    @Transactional(readOnly = true) // 읽기에는 readonly가 성능상 좋다
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 회원 아이디로 조회
     * @param memberId
     * @return
     */
//    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
