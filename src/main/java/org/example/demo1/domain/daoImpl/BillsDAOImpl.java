package org.example.demo1.domain.daoImpl;

import org.example.demo1.domain.dao.BillsDAO;
import org.example.demo1.domain.models.Bills;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillsDAOImpl implements BillsDAO {
    private final Connection conn;

    public BillsDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean addBill(Bills bills) {
        String sql = "INSERT INTO bills (user_id, expense_id, title, due_date, amount, is_paid) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bills.getUserId());

            // Handle nullable expense_id
            if (bills.getExpenseid() == 0) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, bills.getExpenseid());
            }

            stmt.setString(3, bills.getTitle());
            stmt.setDate(4, Date.valueOf(bills.getDue_date()));
            stmt.setDouble(5, bills.getAmount());
            stmt.setBoolean(6, bills.isIs_paid());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateBill(Bills bills) {
        String sql = "UPDATE bills SET user_id = ?, expense_id = ?, title = ?, due_date = ?, amount = ?, is_paid = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bills.getUserId());

            // Handle nullable expense_id
            if (bills.getExpenseid() == 0) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, bills.getExpenseid());
            }

            stmt.setString(3, bills.getTitle());
            stmt.setDate(4, Date.valueOf(bills.getDue_date()));
            stmt.setDouble(5, bills.getAmount());
            stmt.setBoolean(6, bills.isIs_paid());
            stmt.setInt(7, bills.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBillbyid(int id) {
        String sql = "DELETE FROM bills WHERE id = ?";
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
    public List<Bills> getAllBills() {
        List<Bills> billsList = new ArrayList<>();
        String sql = "SELECT id, user_id, expense_id, title, due_date, amount, is_paid, created_at, updated_at FROM bills";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Bills bill = mapResultSetToBill(rs);
                billsList.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billsList;
    }

    @Override
    public boolean markAsPaid(int id) {
        String sql = "UPDATE bills SET is_paid = true WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Bills> getUpcomingBills(LocalDate date) {
        List<Bills> upcomingBills = new ArrayList<>();
        String sql = "SELECT id, user_id, expense_id, title, due_date, amount, is_paid, created_at, updated_at FROM bills WHERE due_date >= ? AND is_paid = false ORDER BY due_date ASC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Bills bill = mapResultSetToBill(rs);
                    upcomingBills.add(bill);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upcomingBills;
    }

    @Override
    public Bills getBillById(int id) {
        String sql = "SELECT id, user_id, expense_id, title, due_date, amount, is_paid, created_at, updated_at FROM bills WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBill(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bills mapResultSetToBill(ResultSet rs) throws SQLException {
        Bills bill = new Bills();
        bill.setId(rs.getInt("id"));
        bill.setUserId(rs.getInt("user_id"));
        bill.setExpenseid(rs.getInt("expense_id"));
        bill.setTitle(rs.getString("title"));
        bill.setDue_date(rs.getDate("due_date").toLocalDate());
        bill.setAmount(rs.getDouble("amount"));
        bill.setIs_paid(rs.getBoolean("is_paid"));

        // Handle timestamps
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            bill.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            bill.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return bill;
    }
}