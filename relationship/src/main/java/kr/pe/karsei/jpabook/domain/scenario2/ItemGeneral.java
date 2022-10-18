package kr.pe.karsei.jpabook.domain.scenario2;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "DTYPE")
public abstract class ItemGeneral {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;
}
