package org.example.demo1.resources;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.UserDAO;
import org.example.demo1.domain.daoImpl.UserDAOImpl;
import org.example.domain.models.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.Base64;

@Path("/signup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SignupResource {

    @POST
    public Response Signup(User user){
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole();

        if(email.isEmpty() || password.isEmpty() || role.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please fill all the fields")
                    .build();
        }

        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

        try(Connection conn = DBUtil.getConnection()){
            UserDAO userDAO = new UserDAOImpl(conn);

            if(userDAO.getUserByEmail(email) != null){
                return Response.status(Response.Status.CONFLICT)
                        .entity("Email already exists")
                        .build();
            }

            if(!role.equals("user") && !role.equals("admin")){
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("VALID ROLES ARE user and admin ")
                        .build();
            }

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(encodedPassword);
            newUser.setRole(role);

            boolean userAdded = userDAO.addUser(newUser);
            if(!userAdded){
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create user")
                        .build();
            }
            else{
                return Response.status(Response.Status.CREATED)
                        .entity("Successfully created user")
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to get connection")
                    .build();
        }
    }

}
