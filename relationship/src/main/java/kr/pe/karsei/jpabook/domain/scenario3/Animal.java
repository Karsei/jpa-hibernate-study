package kr.pe.karsei.jpabook.domain.scenario3;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Animal extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
