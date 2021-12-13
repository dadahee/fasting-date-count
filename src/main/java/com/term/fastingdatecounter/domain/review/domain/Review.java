package com.term.fastingdatecounter.domain.review.domain;

import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.global.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Getter
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDate date;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @ColumnDefault("1")
    @Column(nullable = false)
    private boolean fasted;

    @Builder
    public Review(Long id, Food food, LocalDate date, String title, String content, boolean fasted) {
        this.id = id;
        this.food = food;
        this.date = date;
        this.title = title;
        this.content = content;
        this.fasted = fasted;
    }

    public void updateReview(LocalDate date, String title, String content, boolean fasted) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.fasted = fasted;
    }
}