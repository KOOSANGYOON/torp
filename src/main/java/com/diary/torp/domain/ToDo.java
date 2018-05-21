package com.diary.torp.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class ToDo {
    @Id
    @GeneratedValue
    private long id;

    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "todo_writer"))
    private User writer;

    private boolean deleted = false;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dueDate=" + dueDate +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                '}';
    }
}
