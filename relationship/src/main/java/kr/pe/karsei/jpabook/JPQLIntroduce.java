package kr.pe.karsei.jpabook;

import kr.pe.karsei.jpabook.domain.Address;
import kr.pe.karsei.jpabook.domain.AddressEntity;
import kr.pe.karsei.jpabook.domain.Member;
import kr.pe.karsei.jpabook.domain.MemberEmbeddable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JPQLIntroduce {
    public static void embeddedWarning(EntityManager em) {
        Address address = new Address("city", "street", "10000");

        MemberEmbeddable member1 = new MemberEmbeddable();
        member1.setName("Hone");
        member1.setHomeAddress(address);
        em.persist(member1);

        Address copiedAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());

        MemberEmbeddable member2 = new MemberEmbeddable();
        member2.setName("Hone");
        member2.setHomeAddress(copiedAddress);
        em.persist(member2);

        //member1.getHomeAddress().setCity("newCity");
    }

    public static void valueTypeMapping(EntityManager em) {
        MemberEmbeddable member = new MemberEmbeddable();
        member.setName("member1");
        member.setHomeAddress(new Address("city1", "street1", "zipcode1"));

        member.getFavoriteFoods().add("고기");
        member.getFavoriteFoods().add("백반");
        member.getFavoriteFoods().add("치킨");

        member.getAddressHistory().add(new AddressEntity("old1", "street1", "zipcode1"));
        member.getAddressHistory().add(new AddressEntity("old2", "street1", "zipcode1"));

        em.persist(member);

        em.flush();
        em.clear();

        System.out.println("========================");
        MemberEmbeddable findMember = em.find(MemberEmbeddable.class, member.getId());

        // homeCity -> newCity
        //findMember.getHomeAddress().setCity("newCity"); // 이렇게 하면 안 된다.
        // 아예 갈아끼워야 한다.
//            findMember.setHomeAddress(new Address("newCity", findMember.getHomeAddress().getStreet(), findMember.getHomeAddress().getZipcode()));
//            // 컬렉션의 '치킨' -> '한식'으로 바꾸려면?
//            findMember.getFavoriteFoods().remove("치킨");
//            findMember.getFavoriteFoods().add("한식");

        // 리스트의 경우 컬렉션 내의 equals 를 이용해서 지운다.
//            findMember.getAddressHistory().remove(new AddressEntity("old1", "street1", "zipcode1"));
//            findMember.getAddressHistory().add(new AddressEntity("newCity1", "street1", "zipcode1"));
    }

    public static void jpqlIntroduce(EntityManager em) {
        // JPQL
        List<Member> list = em.createQuery(
                        "select m from Member m where m.name like '%kim%'",
                        Member.class)
                .getResultList();

        // Criteria
        // JPQL 은 단순한 문자열이기 때문에 동적 쿼리를 만들기가 어렵기 때문에 Criteria 는 이를 쉽게 해준다.
        // 컴파일 오류나 동적 쿼리에 대한 코드를 작성하기 쉽다.
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);

        Root<Member> m = query.from(Member.class);

        CriteriaQuery<Member> cq = query
                .select(m)
                .where(cb.equal(m.get("name"), "kim"));
        List<Member> list2 = em.createQuery(cq).getResultList();

        // Native Query
        List<Member> list3 = em.createNativeQuery(
                        "select member_id, city, street, zipcode from member",
                        Member.class)
                .getResultList();
    }
}
