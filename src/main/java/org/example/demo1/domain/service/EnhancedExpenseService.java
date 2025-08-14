package org.example.demo1.domain.service;

import org.example.demo1.aws.BudgetNotificationService;
import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.ExpenseDAO;
import org.example.demo1.domain.daoImpl.ExpenseDAOImpl;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.models.Expenses;
import org.example.demo1.domain.models.MonthlyBudgets;
import org.example.demo1.domain.service.ExpenseService;
import org.example.demo1.domain.service.MonthlyBudgetsService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class EnhancedExpenseService extends ExpenseService {

    private final BudgetNotificationService notificationService;
    private final MonthlyBudgetsService budgetService;

    public EnhancedExpenseService() {
        super();
        this.notificationService = new BudgetNotificationService();
        this.budgetService = new MonthlyBudgetsService();

        try {
            this.notificationService.initialize();
            this.notificationService.startMessageProcessor();
            System.out.println(" Budget notification service ready!");
        } catch (Exception e) {
            System.err.println(" Failed to initialize notification service: " + e.getMessage());
        }
    }

    @Override
    public ServiceResponse<String> addExpense(Expenses expense) {
        try {
            if (expense.getUser_id() <= 0) {
                return ServiceResponse.error("Invalid user ID", 400);
            }

            BudgetCheck budgetCheck = checkBudget(expense);

            if (budgetCheck.exceedsBudget) {
                System.out.println(" Budget overflow detected for user ID: " + expense.getUser_id());


                String userEmail = getUserEmailById(expense.getUser_id());

                if (userEmail != null) {

                    notificationService.publishBudgetOverflow(
                            userEmail,
                            expense.getAmount(),
                            budgetCheck.currentSpent,
                            budgetCheck.monthlyBudget
                    );
                }

                String budgetInfo = String.format(
                        "Budget overflow: $%.2f expense + $%.2f spent = $%.2f > $%.2f budget (Overflow: $%.2f)",
                        expense.getAmount(),
                        budgetCheck.currentSpent,
                        budgetCheck.currentSpent + expense.getAmount(),
                        budgetCheck.monthlyBudget,
                        budgetCheck.overflowAmount
                );

                return ServiceResponse.error("Expense rejected - would exceed monthly budget: " + budgetInfo, 400);
            }

            return super.addExpense(expense);

        } catch (Exception e) {
            System.err.println(" Error in enhanced addExpense: " + e.getMessage());
            e.printStackTrace();

            return ServiceResponse.error("Failed to process expense: " + e.getMessage(), 500);
        }
    }

    private BudgetCheck checkBudget(Expenses expense) {
        BudgetCheck check = new BudgetCheck();

        try {
            int userId = expense.getUser_id();

            YearMonth currentMonth = YearMonth.now();
            MonthlyBudgets currentBudget = getCurrentMonthBudget(userId, currentMonth);

            if (currentBudget == null) {
                check.exceedsBudget = false;
                return check;
            }

            check.monthlyBudget = currentBudget.getBudget_limit();

            check.currentSpent = calculateCurrentMonthSpending(userId);

            double totalAfterExpense = check.currentSpent + expense.getAmount();
            check.exceedsBudget = totalAfterExpense > check.monthlyBudget;
            check.overflowAmount = Math.max(0, totalAfterExpense - check.monthlyBudget);

        } catch (Exception e) {
            System.err.println(" Error checking budget: " + e.getMessage());
            check.exceedsBudget = false;
        }

        return check;
    }

    private MonthlyBudgets getCurrentMonthBudget(int userId, YearMonth currentMonth) {
        try {
            ServiceResponse<List<MonthlyBudgets>> budgetResponse = budgetService.getMonthlyBudgetsByUserId(userId);
            if (!budgetResponse.isSuccess() || budgetResponse.getData() == null || budgetResponse.getData().isEmpty()) {
                return null;
            }

            for (MonthlyBudgets budget : budgetResponse.getData()) {
                if (budget.getMonth_year() != null && budget.getMonth_year().equals(currentMonth)) {
                    return budget;
                }
            }

            return budgetResponse.getData().get(0);
        } catch (Exception e) {
            System.err.println(" Error getting current month budget: " + e.getMessage());
            return null;
        }
    }

    private double calculateCurrentMonthSpending(int userId) {
        try {
            ServiceResponse<List<Expenses>> expensesResponse = getExpensesByUserId(userId);
            if (!expensesResponse.isSuccess() || expensesResponse.getData() == null) {
                return 0.0;
            }

            YearMonth currentMonth = YearMonth.now();
            double total = 0.0;

            for (Expenses expense : expensesResponse.getData()) {
                if (expense.getExpense_date() != null) {

                    LocalDate expenseDate = expense.getExpense_date().toLocalDate();
                    YearMonth expenseMonth = YearMonth.from(expenseDate);

                    if (expenseMonth.equals(currentMonth)) {
                        total += expense.getAmount();
                    }
                }
            }

            return total;
        } catch (Exception e) {
            System.err.println(" Error calculating spending: " + e.getMessage());
            return 0.0;
        }
    }

    private ServiceResponse<List<Expenses>> getExpensesByUserId(int userId) {
        try {
            Connection conn = DBUtil.getConnection();
            ExpenseDAO expenseDAO = new ExpenseDAOImpl(conn);

            if (userId <= 0) {
                return ServiceResponse.error("Invalid user ID", 400);
            }

            List<Expenses> expenses = expenseDAO.getAllExpensesByUserId(userId);

            if (expenses != null && !expenses.isEmpty()) {
                return ServiceResponse.success(expenses, "Expenses retrieved successfully");
            } else {
                return ServiceResponse.success(new ArrayList<>(), "No expenses found for this user");
            }
        } catch (SQLException e) {
            System.err.println(" Error getting expenses by user ID: " + e.getMessage());
            return ServiceResponse.error("Database error: " + e.getMessage(), 500);
        }
    }

    private String getUserEmailById(int userId) {
        try {
            Connection conn = DBUtil.getConnection();
            String query = "SELECT email FROM users WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            System.err.println(" Error getting user email: " + e.getMessage());
        }
        return null;
    }


    private static class BudgetCheck {
        boolean exceedsBudget = false;
        double currentSpent = 0.0;
        double monthlyBudget = 0.0;
        double overflowAmount = 0.0;
    }
}