package kr.pe.karsei.jpabook.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId;

    // Spring 에서는 underscore 방식과 camelCase 를 자동으로 변환해줌
    // 순수 Hibernate 에서는 그대로 이름을 가져가는게 원칙
    private LocalDateTime orderDate; // order_date, ORDER_DATE

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}