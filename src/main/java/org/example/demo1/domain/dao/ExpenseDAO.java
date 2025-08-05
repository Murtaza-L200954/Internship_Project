package org.example.demo1.domain.dao;

import org.example.demo1.domain.models.Expenses;

import java.util.List;

public interface ExpenseDAO {
    void addExpense(Expenses expense);
    List<Expenses> getAllExpensesbyEmail(String email);
    Expenses getExpenseById(int id);
    void updateExpense(Expenses expense);
    void deleteExpenseById(int id);
}
