package com.nucleus.floracestore.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long orderItemId;
    @JsonBackReference(value = "order-items")
    @ManyToOne(targetEntity=OrderEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name="product_id", referencedColumnName = "product_id")
    private ProductEntity product;

    @JsonBackReference(value = "order-codes")
    @ManyToOne(targetEntity=OrderItemsStatusCodesEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_status_code_id")
    private OrderItemsStatusCodesEntity orderItemStatusCode;

    @Column(name = "order_item_quantity", nullable = false)
    private Integer orderItemQuantity;

    @Column(name = "order_item_price", nullable = false)
    private BigDecimal orderItemPrice;

    @Column(name = "rma_number", nullable = false)
    private String rmaNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "rma_issued_by")
    private Date rmaIssuedBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "rma_issued_date")
    private Date rmaIssuedData;

    @Column(name = "other_order_item_details", columnDefinition = "TEXT")
    private String orderItemDetails;


}
