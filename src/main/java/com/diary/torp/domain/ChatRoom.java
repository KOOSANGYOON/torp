package com.diary.torp.domain;

import javax.persistence.*;

@Entity
public class ChatRoom {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String title;

    @OneToOne
    private User maker;

    private boolean isPublic = true;

    private boolean deleted = false;

    public ChatRoom() {

    }

    public ChatRoom(String title, User user) {
        this.title = title;
        this.maker = user;
    }

    //getter methods
    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public User getMaker() {
        return maker;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public boolean isDeleted() {
        return deleted;
    }
}
