package kr.pe.karsei.jpabook.domain.scenario;

import javax.persistence.*;

@Entity
public class Locker {
    @Id
    @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;
    private String name;

    @OneToOne(mappedBy = "locker")
    private Member1 member;
}
