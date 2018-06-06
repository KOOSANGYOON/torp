package com.diary.torp.domain;

import com.diary.torp.UnAuthenticationException;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
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

    //deck 추가
    public void addDeck(ToDoDeck newDeck) {
        this.toDoDecks.add(newDeck);
    }

    //접근성 확인
    public boolean isOwner(User loginUser) {
        return this.writer.equals(loginUser);
    }

    //getter, setter

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<ToDoDeck> getToDoDecks() {
        return toDoDecks;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    //toString()

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
