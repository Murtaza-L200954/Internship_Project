package org.example.demo1.resources;

import org.example.demo1.domain.models.Expenses;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.service.ExpenseService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/expenses")
public class ExpenseResource {

    private final ExpenseService expenseService = new ExpenseService();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addExpense(Expenses expense) {
        try {
            ServiceResponse<String> result = expenseService.addExpense(expense);

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

    @GET
    @Path("/user/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllExpensesByEmail(@PathParam("email") String email) {
        try {
            ServiceResponse<List<Expenses>> result = expenseService.getAllExpensesByEmail(email);

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

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExpenseById(@PathParam("id") int id) {
        try {
            ServiceResponse<Expenses> result = expenseService.getExpenseById(id);

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

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateExpense(@PathParam("id") int id, Expenses expense) {
        try {
            expense.setId(id);
            ServiceResponse<String> result = expenseService.updateExpense(expense);

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

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteExpense(@PathParam("id") int id) {
        try {
            ServiceResponse<String> result = expenseService.deleteExpense(id);

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