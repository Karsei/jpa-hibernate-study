package kr.pe.karsei.jpqlstudy;

import kr.pe.karsei.jpqlstudy.domain.Member;
import kr.pe.karsei.jpqlstudy.domain.Team;

import javax.persistence.*;

import static kr.pe.karsei.jpqlstudy.Basic.*;
import static kr.pe.karsei.jpqlstudy.Advanced.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gazuua");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 초기화
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);
            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);
            for (int i = 0; i < 30; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                if (i % 7 == 0) member.setTeam(teamA);
                if (i % 11 == 0) member.setTeam(teamB);
                em.persist(member);
            }

            //checkQueryAndTypedQuery(em);
            //checkBinding(em);
            //checkSingleAndMultipleSelect(em);
            //checkPaging(em);
            //checkJoin(em);
            //checkCaseWhen(em);
            //checkFetchJoin(em);
            //checkFetchJoinWithCollection(em);
            checkFetchJoinWithPaging(em);

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
