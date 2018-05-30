package com.diary.torp.domain;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String comment;

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
}
