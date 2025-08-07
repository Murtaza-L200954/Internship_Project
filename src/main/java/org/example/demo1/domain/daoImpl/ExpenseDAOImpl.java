package org.example.demo1.domain.daoImpl;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.ExpenseDAO;
import org.example.demo1.domain.models.Expenses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAOImpl implements ExpenseDAO {
    private final Connection conn;

    public ExpenseDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean addExpense(Expenses expense){
        String sql = "INSERT INTO expenses (user_id,amount,category_id,expense_date,notes,is_recurring) VALUES (?,?,?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, expense.getUser_id());
            stmt.setDouble(2, expense.getAmount());
            stmt.setInt(3, expense.getCategory_id());
            stmt.setDate(4,expense.getExpense_date());
            stmt.setString(5,expense.getNotes());
            stmt.setBoolean(6,expense.isIs_recurring());
            int rowsinserted = stmt.executeUpdate();
            return rowsinserted > 0;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Expenses> getAllExpensesbyEmail(String email) {
        List<Expenses> expenses = new ArrayList<>();
        String sql = "SELECT e.id, e.user_id, e.amount, e.category_id, e.expense_date, e.notes, e.is_recurring, e.created_at, e.updated_at " +
                "FROM expenses e JOIN users u ON e.user_id = u.id WHERE u.email = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, email);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Expenses expense = new Expenses();
                    expense.setId(rs.getInt("id"));
                    expense.setUser_id(rs.getInt("user_id"));
                    expense.setAmount(rs.getDouble("amount"));
                    expense.setCategory_id(rs.getInt("category_id"));
                    expense.setExpense_date(rs.getDate("expense_date"));
                    expense.setNotes(rs.getString("notes"));
                    expense.setIs_recurring(rs.getBoolean("is_recurring"));
                    expense.setCreatedAt(rs.getTimestamp("created_at"));
                    expense.setUpdatedAt(rs.getTimestamp("updated_at"));
                    expenses.add(expense);
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public Expenses getExpenseById(int id) {
        String sql = "SELECT id, user_id, amount, category_id, expense_date, notes, is_recurring, created_at, updated_at FROM expenses WHERE id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    Expenses expense = new Expenses();
                    expense.setId(rs.getInt("id"));
                    expense.setUser_id(rs.getInt("user_id"));
                    expense.setAmount(rs.getDouble("amount"));
                    expense.setCategory_id(rs.getInt("category_id"));
                    expense.setExpense_date(rs.getDate("expense_date"));
                    expense.setNotes(rs.getString("notes"));
                    expense.setIs_recurring(rs.getBoolean("is_recurring"));
                    expense.setCreatedAt(rs.getTimestamp("created_at"));
                    expense.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return expense;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateExpense(Expenses expense) {
        String sql = "UPDATE expenses SET user_id = ?, amount = ?, category_id = ?, expense_date = ?, notes = ?, is_recurring = ? WHERE id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, expense.getUser_id());
            stmt.setDouble(2, expense.getAmount());
            stmt.setInt(3, expense.getCategory_id());
            stmt.setDate(4, expense.getExpense_date());
            stmt.setString(5, expense.getNotes());
            stmt.setBoolean(6, expense.isIs_recurring());
            stmt.setInt(7, expense.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteExpenseById(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}