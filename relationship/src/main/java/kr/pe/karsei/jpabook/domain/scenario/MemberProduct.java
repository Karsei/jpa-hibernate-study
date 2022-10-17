package kr.pe.karsei.jpabook.domain.scenario;

import javax.persistence.*;

@Entity
public class MemberProduct {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member1 member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
}
