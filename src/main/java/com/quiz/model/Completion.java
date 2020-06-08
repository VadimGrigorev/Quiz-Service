package com.quiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="completion")
public class Completion {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private int completionId;

    @JsonIgnore
    @Column
    private int userId;

    //question id
    @Column(name = "question_id")
    private int id;

    @Column
    private LocalDateTime completedAt;

    public Completion() {
    }

    public Completion(int userId, int questionId, LocalDateTime completedAt) {
        this.userId = userId;
        this.id = questionId;
        this.completedAt = completedAt;
    }

    public int getCompletionId() {
        return completionId;
    }

    public void setCompletionId(int completionId) {
        this.completionId = completionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
