package org.example.demo1.domain.service;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.ExpenseDAO;
import org.example.demo1.domain.daoImpl.ExpenseDAOImpl;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.models.Expenses;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ExpenseService {

    public ServiceResponse<String> addExpense(Expenses expense) throws SQLException{
        Connection conn = null;
        conn = DBUtil.getConnection();
        ExpenseDAO expenseDAO = new ExpenseDAOImpl(conn);

        if (expense.getUser_id() <= 0 || expense.getAmount() <= 0 || expense.getExpense_date() == null) {
            return ServiceResponse.error("Invalid expense data. User ID, amount, and expense date are required.",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isAdded = expenseDAO.addExpense(expense);

        if (isAdded) {
            return ServiceResponse.success("Success", "Expense added successfully");
        } else {
            return ServiceResponse.error("Failed to add expense",
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    public ServiceResponse<List<Expenses>> getAllExpensesByEmail(String email) throws SQLException {
        Connection conn = null;
        conn = DBUtil.getConnection();
        ExpenseDAO expenseDAO = new ExpenseDAOImpl(conn);

        if (email == null || email.trim().isEmpty()) {
            return ServiceResponse.error("Email is required",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        List<Expenses> expenses = expenseDAO.getAllExpensesbyEmail(email);

        if (expenses != null && !expenses.isEmpty()) {
            return ServiceResponse.success(expenses, "Expenses retrieved successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("No expenses found for this user",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<Expenses> getExpenseById(int id) throws SQLException {
        Connection conn = null;
        conn = DBUtil.getConnection();
        ExpenseDAO expenseDAO = new ExpenseDAOImpl(conn);

        if (id <= 0) {
            return ServiceResponse.error("Invalid expense ID",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        Expenses expense = expenseDAO.getExpenseById(id);

        if (expense != null) {
            return ServiceResponse.success(expense, "Expense retrieved successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Expense not found",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<String> updateExpense(Expenses expense) throws SQLException {
        Connection conn = null;
        conn = DBUtil.getConnection();
        ExpenseDAO expenseDAO = new ExpenseDAOImpl(conn);

        if (expense.getId() <= 0 || expense.getUser_id() <= 0 || expense.getAmount() <= 0 || expense.getExpense_date() == null) {
            return ServiceResponse.error("Invalid expense data. ID, user ID, amount, and expense date are required.",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isUpdated = expenseDAO.updateExpense(expense);

        if (isUpdated) {
            return ServiceResponse.success("Success", "Expense updated successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Expense not found or update failed",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<String> deleteExpense(int id) throws SQLException {
        Connection conn = null;
        conn = DBUtil.getConnection();
        ExpenseDAO expenseDAO = new ExpenseDAOImpl(conn);

        if (id <= 0) {
            return ServiceResponse.error("Invalid expense ID",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isDeleted = expenseDAO.deleteExpenseById(id);

        if (isDeleted) {
            return ServiceResponse.success("Success", "Expense deleted successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Expense not found or delete failed",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }
}