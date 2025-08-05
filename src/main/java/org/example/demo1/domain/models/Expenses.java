package org.example.demo1.domain.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Expenses {
    private int id;
    private int user_id;
    private double amount;
    private int category_id;
    private LocalDate expense_date;
    private String notes;
    private String file_path;
    private boolean is_recurring;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Expenses() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public LocalDate getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(LocalDate expense_date) {
        this.expense_date = expense_date;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isIs_recurring() {
        return is_recurring;
    }

    public void setIs_recurring(boolean is_recurring) {
        this.is_recurring = is_recurring;
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
