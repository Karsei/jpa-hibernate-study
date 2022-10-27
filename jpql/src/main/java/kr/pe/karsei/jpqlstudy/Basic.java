package kr.pe.karsei.jpqlstudy;

import kr.pe.karsei.jpqlstudy.domain.Member;
import kr.pe.karsei.jpqlstudy.dto.MemberDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class Basic {
    public static void checkQueryAndTypedQuery(EntityManager em) {
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
    }

    public static void checkBinding(EntityManager em) {
        // 바인딩
        Member singleBindResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        // 아래를 위처럼 체이닝으로 활용할 수도 있다.
        //TypedQuery<Member> query4 = em.createQuery("select m from Member m where m.username = :username", Member.class);
        //query4.setParameter("username", "member1");
        //Member singleBindResult = query4.getSingleResult();
        System.out.println("singleBindResult = " + singleBindResult.getUsername());
    }

    public static void checkSingleAndMultipleSelect(EntityManager em) {
        // 한 개 (정확히 하나)
        //Member singleResult = query1.getSingleResult();

        // 여러 값 조회
        List<MemberDTO> objectRawResultList = em.createQuery("select new kr.pe.karsei.jpqlstudy.dto.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                .getResultList();
        MemberDTO memberDTO = objectRawResultList.get(0);
        System.out.println("memberDTO.username = " + memberDTO.getUsername());
        System.out.println("memberDTO.age = " + memberDTO.getAge());
        // 아래를 위처럼 쉽게 쓸 수 있음
        //List<Object[]> objectRawResultList = em.createQuery("select m.username, m.age from Member m")
        //        .getResultList();
        //List objectRawResultList = em.createQuery("select m.username, m.age from Member m")
        //        .getResultList();
        //Object objectResultRaw = objectRawResultList.get(0);
        //Object[] objectResult = (Object[]) objectResultRaw;
        //System.out.println("username = " + objectResult[0]);
        //System.out.println("age = " + objectResult[1]);
    }

    public static void checkPaging(EntityManager em) {
        // 페이징
        List<Member> pagingList = em.createQuery("select m from Member m order by m.age desc", Member.class)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();
        System.out.println("pagingList.size = " + pagingList.size());
        for (Member member1 : pagingList) {
            System.out.println("member1 = " + member1);
        }
    }

    public static void checkJoin(EntityManager em) {
        // 조인
        String joinQuery = "select m from Member m inner join m.team t";
        List<Member> joinList = em.createQuery(joinQuery, Member.class)
                .getResultList();
    }

    public static void checkCaseWhen(EntityManager em) {
        // CASE-WHEN
        String query = """
                    select
                        case when m.age <= 10 then '어린이'
                             when m.age >= 60 then '경로'
                             else '일반'
                        end
                    from Member m
                    """;
        List<String> caseWhenList = em.createQuery(query, String.class).getResultList();
        for (String s : caseWhenList) {
            System.out.println("s = " + s);
        }
    }
}
