package kr.pe.karsei;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // persistence.xml 에 있는 이름
        // 어플리케이션에서 딱 하나만 생성되어야 하고, 전체에서 공유되어야 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gazuua");
        // 고객 요청이 올 때마다 (DB 커넥션을 얻어서 뭔가 한다던가) 꼭 EntityManager 를 만들어야 한다.
        // 스레드간에 절대 공유가 되면 안 된다. (사용 후 버려야 함)
        EntityManager em = emf.createEntityManager();

        // 트랜잭션 - 시작
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //crud(em);
            //jpql(em);
            //persistStatus(em);
            //sameEntity(em);
            //lazyCreate(em);
            //dirtyCheck(em);
            doFlush(em);

            // 트랜잭션 - 종료
            tx.commit();
        }
        catch (Exception e) {
            // 트랜잭션 - 롤백
            tx.rollback();
        }
        finally {
            em.close();
        }

        emf.close();
    }

    private static void crud(EntityManager em) {
        // 조회
        Member findMember = em.find(Member.class, 1L);
        System.out.println("findMember.id = " + findMember.getId());
        System.out.println("findMember.name = " + findMember.getName());

        // 수정
        // 모든 데이터 변경은 트랜잭션 안에서 실행되어야 한다.
        findMember.setName("Mr Hong1");

        // 추가
        Member member = new Member();
        member.setId(3L);
        member.setName("Mr Hong3");
        em.persist(member);

        // 삭제
        em.remove(member);
    }

    private static void jpql(EntityManager em) {
        // JPQL 사용 (SQL 을 추상화함) - 테이블이 아닌 객체를 대상으로 질의함
        List<Member> result = em.createQuery("select m from Member as m", Member.class)
                .setFirstResult(5)
                .setMaxResults(10)
                .getResultList();

        for (Member member : result) {
            System.out.println("member.name = " + member.getName());
        }
    }

    private static void persistStatus(EntityManager em) {
        // 비영속
        Member member = new Member();
        member.setId(123L);
        member.setName("Mr Hong");

        // 영속 (영속성 컨텍스트에 저장하고 DB 에 바로 저장 안함 -> commit 시 저장됨)
        em.persist(member);

        // 준영속 (영속성 컨텍스트에서 빼냄)
        em.detach(member);

        // 삭제 (DB 까지해서 전부 삭제)
        em.remove(member);
    }

    private static void sameEntity(EntityManager em) {
        Member member1 = em.find(Member.class, 1L); // SQL
        Member member2 = em.find(Member.class, 1L); // Cache

        System.out.println("result = " + (member1 == member2));
    }

    private static void lazyCreate(EntityManager em) {
        Member memberA = new Member(101L, "memberA");
        Member memberB = new Member(102L, "memberB");

        // 1차 캐시에는 우선 쌓이게 되지만 바로 INSERT 가 실행되지는 않는다.
        em.persist(memberA);
        em.persist(memberB);

        System.out.println("==============");

        // 나중에 트랜잭션 commit 을 하게 될 때 최종적으로 DB 로 위 Entity 들의 INSERT 가 실행된다.
    }

    private static void dirtyCheck(EntityManager em) {
        Member member = em.find(Member.class, 1L); // SQL
        member.setName("memberrrrrrrrA");

        // 하지 않아도 변경이 된다. (마치 자바 컬렉션처럼 되도록)
        // 트랜잭션 커밋 시 flush 되면서 스냅샷과 entity 를 서로 비교하여 변경된 값에 대해 UPDATE 문을 만들고 다시 실행하여 flush 를 통해 DB 업데이트를 한다.
        // flush -> 영속성 컨텍스트 변경 내용을 DB 에 반영하는 것을 말한다.
        //em.persist(member);

        System.out.println("==============");
    }

    private static void doFlush(EntityManager em) {
        Member member = new Member(4L, "doFlush");
        em.persist(member);
        em.flush();
    }
}
