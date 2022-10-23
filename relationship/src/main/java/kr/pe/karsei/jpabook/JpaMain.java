package kr.pe.karsei.jpabook;

import javax.persistence.*;

import static kr.pe.karsei.jpabook.CheckCascadeOrphan.*;
import static kr.pe.karsei.jpabook.CheckInheritance.*;
import static kr.pe.karsei.jpabook.CheckManyToOne.*;
import static kr.pe.karsei.jpabook.CheckOneToMany.*;
import static kr.pe.karsei.jpabook.CheckProxy.*;
import static kr.pe.karsei.jpabook.JPQLIntroduce.*;

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
            // 일대다 관련 시나리오 예시
            //checkManyToOne(em);
            //checkOneToMany(em);
            //checkOneToManyCaution(em);
            //checkOneToManyCaution2(em);
            //oneToMany(em);

            // 상속
            //inheritance(em);
            //inheritanceTablePerClassUnion(em);

            // 프록시
            //proxyLazy(em);
            //proxyEager(em);

            // cascade
            //cascade(em);
            //orphan(em);

            // Embedded
            //embeddedWarning(em);
            //valueTypeMapping(em);

            // JPQL
            //jpqlIntroduce(em);

            // 트랜잭션 - 종료
            tx.commit();
        }
        catch (Exception e) {
            // 트랜잭션 - 롤백
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
        }

        emf.close();
    }
}
