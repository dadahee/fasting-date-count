package com.term.fastingdatecounter.api.review.domain;

import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.global.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;


@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
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
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @ColumnDefault("1")
    @Column(nullable = false)
    private boolean fasted;

    @Builder
    public Review(Food food, Date date, String title, String content, boolean fasted) {
        this.id = id;
        this.food = food;
        this.date = date;
        this.title = title;
        this.content = content;
        this.fasted = fasted;
    }
}