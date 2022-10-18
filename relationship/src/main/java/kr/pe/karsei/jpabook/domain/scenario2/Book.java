package kr.pe.karsei.jpabook.domain.scenario2;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter
@DiscriminatorValue("B")
public class Book extends ItemGeneral {
    private String author;
    private String isbn;
}
