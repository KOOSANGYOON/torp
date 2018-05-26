//package com.diary.torp.domain;
//
//import javax.persistence.*;
//import javax.validation.constraints.Size;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//public class ToDoCard {
//    @Id
//    @GeneratedValue
//    private long id;
//
//    @Size(min = 3, max = 20)
//    @Column(nullable = false, length = 20)
//    private String title;
//
//    @Column(nullable = true)
//    private String description;
//
//    @ManyToOne
//    @JoinColumn(foreignKey = @ForeignKey(name = "card_writer"))
//    private User writer;
//
//    // have to make due_date column
//
//    @Column(nullable = true)
//    private String label;
//
//    @Column
//    private List<Comment> comments = new ArrayList<Comment> ();
//
//    private boolean deleted = false;
//
//    public ToDoCard() {
//
//    }
//
//    public ToDoCard(User loginUser, String title) {
//        this.writer = loginUser;
//        this.title = title;
//    }
//
//    //getter, setter
//    public long getId() {
//        return id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public User getWriter() {
//        return writer;
//    }
//
//    public boolean isDeleted() {
//        return deleted;
//    }
//
//    public List<Comment> getComments() {
//        return comments;
//    }
//}
