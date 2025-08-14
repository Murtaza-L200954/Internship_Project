package org.example.demo1.domain.daoImpl;

import org.example.demo1.domain.dao.MonthlyBudgetsDAO;
import org.example.demo1.domain.models.MonthlyBudgets;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MonthlyBudgetsDAOImpl implements MonthlyBudgetsDAO {
    private final Connection conn;

    public MonthlyBudgetsDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean addMonthlyBudget(MonthlyBudgets monthlyBudget) {
        String sql = "INSERT INTO monthly_budgets (user_id, month_year, salary, budget_limit) VALUES (?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, monthlyBudget.getUser_id());
            stmt.setDate(2, monthlyBudget.getMonth_year());
            stmt.setDouble(3, monthlyBudget.getSalary());
            stmt.setDouble(4, monthlyBudget.getBudget_limit());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateMonthlyBudget(MonthlyBudgets monthlyBudget) {
        String sql = "UPDATE monthly_budgets SET user_id = ?, month_year = ?, salary = ?, budget_limit = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, monthlyBudget.getUser_id());
            stmt.setDate(2, monthlyBudget.getMonth_year()); // instead of setString(...)            stmt.setDouble(3, monthlyBudget.getSalary());
            stmt.setDouble(4, monthlyBudget.getBudget_limit());
            stmt.setInt(5, monthlyBudget.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMonthlyBudgetById(int id) {
        String sql = "DELETE FROM monthly_budgets WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MonthlyBudgets> getAllMonthlyBudgets() {
        List<MonthlyBudgets> budgetsList = new ArrayList<>();
        String sql = "SELECT id, user_id, month_year, salary, budget_limit, created_at, updated_at FROM monthly_budgets ORDER BY user_id, month_year DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MonthlyBudgets budget = mapResultSetToMonthlyBudget(rs);
                budgetsList.add(budget);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budgetsList;
    }

    @Override
    public MonthlyBudgets getMonthlyBudgetById(int id) {
        String sql = "SELECT id, user_id, month_year, salary, budget_limit, created_at, updated_at FROM monthly_budgets WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMonthlyBudget(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MonthlyBudgets> getMonthlyBudgetsByUserId(int userId) {
        List<MonthlyBudgets> budgetsList = new ArrayList<>();
        String sql = "SELECT id, user_id, month_year, salary, budget_limit, created_at, updated_at FROM monthly_budgets WHERE user_id = ? ORDER BY month_year DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MonthlyBudgets budget = mapResultSetToMonthlyBudget(rs);
                    budgetsList.add(budget);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budgetsList;
    }

    @Override
    public boolean existsByUserIdAndMonth(int userId, Date monthYear) {
        String sql = "SELECT COUNT(*) FROM monthly_budgets WHERE user_id = ? AND month_year = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, monthYear);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private MonthlyBudgets mapResultSetToMonthlyBudget(ResultSet rs) throws SQLException {
        MonthlyBudgets budget = new MonthlyBudgets();
        budget.setId(rs.getInt("id"));
        budget.setUser_id(rs.getInt("user_id"));

        // Convert month_year string to YearMonth
        Date monthYearDate = rs.getDate("month_year");
        if (monthYearDate != null) {
            budget.setMonth_year(monthYearDate);
        }

        budget.setSalary(rs.getDouble("salary"));
        budget.setBudget_limit(rs.getDouble("budget_limit"));

        // Handle timestamps
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            budget.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            budget.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return budget;
    }
}