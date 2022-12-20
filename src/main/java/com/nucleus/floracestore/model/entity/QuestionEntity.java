package com.nucleus.floracestore.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "question", nullable = false, unique = true)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user__id", referencedColumnName = "user_id")
    private UserEntity user;
    @ManyToOne(targetEntity=ProductEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private ProductEntity product;

    @OneToMany(targetEntity = AnswerEntity.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "question")
    private Set<AnswerEntity> answers = new HashSet<>();
}
