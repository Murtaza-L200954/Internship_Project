package org.example.demo1.domain.service;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.BillsDAO;
import org.example.demo1.domain.daoImpl.BillsDAOImpl;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.models.Bills;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BillsService {

    public ServiceResponse<String> addBill(Bills bill) throws SQLException {
        Connection conn = DBUtil.getConnection();
        BillsDAO billsDAO = new BillsDAOImpl(conn);

        // Validate required fields
        if (bill.getUserId() <= 0 || bill.getTitle() == null || bill.getTitle().trim().isEmpty() ||
                bill.getDue_date() == null || bill.getAmount() <= 0) {
            return ServiceResponse.error("User ID, title, due date, and amount are required",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isAdded = billsDAO.addBill(bill);

        if (isAdded) {
            return ServiceResponse.success("Success", "Bill added successfully");
        } else {
            return ServiceResponse.error("Failed to add bill",
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    public ServiceResponse<String> updateBill(Bills bill) throws SQLException {
        Connection conn = DBUtil.getConnection();
        BillsDAO billsDAO = new BillsDAOImpl(conn);

        // Validate required fields
        if (bill.getId() <= 0 || bill.getUserId() <= 0 || bill.getTitle() == null ||
                bill.getTitle().trim().isEmpty() || bill.getDue_date() == null || bill.getAmount() <= 0) {
            return ServiceResponse.error("ID, user ID, title, due date, and amount are required",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isUpdated = billsDAO.updateBill(bill);

        if (isUpdated) {
            return ServiceResponse.success("Success", "Bill updated successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Bill not found or update failed",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<String> deleteBill(int id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        BillsDAO billsDAO = new BillsDAOImpl(conn);

        if (id <= 0) {
            return ServiceResponse.error("Invalid bill ID",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isDeleted = billsDAO.deleteBillbyid(id);

        if (isDeleted) {
            return ServiceResponse.success("Success", "Bill deleted successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Bill not found or delete failed",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<List<Bills>> getAllBills() throws SQLException {
        Connection conn = DBUtil.getConnection();
        BillsDAO billsDAO = new BillsDAOImpl(conn);

        List<Bills> bills = billsDAO.getAllBills();

        if (bills != null && !bills.isEmpty()) {
            return ServiceResponse.success(bills, "Bills retrieved successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("No bills found",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<Bills> getBillById(int id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        BillsDAO billsDAO = new BillsDAOImpl(conn);

        if (id <= 0) {
            return ServiceResponse.error("Invalid bill ID",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        Bills bill = billsDAO.getBillById(id);

        if (bill != null) {
            return ServiceResponse.success(bill, "Bill retrieved successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Bill not found",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<String> markBillAsPaid(int id) throws SQLException {
        Connection conn = DBUtil.getConnection();
        BillsDAO billsDAO = new BillsDAOImpl(conn);

        if (id <= 0) {
            return ServiceResponse.error("Invalid bill ID",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        boolean isMarked = billsDAO.markAsPaid(id);

        if (isMarked) {
            return ServiceResponse.success("Success", "Bill marked as paid successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("Bill not found or already paid",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    public ServiceResponse<List<Bills>> getUpcomingBills(LocalDate date) throws SQLException {
        Connection conn = DBUtil.getConnection();
        BillsDAO billsDAO = new BillsDAOImpl(conn);

        if (date == null) {
            date = LocalDate.now(); // Default to today if no date provided
        }

        List<Bills> upcomingBills = billsDAO.getUpcomingBills(date);

        if (upcomingBills != null && !upcomingBills.isEmpty()) {
            return ServiceResponse.success(upcomingBills, "Upcoming bills retrieved successfully")
                    .withStatusCode(Response.Status.OK.getStatusCode());
        } else {
            return ServiceResponse.error("No upcoming bills found",
                    Response.Status.NOT_FOUND.getStatusCode());
        }
    }
}