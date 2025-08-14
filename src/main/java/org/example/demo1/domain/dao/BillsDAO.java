package org.example.demo1.domain.dao;

import org.example.demo1.domain.models.Bills;
import java.time.LocalDate;
import java.util.List;

public interface BillsDAO {
    boolean addBill(Bills bills);
    boolean updateBill(Bills bills);
    boolean deleteBillbyid(int id);
    List<Bills> getAllBills();
    boolean markAsPaid(int id);
    List<Bills> getUpcomingBills(LocalDate date);
    Bills getBillById(int id);
}