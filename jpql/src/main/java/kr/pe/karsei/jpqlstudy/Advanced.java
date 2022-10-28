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
        String pagingQuery = "select t FROM Team t";
        List<Team> pagingQueryList = em.createQuery(pagingQuery, Team.class)
                .setFirstResult(0)
                .setMaxResults(2)
                .getResultList();

        System.out.println("result = " + pagingQueryList.size());

        for (Team team : pagingQueryList) {
            System.out.println("team = " + team.getName() + ", members = " + team.getMembers().size());
            for (Member member : team.getMembers()) {
                System.out.println("--> member = " + member);
            }
        }
    }

    public static void checkEntityDirectUsage(EntityManager em, Member sampleMember, Team sampleTeam) {
        // 기본
        String query = "select m FROM Member m where m = :member";
        Member findMember = em.createQuery(query, Member.class)
                .setParameter("member", sampleMember)
                .getSingleResult();

        System.out.println("findMember = " + findMember);

        // 외래
        String query2 = "select m FROM Member m where m.team = :team";
        List<Member> findmembers = em.createQuery(query2, Member.class)
                .setParameter("team", sampleTeam)
                .getResultList();

        for (Member member : findmembers) {
            System.out.println("members = " + member);
        }
    }

    public static void checkNamedQuery(EntityManager em) {
        Member result = em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", "specialMember")
                .getSingleResult();

        System.out.println("result = " + result);
    }

    public static void checkBulkUpdate(EntityManager em, Member sampleMember) {
        int resultCount = em.createQuery("update Member m set m.age = 20")
                .executeUpdate();

        System.out.println("resultCount = " + resultCount);

        // 트랜잭션 커밋하기 전에 위의 Member 들을 조회하면 age 는 0으로 되어 있고
        // 반영이 되지 않은 데이터를 가져오므로 주의해야 한다.
        System.out.println("sampleMember = " + sampleMember.getAge()); // 0
        Member findMember = em.find(Member.class, sampleMember.getId());
        System.out.println("findMember = " + findMember.getAge()); // 0

        // 영속성 컨텍스트를 비워주어야 비로소 제대로 가져온다.
        em.clear();
        Member findMember2 = em.find(Member.class, sampleMember.getId());
        System.out.println("findMember = " + findMember2.getAge()); // 20
    }
}
