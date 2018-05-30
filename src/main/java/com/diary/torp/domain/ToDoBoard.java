package com.diary.torp.domain;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ToDoBoard {
    @Id
    @GeneratedValue
    private long id;

    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20)
    private String title;

    @OneToMany(mappedBy = "toDoBoard", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private List<ToDoDeck> toDoDecks = new ArrayList<ToDoDeck>();

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "todo_writer"))
    private User writer;

    private boolean deleted = false;

    public ToDoBoard() {

    }

    public ToDoBoard(User writer, String title) {
        this.title = title;
        this.writer = writer;
    }

    //getter, setter
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getWriter() {
        return writer;
    }

    public List<ToDoDeck> getToDoDecks() {
        return toDoDecks;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "ToDoBoard{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", toDoDecks=" + toDoDecks +
                ", writer=" + writer +
                ", deleted=" + deleted +
                '}';
    }
}
