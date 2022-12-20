package com.nucleus.floracestore.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "answers")
@NoArgsConstructor
@AllArgsConstructor
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "answer", nullable = false, unique = true)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", referencedColumnName = "user_id")
    private UserEntity user;
    @ManyToOne(targetEntity=QuestionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="question_Id")
    private QuestionEntity question;

}
