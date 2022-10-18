package kr.pe.karsei.jpabook.domain.scenario2;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter
@DiscriminatorValue("M")
public class Movie extends ItemGeneral {
    private String director;
    private String actor;
}
