package kr.pe.karsei.jpqlstudy;

import kr.pe.karsei.jpqlstudy.domain.Member;
import kr.pe.karsei.jpqlstudy.dto.MemberDTO;
import kr.pe.karsei.jpqlstudy.domain.Team;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gazuua");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 초기화
            Team teamA = new Team();
            teamA.setName("해바라기반");
            em.persist(teamA);
            Team teamB = new Team();
            teamB.setName("장미반");
            em.persist(teamB);
            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                if (i % 7 == 0) member.setTeam(teamA);
                if (i % 11 == 0) member.setTeam(teamB);
                em.persist(member);
            }

            // 반환 타입이 정해져 있는 경우
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            // 반환 타입이 정해져 있지 않은 경우
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            // 여러 개
            List<Member> multipleResult = query1.getResultList();
            for (Member member1 : multipleResult) {
                System.out.println("member1 = " + member1);
            }

            // 한 개 (정확히 하나)
            Member singleResult = query1.getSingleResult();

            // 바인딩
            Member singleBindResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            // 아래를 위처럼 체이닝으로 활용할 수도 있다.
            //TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.username = :username", Member.class);
            //query4.setParameter("username", "member1");
            //Member singleBindResult = query4.getSingleResult();
            System.out.println("singleBindResult = " + singleBindResult.getUsername());

            // 여러 값 조회
            List<MemberDTO> objectRawResultList = em.createQuery("select new kr.pe.karsei.jpqlstudy.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            MemberDTO memberDTO = objectRawResultList.get(0);
            System.out.println("memberDTO.username = " + memberDTO.getUsername());
            System.out.println("memberDTO.age = " + memberDTO.getAge());
            // 아래를 위처럼 쉽게 쓸 수 있음
            //List<Object[]> objectRawResultList = em.createQuery("select m.username, m.age from Member m")
            //        .getResultList();
            //List objectRawResultList = em.createQuery("select m.username, m.age from Member m")
            //        .getResultList();
            Object objectResultRaw = objectRawResultList.get(0);
            Object[] objectResult = (Object[]) objectResultRaw;
            System.out.println("username = " + objectResult[0]);
            System.out.println("age = " + objectResult[1]);

            // 페이징
            List<Member> pagingList = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();
            System.out.println("pagingList.size = " + pagingList.size());
            for (Member member1 : pagingList) {
                System.out.println("member1 = " + member1);
            }
            
            // 조인
            String joinQuery = "select m from Member m inner join m.team t";
            List<Member> joinList = em.createQuery(joinQuery, Member.class)
                    .getResultList();

            tx.commit();
        }
        catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        emf.close();
    }
}
