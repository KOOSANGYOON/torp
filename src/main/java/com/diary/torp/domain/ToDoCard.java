package com.diary.torp.domain;

import com.diary.torp.UnAuthenticationException;
import com.diary.torp.web.HomeController;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ToDoCard {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

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

    //deck 에 추가하기
    public void registerIntoDeck(ToDoDeck deck) {
        this.toDoDeck = deck;
    }

    //description 변경
    public void editDescription(User loginUser, String newDescription) throws UnAuthenticationException {
        if (!isOwner(loginUser)) {
            log.debug("--- writer : " + this.writer);
            log.debug("--- loginUser : " + loginUser);
            throw new UnAuthenticationException();
        }
        this.description = newDescription;
    }

    //comment 추가
    public void addComment(User loginUser, Comment newComment) throws UnAuthenticationException {
        if (!isOwner(loginUser)) {
            log.debug("--- writer : " + this.writer);
            log.debug("--- loginUser : " + loginUser);
            throw new UnAuthenticationException();
        }
        newComment.registerIntoCard(this);
        this.comments.add(newComment);
    }

    public void editTitle(User loginUser, String newTitle) throws UnAuthenticationException {
        if (!this.isOwner(loginUser)) {
            throw new UnAuthenticationException();
        }

        this.title = newTitle;
    }

    //작성자 확인
    public boolean isOwner(User loginUser) {
        return this.writer.equals(loginUser);
    }

    //card 삭제
    public void delete(User loginUser) throws UnAuthenticationException {
        if (!this.isOwner(loginUser)) {
            throw new UnAuthenticationException();
        }
        this.deleted = true;
        for (Comment comment : this.comments) {
            comment.delete();
        }
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
