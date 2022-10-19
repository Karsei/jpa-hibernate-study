package kr.pe.karsei.jpabook;

import kr.pe.karsei.jpabook.domain.Item;
import kr.pe.karsei.jpabook.domain.Movie;

import javax.persistence.EntityManager;

public class CheckInheritance {
    public static void inheritance(EntityManager em) {
        Movie movie = new Movie();
        movie.setDirector("홍길동");
        movie.setActor("고길동");
        movie.setName("바람과 함께 사라지다.");
        movie.setPrice(10000);

        em.persist(movie);

        em.flush();
        em.clear();

        Movie findMovie = em.find(Movie.class, movie.getId());
        System.out.println("findMovie = " + findMovie);
    }

    public static void inheritanceTablePerClassUnion(EntityManager em) {
        Movie movie = new Movie();
        movie.setDirector("홍길동");
        movie.setActor("고길동");
        movie.setName("바람과 함께 사라지다.");
        movie.setPrice(10000);

        em.persist(movie);

        em.flush();
        em.clear();

        Item item = em.find(Item.class, movie.getId());
        System.out.println("Item = " + item);
    }
}
