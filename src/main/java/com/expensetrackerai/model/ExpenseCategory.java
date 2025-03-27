package com.expensetrackerai.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ExpenseCategory() {
    }

    public ExpenseCategory(Long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long category_id) {
        this.id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String category_name) {
        this.name = category_name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserId(Long userId) {
        this.user = new User();
        this.user.setId(userId);
    }
}