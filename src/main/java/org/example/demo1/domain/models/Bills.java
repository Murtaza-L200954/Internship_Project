package org.example.domain.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Bills {
    private int id;
    private int user_Id;
    private int expenseid;
    private String title;
    private LocalDate due_date;
    private double amount;
    private boolean is_paid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Bills(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user_Id;
    }

    public void setUserId(int userId) {
        this.user_Id = userId;
    }

    public int getExpenseid() {
        return expenseid;
    }

    public void setExpenseid(int expenseid) {
        this.expenseid = expenseid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
