package org.example.demo1.domain.daoImpl;

import org.example.demo1.domain.dao.CategoriesDAO;
import org.example.demo1.domain.models.Categories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDAOImpl implements CategoriesDAO {
    private final Connection conn;

    public CategoriesDAOImpl(Connection connection) {
        this.conn = connection;
    }

    public boolean create(Categories categories) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, categories.getName());
            int rowsinserted = stmt.executeUpdate();
            return rowsinserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean update(Categories categories) {
        String sql = "UPDATE categories SET name=? WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, categories.getName());
            int rowsupdated = stmt.executeUpdate();
            return rowsupdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM categories WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Categories> getAllCategories() {
        List<Categories> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Categories c = new Categories();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setCreatedAt(rs.getTimestamp("created_at"));
                    c.setUpdatedAt(rs.getTimestamp("updated_at"));
                    categories.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
}
