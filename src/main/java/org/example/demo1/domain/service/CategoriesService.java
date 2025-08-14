package org.example.demo1.domain.service;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.CategoriesDAO;
import org.example.demo1.domain.daoImpl.CategoriesDAOImpl;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.models.Categories;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoriesService {

    public ServiceResponse<String> createCategory(Categories category) throws SQLException {
        Connection conn = null;
        conn = DBUtil.getConnection();
        CategoriesDAO categoriesDAO = new CategoriesDAOImpl(conn);

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return ServiceResponse.error("Category name is required",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isCreated = categoriesDAO.create(category);

        if (isCreated) {
            return ServiceResponse.success("Success", "Category created successfully");
        } else {
            return ServiceResponse.error("Failed to create category",
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    public ServiceResponse<String> updateCategory(Categories category) throws SQLException {
        Connection conn = null;
        conn = DBUtil.getConnection();
        CategoriesDAO categoriesDAO = new CategoriesDAOImpl(conn);

        if (category.getId() <= 0 || category.getName() == null || category.getName().trim().isEmpty()) {
            return ServiceResponse.error("Category ID and name are required",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isUpdated = categoriesDAO.update(category);

        if (isUpdated) {
            return ServiceResponse.success("Success", "Category updated successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Category not found or update failed",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<String> deleteCategory(int id)  throws SQLException {
        Connection conn = null;
        conn = DBUtil.getConnection();
        CategoriesDAO categoriesDAO = new CategoriesDAOImpl(conn);

        if (id <= 0) {
            return ServiceResponse.error("Invalid category ID",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isDeleted = categoriesDAO.delete(id);

        if (isDeleted) {
            return ServiceResponse.success("Success", "Category deleted successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Category not found or delete failed",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<List<Categories>> getallCategories() throws SQLException {
        Connection conn = DBUtil.getConnection();
        CategoriesDAO categoriesDAO = new CategoriesDAOImpl(conn);

        List<Categories> categories = categoriesDAO.getAllCategories();

        if (categories != null && !categories.isEmpty()) {
            return ServiceResponse.success(categories, "Categories retrieved successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("No categories found",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }
}