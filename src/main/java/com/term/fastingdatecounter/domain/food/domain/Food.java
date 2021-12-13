package com.term.fastingdatecounter.domain.food.domain;

import com.term.fastingdatecounter.domain.user.domain.User;
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
public class Food extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "start_date", columnDefinition = "DATE DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDate startDate;

    @ColumnDefault("0")
    @Column(name = "day_count", nullable = false)
    private Long dayCount;

    @Builder
    public Food(Long id, User user, String name, LocalDate startDate, Long dayCount) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.startDate = startDate;
        this.dayCount = dayCount;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void updateStartDate(LocalDate startDate){
        this.startDate = startDate;
    }

    public void updateDayCount(Long dayCount) {
        this.dayCount = dayCount;
    }
}
