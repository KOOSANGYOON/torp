package com.diary.torp.domain;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ToDoCard {
    @Id
    @GeneratedValue
    private long id;

    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20)
    private String title;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "toDoCards"))
    private ToDoDeck toDoDeck;

    @Column(nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "card_writer"))
    private User writer;

    // have to make due_date column

    @Column(nullable = true)
    private String label;

    @OneToMany(mappedBy = "toDoCard", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private List<Comment> comments = new ArrayList<Comment> ();

    private boolean deleted = false;

    public ToDoCard() {

    }

    public ToDoCard(User loginUser, String title) {
        this.writer = loginUser;
        this.title = title;
    }

    public void registerIntoDeck(ToDoDeck deck) {
        this.toDoDeck = deck;
    }

    //getter, setter


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ToDoDeck getToDoDeck() {
        return toDoDeck;
    }

    public String getDescription() {
        return description;
    }

    public User getWriter() {
        return writer;
    }

    public String getLabel() {
        return label;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
