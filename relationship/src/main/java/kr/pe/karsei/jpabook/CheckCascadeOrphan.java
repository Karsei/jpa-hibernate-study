package kr.pe.karsei.jpabook;

import kr.pe.karsei.jpabook.domain.cascade.Child;
import kr.pe.karsei.jpabook.domain.cascade.Parent;
import kr.pe.karsei.jpabook.domain.orphan.ChildOrphan;
import kr.pe.karsei.jpabook.domain.orphan.ParentOrphan;

import javax.persistence.EntityManager;

public class CheckCascadeOrphan {
    public static void cascade(EntityManager em) {
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

    public static void orphan(EntityManager em) {
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
