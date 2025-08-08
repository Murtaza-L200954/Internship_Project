package org.example.demo1.domain.models;

import java.time.LocalDateTime;
import java.time.YearMonth;

public class MonthlyBudgets {
    private int id;
    private int user_id;
    private YearMonth month_year;
    private double salary;
    private double budget_limit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MonthlyBudgets() {}

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

    public YearMonth getMonth_year() {
        return month_year;
    }

    public void setMonth_year(YearMonth month_year) {
        this.month_year = month_year;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getBudget_limit() {
        return budget_limit;
    }

    public void setBudget_limit(double budget_limit) {
        this.budget_limit = budget_limit;
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
