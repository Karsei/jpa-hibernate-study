package kr.pe.karsei.jpqlstudy;

import kr.pe.karsei.jpqlstudy.domain.Member;
import kr.pe.karsei.jpqlstudy.domain.Team;

import javax.persistence.EntityManager;
import java.util.List;

public class Advanced {
    public static void checkFetchJoin(EntityManager em) {
        // Fetch Join
        String fetchJoinQuery = "select m from Member m where m.team is not null";
        List<Member> fetchJoinList = em.createQuery(fetchJoinQuery, Member.class)
                .getResultList();
        for (Member member : fetchJoinList) {
            System.out.println("member = " + member + ", " + member.getTeam().getName());
        }
    }

    public static void checkFetchJoinWithCollection(EntityManager em) {
        // Collection Fetch Join
        String colJoinQuery = "select t FROM Team t JOIN FETCH t.members";
        List<Team> colJoinList = em.createQuery(colJoinQuery, Team.class)
                .getResultList();

        System.out.println("result = " + colJoinList.size());

        for (Team team : colJoinList) {
            System.out.println("team = " + team.getName() + ", members = " + team.getMembers().size());
            for (Member member : team.getMembers()) {
                System.out.println("--> member = " + member);
            }
        }
    }

    public static void checkFetchJoinWithPaging(EntityManager em) {
        // Collection Fetch Join
        String pagingQuery = "select t FROM Team t";
        List<Team> pagingQueryList = em.createQuery(pagingQuery, Team.class)
                .setFirstResult(0)
                .setMaxResults(3)
                .getResultList();

        System.out.println("result = " + pagingQueryList.size());

        for (Team team : pagingQueryList) {
            System.out.println("team = " + team.getName() + ", members = " + team.getMembers().size());
            for (Member member : team.getMembers()) {
                System.out.println("--> member = " + member);
            }
        }
    }
}
