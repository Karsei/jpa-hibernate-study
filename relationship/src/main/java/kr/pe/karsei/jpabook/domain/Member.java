package kr.pe.karsei.jpabook.domain;

import kr.pe.karsei.jpabook.CheckOneToMany;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID")
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    private String name;

    private String city;

    private String street;

    private String zipcode;
}