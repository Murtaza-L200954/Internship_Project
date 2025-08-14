package org.example.demo1.resources;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.UserDAO;
import org.example.demo1.domain.daoImpl.UserDAOImpl;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.service.UserService;
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
    private final UserService userService = new UserService();

    @POST
    public Response signup(User user) {
        try {
            ServiceResponse<User> result = userService.createUser(user);

            if (result.isSuccess()) {
                return Response.status(Response.Status.CREATED)
                        .entity(result.getMessage())
                        .build();
            } else {
                return Response.status(result.getStatusCode())
                        .entity(result.getMessage())
                        .build();
            }

        } catch (Exception e) {
            // todo Log the exception properly (use a logger in real applications)
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred. Please try again later.")
                    .build();
        }
    }
}
