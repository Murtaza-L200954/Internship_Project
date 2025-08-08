package org.example.demo1.domain.dao;

import org.example.demo1.domain.models.Expenses;

import java.util.List;

public interface ExpenseDAO {
    boolean addExpense(Expenses expense);
    List<Expenses> getAllExpensesbyEmail(String email);
    Expenses getExpenseById(int id);
    boolean updateExpense(Expenses expense);
    boolean deleteExpenseById(int id);
}
