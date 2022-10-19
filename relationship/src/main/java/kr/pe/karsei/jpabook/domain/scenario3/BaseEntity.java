package kr.pe.karsei.jpabook.domain.scenario3;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class BaseEntity {
    @Column(name = "USER_NAME")
    private String name;
    private LocalDateTime createdAt;
}
