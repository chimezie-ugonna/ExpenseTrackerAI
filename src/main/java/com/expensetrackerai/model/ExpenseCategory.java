package com.expensetrackerai.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class ExpenseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    @Column(name = "category_name")
    private String category_name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // This is the relation to the User entity

    public ExpenseCategory() {}

    public ExpenseCategory(String category_name, User user) {
        this.category_name = category_name;
        this.user = user;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
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