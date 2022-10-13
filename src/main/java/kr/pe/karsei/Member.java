package kr.pe.karsei;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {
    @Id // Primary Key
    private Long id;
    private String name;
}
