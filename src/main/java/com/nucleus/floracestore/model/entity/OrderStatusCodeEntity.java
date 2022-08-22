package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "order_status_code")
public class OrderStatusCodeEntity {
    @Id
    @Column(name = "order_status_code_id")
    private Long orderStatusCodeId;
    @Column(name = "order_status_code")
    private String statusCode;
    // eg. Canceled, Completed
    @Column(name = "order_status_code_description", columnDefinition = "TEXT", nullable = false)
    private String statusDescription;
}
