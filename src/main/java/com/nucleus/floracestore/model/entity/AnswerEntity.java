package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "answers")
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "answer", nullable = false, unique = true)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", referencedColumnName = "user_id")
    private UserEntity user;
//    @JsonManagedReference(value = "question-answers")
    @ManyToOne(targetEntity=QuestionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="question_id")
    private QuestionEntity question;

}
