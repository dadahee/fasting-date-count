package com.term.fastingdatecounter.api.review.domain;

import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.global.domain.BaseTimeEntity;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Getter
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @ColumnDefault("1")
    @Column(nullable = false)
    private boolean fasted;

}