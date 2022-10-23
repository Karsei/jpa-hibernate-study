package kr.pe.karsei.jpabook;

import kr.pe.karsei.jpabook.domain.*;
import kr.pe.karsei.jpabook.domain.cascade.Child;
import kr.pe.karsei.jpabook.domain.cascade.Parent;
import kr.pe.karsei.jpabook.domain.orphan.ChildOrphan;
import kr.pe.karsei.jpabook.domain.orphan.ParentOrphan;
import kr.pe.karsei.jpabook.domain.scenario.MemberEager;
import kr.pe.karsei.jpabook.domain.scenario.MemberLazy;
import kr.pe.karsei.jpabook.domain.scenario.TeamEager;
import kr.pe.karsei.jpabook.domain.scenario.TeamLazy;
import org.hibernate.Hibernate;

import javax.persistence.*;
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
            //(emf, em);
            //proxyLazy(em);
            //proxyEager(em);

            // cascade
            //cascade(em);
            //orphan(em);

            // Embedded
            //embeddedWarning(em);
            //valueTypeMapping(em);

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

    private static void valueTypeMapping(EntityManager em) {
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

    private static void embeddedWarning(EntityManager em) {
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

    private static void proxy(EntityManagerFactory emf, EntityManager em) {
        Member member = new Member();
        member.setName("hi");
        em.persist(member);

        em.flush();
        em.clear();

        Member ref = em.getReference(Member.class, member.getId());
        System.out.println("ref = " + ref.getClass()); // Proxy

        // 프록시의 초기화를 안했기 때문에 false 
        System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(ref));
        ref.getName(); // SELECT 실행
        // 프록시의 초기화를 했기 때문에 true 
        System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(ref));
        // 강제 초기화 (사실, JPA 표준에는 강제 초기화가 없다)
        Hibernate.initialize(ref);
    }

    private static void proxyLazy(EntityManager em) {
        TeamLazy team = new TeamLazy();
        team.setName("단풍나무");
        em.persist(team);

        MemberLazy member = new MemberLazy();
        member.setName("Mr. Hong");
        member.setTeam(team);
        em.persist(member);

        em.flush();
        em.clear();

        MemberLazy findMember = em.find(MemberLazy.class, member.getId());
        System.out.println("findMember = " + findMember.getTeam().getClass());

        System.out.println("=============");
        findMember.getTeam().getName();
        System.out.println("=============");
    }

    private static void proxyEager(EntityManager em) {
        TeamEager team = new TeamEager();
        team.setName("단풍나무");
        em.persist(team);

        TeamEager team2 = new TeamEager();
        team2.setName("소나무");
        em.persist(team2);

        MemberEager member = new MemberEager();
        member.setName("Mr. Hong");
        member.setTeam(team);
        em.persist(member);

        MemberEager member2 = new MemberEager();
        member2.setName("Mr. Back");
        member2.setTeam(team2);
        em.persist(member2);

        em.flush();
        em.clear();

        // SQL : SELECT * FROM MEMBER
        // SQL : SELECT * FROM TEAM WHERE TEAM_ID = xxx
        List<MemberEager> list = em.createQuery("select m from MemberEager m", MemberEager.class).getResultList();
    }

    private static void cascade(EntityManager em) {
        Child child1 = new Child();
        Child child2 = new Child();

        Parent parent = new Parent();
        parent.addChild(child1);
        parent.addChild(child2);

        // cascade = CascadeType.ALL 로 인해서 자동으로 모두 컨텍스트에 등록된다.
        em.persist(parent);
        //em.persist(child1);
        //em.persist(child2);
    }

    private static void orphan(EntityManager em) {
        ChildOrphan child1 = new ChildOrphan();
        ChildOrphan child2 = new ChildOrphan();

        ParentOrphan parent = new ParentOrphan();
        parent.addChild(child1);
        parent.addChild(child2);

        em.persist(parent);

        em.flush();
        em.clear();

        ParentOrphan findParent = em.find(ParentOrphan.class, parent.getId());
        findParent.getChildren().remove(0);
    }
}
