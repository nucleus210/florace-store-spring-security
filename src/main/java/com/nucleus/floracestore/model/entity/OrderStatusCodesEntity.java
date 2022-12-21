package com.nucleus.floracestore.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "order_status_code")
public class OrderStatusCodesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_status_code_id")
    private Long orderStatusCodeId;
    @Column(name = "order_status_code", nullable = false)
    private String statusCode;
    // eg. Canceled, Completed
    @Column(name = "order_status_code_description", columnDefinition = "TEXT", nullable = false)
    private String statusDescription;
    @JsonManagedReference(value = "order-codes")
    @OneToMany(targetEntity = OrderEntity.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "orderStatusCode")
    private Set<OrderEntity> orders;
}
