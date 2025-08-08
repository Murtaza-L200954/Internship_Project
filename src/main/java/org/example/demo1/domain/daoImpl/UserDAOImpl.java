package org.example.demo1.domain.daoImpl;

import org.example.demo1.domain.dao.UserDAO;
import org.example.domain.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
//todo userservice impl
public class UserDAOImpl implements UserDAO {
    private final Connection conn;

    public UserDAOImpl(Connection conn) {this.conn = conn;}

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, email);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password_hash")); // Also fix column name
                    user.setRole(rs.getString("role"));
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    return user;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (email,password_hash,role) VALUES (?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            int rowsinserted = stmt.executeUpdate();
            return rowsinserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET email=?,password_hash=?,role=? WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setInt(4, user.getId());
            int rowsupdated = stmt.executeUpdate();
            return rowsupdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            int rowsdeleted = stmt.executeUpdate();
            return rowsdeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean UserExists(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, email);
            try(ResultSet rs = stmt.executeQuery()){
                return rs.next();
            }
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
