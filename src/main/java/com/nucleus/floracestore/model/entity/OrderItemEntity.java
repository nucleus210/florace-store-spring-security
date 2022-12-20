package com.nucleus.floracestore.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

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
    @JsonManagedReference(value = "order_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "_order_id", referencedColumnName = "order_id")
    private OrderEntity order;
    @JsonManagedReference(value = "product_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "_product_id", referencedColumnName = "product_id")
    private ProductEntity product;
    @JsonManagedReference(value = "order_item_status_code_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "_order_item_status_code_id", referencedColumnName = "order_item_status_code_id")
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
