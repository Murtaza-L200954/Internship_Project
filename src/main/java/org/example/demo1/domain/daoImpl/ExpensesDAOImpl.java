package org.example.demo1.domain.daoImpl;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.ExpenseDAO;
import org.example.domain.models.Expenses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ExpensesDAOImpl implements ExpenseDAO {
    @Override
    public void addExpense(Expenses expense) throws SQLException {
        String sql = "INSERT INTO expenses (user_id, amount, category_id, expense_date, notes, is_recurring) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, expense.getUserId());
            stmt.setBigDecimal(2, expense.getAmount());

            if (expense.getCategoryId() != null) {
                stmt.setInt(3, expense.getCategoryId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setDate(4, expense.getExpenseDate());
            stmt.setString(5, expense.getNotes());
            stmt.setBoolean(6, expense.isRecurring());

            stmt.executeUpdate();
        }
    }



}
