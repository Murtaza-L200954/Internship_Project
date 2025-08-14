package org.example.demo1.domain.dao;

import org.example.demo1.domain.models.MonthlyBudgets;
import java.sql.Date;
import java.util.List;

public interface MonthlyBudgetsDAO {

    boolean addMonthlyBudget(MonthlyBudgets monthlyBudget);

    MonthlyBudgets getMonthlyBudgetById(int id);
    List<MonthlyBudgets> getAllMonthlyBudgets();
    List<MonthlyBudgets> getMonthlyBudgetsByUserId(int userId);

    boolean updateMonthlyBudget(MonthlyBudgets monthlyBudget);

    boolean deleteMonthlyBudgetById(int id);

    boolean existsByUserIdAndMonth(int userId, Date monthYear);
}