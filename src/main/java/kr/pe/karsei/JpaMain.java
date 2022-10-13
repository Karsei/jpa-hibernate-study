package kr.pe.karsei;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        // persistence.xml 에 있는 이름
        // 어플리케이션에서 딱 하나만 생성되어야 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gazuua");
        // 고객 요청이 올때마다(DB 커넥션을 얻어서 뭔가 한다던가) 꼭 EntityManager 를 만들어야 한다.
        EntityManager em = emf.createEntityManager();

        Member member = new Member();
        member.setId(1L);
        member.setName("mr Hong");
        em.persist(member);

        em.close();
        emf.close();
    }
}
