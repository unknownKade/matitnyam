package com.wandted.matitnyam.review.domain;

import com.wandted.matitnyam.restaurant.domain.Restaurant;
import com.wandted.matitnyam.review.dto.ReviewResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_review")
@Entity
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("식당")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;

    @Comment("평점")
    @Column(nullable = false)
    private Double rate;

    @Comment("후기 내용")
    @Column(nullable = false)
    private String comment;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    //TODO: add username to Review DTO
    public ReviewResponse toDto(){
        return new ReviewResponse(id, rate, comment, createdBy, lastModifiedDate, createdDate != lastModifiedDate);
    }

    public void update(String comment){
        this.comment = comment;
    }
}
