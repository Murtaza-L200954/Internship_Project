package org.example.demo1.domain.dao;
import org.example.demo1.domain.models.Categories;


public interface CategoriesDAO {
    boolean create(Categories categories);
    boolean update(Categories categories);
    boolean delete(int id);
}
