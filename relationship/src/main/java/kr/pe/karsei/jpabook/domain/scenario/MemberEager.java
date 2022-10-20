package kr.pe.karsei.jpabook.domain.scenario;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class MemberEager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEAM_ID")
    private TeamEager team;

    //@OneToOne
    //@JoinColumn(name = "LOCKER_ID")
    //private Locker locker;

    @Override
    public String toString() {
        return "MemberProxy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", team=" + team +
                '}';
    }
}