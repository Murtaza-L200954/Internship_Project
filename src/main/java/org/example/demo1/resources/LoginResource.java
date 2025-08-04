package org.example.demo1.resources;

import org.example.demo1.common.DBUtil;
import org.example.demo1.domain.dao.UserDAO;
import org.example.demo1.domain.daoImpl.UserDAOImpl;
import org.example.demo1.common.JWTUtil;
import org.example.domain.models.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.util.Base64;

@Path("/login")
public class LoginResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user){
        String email = user.getEmail();
        String password = user.getPassword();

        try(Connection conn = DBUtil.getConnection()){
            UserDAO userDAO = new UserDAOImpl(conn);
            User user1 = userDAO.getUserByEmail(email);

            if(user1 != null) {
                String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

                if(user1.getPassword().equals(encodedPassword)) {
                    String token = JWTUtil.generateToken(user1.getEmail(),user1.getRole());
                    String json = "{\"token\":\"" + token + "\"}";
                    return Response.status(Response.Status.OK).entity(json).build();
                }
            }
            else{
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid email or password")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Internal server error\"}")
                    .build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
