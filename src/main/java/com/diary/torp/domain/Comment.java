package com.diary.torp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String comment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "comments"))
    private ToDoCard toDoCard;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "comment_writer"))
    private User writer;

    private boolean deleted = false;

    public Comment() {

    }

    public Comment(User loginUser, String comment) {
        this.comment = comment;
        this.writer = loginUser;
    }

    //card를 등록해주는 메서드
    public void registerIntoCard(ToDoCard card) {
        this.toDoCard = card;
    }

    //delete comment
    public void delete() {
        this.deleted = true;
    }

    //getter, setter

    public long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public ToDoCard getToDoCard() {
        return toDoCard;
    }
}
