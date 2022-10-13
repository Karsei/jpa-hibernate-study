package kr.pe.karsei;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        // persistence.xml 에 있는 이름
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gazuua");
        EntityManager em = emf.createEntityManager();
        em.close();
        emf.close();
    }
}
