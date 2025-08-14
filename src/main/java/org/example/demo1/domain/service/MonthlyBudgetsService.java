package org.example.demo1.domain.service;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.MonthlyBudgetsDAO;
import org.example.demo1.domain.daoImpl.MonthlyBudgetsDAOImpl;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.models.MonthlyBudgets;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.sql.Date;


public class MonthlyBudgetsService {

    public ServiceResponse<String> addMonthlyBudget(MonthlyBudgets monthlyBudget) throws SQLException {
        Connection conn = DBUtil.getConnection();
        MonthlyBudgetsDAO monthlyBudgetsDAO = new MonthlyBudgetsDAOImpl(conn);

        // Validate required fields
        if (monthlyBudget.getUser_id() <= 0 || monthlyBudget.getMonth_year() == null ||
                monthlyBudget.getBudget_limit() < 0 || monthlyBudget.getSalary() < 0) {
            return ServiceResponse.error("User ID, month year, and valid budget limit and salary are required",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        // Check if budget already exists for this user and month
        if (monthlyBudgetsDAO.existsByUserIdAndMonth(monthlyBudget.getUser_id(), monthlyBudget.getMonth_year())) {
            return ServiceResponse.error("Budget already exists for this user and month",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isAdded = monthlyBudgetsDAO.addMonthlyBudget(monthlyBudget);

        if (isAdded) {
            return ServiceResponse.success("Success", "Monthly budget added successfully");
        } else {
            return ServiceResponse.error("Failed to add monthly budget",
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    public ServiceResponse<String> updateMonthlyBudget(MonthlyBudgets monthlyBudget) throws SQLException {
        Connection conn = DBUtil.getConnection();
        MonthlyBudgetsDAO monthlyBudgetsDAO = new MonthlyBudgetsDAOImpl(conn);

        // Validate required fields
        if (monthlyBudget.getId() <= 0 || monthlyBudget.getUser_id() <= 0 ||
                monthlyBudget.getMonth_year() == null || monthlyBudget.getBudget_limit() < 0 ||
                monthlyBudget.getSalary() < 0) {
            return ServiceResponse.error("ID, user ID, month year, and valid budget limit and salary are required",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isUpdated = monthlyBudgetsDAO.updateMonthlyBudget(monthlyBudget);

        if (isUpdated) {
            return ServiceResponse.success("Success", "Monthly budget updated successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Monthly budget not found or update failed",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<String> deleteMonthlyBudget(int id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        MonthlyBudgetsDAO monthlyBudgetsDAO = new MonthlyBudgetsDAOImpl(conn);

        if (id <= 0) {
            return ServiceResponse.error("Invalid monthly budget ID",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isDeleted = monthlyBudgetsDAO.deleteMonthlyBudgetById(id);

        if (isDeleted) {
            return ServiceResponse.success("Success", "Monthly budget deleted successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Monthly budget not found or delete failed",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<List<MonthlyBudgets>> getAllMonthlyBudgets() throws SQLException {
        Connection conn = DBUtil.getConnection();
        MonthlyBudgetsDAO monthlyBudgetsDAO = new MonthlyBudgetsDAOImpl(conn);

        List<MonthlyBudgets> budgets = monthlyBudgetsDAO.getAllMonthlyBudgets();

        if (budgets != null && !budgets.isEmpty()) {
            return ServiceResponse.success(budgets, "Monthly budgets retrieved successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("No monthly budgets found",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<MonthlyBudgets> getMonthlyBudgetById(int id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        MonthlyBudgetsDAO monthlyBudgetsDAO = new MonthlyBudgetsDAOImpl(conn);

        if (id <= 0) {
            return ServiceResponse.error("Invalid monthly budget ID",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        MonthlyBudgets budget = monthlyBudgetsDAO.getMonthlyBudgetById(id);

        if (budget != null) {
            return ServiceResponse.success(budget, "Monthly budget retrieved successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Monthly budget not found",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<List<MonthlyBudgets>> getMonthlyBudgetsByUserId(int userId) throws SQLException {
        Connection conn = DBUtil.getConnection();
        MonthlyBudgetsDAO monthlyBudgetsDAO = new MonthlyBudgetsDAOImpl(conn);

        if (userId <= 0) {
            return ServiceResponse.error("Invalid user ID",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        List<MonthlyBudgets> budgets = monthlyBudgetsDAO.getMonthlyBudgetsByUserId(userId);

        if (budgets != null && !budgets.isEmpty()) {
            return ServiceResponse.success(budgets, "Monthly budgets retrieved successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("No monthly budgets found for this user",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }
}
