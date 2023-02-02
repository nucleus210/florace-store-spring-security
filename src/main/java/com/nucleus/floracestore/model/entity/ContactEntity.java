package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "contacts")
public class ContactEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "contact_id", nullable = false)
  private Long contactId;
  @Column(name = "first_name", nullable = false)
  private String fistName;
  @Column(name = "last_name", nullable = false)
  private String lastName;
  @Column(name = "email_name", nullable = false)
  private String email;
  @Column(name = "phone")
  private String phone;
  @Column(name = "comment", nullable = false)
  private String comment;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "created_date", nullable = false)
  private Date createdDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "response_date", nullable = false)
  private Date responseDate;
}
