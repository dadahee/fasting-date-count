package com.term.fastingdatecounter.api.food.domain;

import com.term.fastingdatecounter.api.user.domain.User;
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
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @ColumnDefault("0")
    @Column(name = "day_count", nullable = false)
    private int dayCount;

    @Builder
    public Food(User user, String name, Date startDate, int dayCount) {
        this.user = user;
        this.name = name;
        this.startDate = startDate;
        this.dayCount = dayCount;
    }
}
