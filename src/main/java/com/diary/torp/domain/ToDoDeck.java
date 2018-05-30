package com.diary.torp.domain;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ToDoDeck {
    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20)
    private String title;

    @OneToMany(mappedBy = "toDoDeck", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private List<ToDoCard> toDoCards = new ArrayList<ToDoCard>();

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "toDoDecks"))
    private ToDoBoard toDoBoard;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "deck_writer"))
    private User writer;

    private boolean deleted = false;

    public ToDoDeck() {

    }

    public ToDoDeck(User loginUser, String title) {
        this.writer = loginUser;
        this.title = title;
    }

    //getter(), setter()

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<ToDoCard> getToDoCards() {
        return toDoCards;
    }

    public ToDoBoard getToDoBoard() {
        return toDoBoard;
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
