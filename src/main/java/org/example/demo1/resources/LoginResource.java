package org.example.demo1.resources;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.UserDAO;
import org.example.demo1.domain.daoImpl.UserDAOImpl;
import org.example.demo1.common.JWTUtil;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.domain.models.User;
import org.example.demo1.domain.service.UserService;
import org.example.demo1.domain.dto.LoginRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.Base64;
//todo rename monthly budgets, use camel case
@Path("/login")
public class LoginResource {

    private final UserService userService = new UserService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        try{
            ServiceResponse<String> result = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

            return Response.status(result.getStatusCode())
                    .entity(result)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Unexpected error, try later")
                    .build();
        }
    }
}
