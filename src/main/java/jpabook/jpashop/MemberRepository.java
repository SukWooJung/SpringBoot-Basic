package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        member.getId();
        member.getUsername();
        member.setUsername("정석우");
        em.persist(member);
        return member.getId(); // Id정도를 리턴하면 조회하기도 확인하기도 편함
    }

    public Member find(Long id){
        return em.find(Member.class , id);
    }

}
