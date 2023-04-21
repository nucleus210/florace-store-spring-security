package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "contacts")
public class ContactEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO,
          generator="native"
  )
  @GenericGenerator(
          name = "native",
          strategy = "native"
  )
  @Column(name = "contact_id", nullable = false)
  private Long contactId;
  @Column(name = "first_name", nullable = false)
  private String firstName;
  @Column(name = "last_name", nullable = false)
  private String lastName;
  @Column(name = "email_name", nullable = false)
  private String email;
  @Column(name = "phone")
  private String phone;
  @Column(name = "message", nullable = false)
  private String message;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "created_date", nullable = false)
  private Date createdDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "response_date")
  private Date responseDate;
}
