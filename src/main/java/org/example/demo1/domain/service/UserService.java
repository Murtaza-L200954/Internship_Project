package org.example.demo1.domain.service;

import org.example.demo1.common.DBUtil;
import org.example.demo1.common.JWTUtil;
import org.example.demo1.domain.dao.UserDAO;
import org.example.demo1.domain.daoImpl.UserDAOImpl;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.dto.ValidationResult;
import org.example.domain.models.User;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

public class UserService{

    public ServiceResponse<String> login(String email, String password) throws SQLException{

        if(email.isEmpty()){
            return ServiceResponse.error("Username is required",400);
        }

        if(password.isEmpty()){
            return ServiceResponse.error("Password is required",400);
        }
        Connection conn = DBUtil.getConnection();
        UserDAO userDAO = new UserDAOImpl(conn);
        User user = userDAO.getUserByEmail(email);
        if(user == null){
            return ServiceResponse.error("User not found",400);
        }

        String encodedInputPassword = Base64.getEncoder().encodeToString(password.getBytes());

        if(!user.getPassword().equals(encodedInputPassword)){
            return ServiceResponse.error("Wrong password",400);
        }

        String token = JWTUtil.generateToken(user.getEmail(),user.getRole());

        return ServiceResponse.success(token,"Login successful").withStatusCode(200);
    }

    public ServiceResponse<User> createUser(User user) throws SQLException {

        ValidationResult validation = validateUserInput(user);
        if (!validation.isValid()) {
            return ServiceResponse.error(validation.getErrorMessage(), 400);
        }

        String encodedPassword = Base64.getEncoder().encodeToString(user.getPassword().getBytes());

        Connection conn = DBUtil.getConnection();
        UserDAO userDAO = new UserDAOImpl(conn);

        if (userDAO.getUserByEmail(user.getEmail()) != null) {
            return ServiceResponse.error("Email already exists", 409);
        }

        if (!isValidRole(user.getRole())) {
            return ServiceResponse.error("Invalid role. Valid roles are: user, admin", 400);
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setRole(user.getRole());

        boolean userAdded = userDAO.addUser(newUser);
        if (!userAdded) {
            return ServiceResponse.error("Failed to create user due to database error", 500);
        }

        return ServiceResponse.success(newUser, "User created successfully");
    }

    private ValidationResult validateUserInput(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return ValidationResult.invalid("Email is required and cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return ValidationResult.invalid("Password is required and cannot be empty");
        }

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            return ValidationResult.invalid("Role is required and cannot be empty");
        }

        if (!isValidEmail(user.getEmail())) {
            return ValidationResult.invalid("Please provide a valid email address");
        }

        if (user.getPassword().length() < 6) {
            return ValidationResult.invalid("Password must be at least 6 characters long");
        }

        return ValidationResult.valid();
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isValidRole(String role) {
        return "user".equals(role) || "admin".equals(role);
    }
}