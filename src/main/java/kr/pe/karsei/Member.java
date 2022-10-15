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

    // JPA 는 기본 생성자가 있어야 한다.
    public Member() {
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
