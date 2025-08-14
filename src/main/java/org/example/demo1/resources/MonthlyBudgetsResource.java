package org.example.demo1.resources;

import org.example.demo1.domain.models.MonthlyBudgets;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.service.MonthlyBudgetsService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/monthly-budgets")
public class MonthlyBudgetsResource {

    private final MonthlyBudgetsService monthlyBudgetsService = new MonthlyBudgetsService();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMonthlyBudget(MonthlyBudgets monthlyBudget) {
        try {
            ServiceResponse<String> result = monthlyBudgetsService.addMonthlyBudget(monthlyBudget);

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
    public Response updateMonthlyBudget(@PathParam("id") int id, MonthlyBudgets monthlyBudget) {
        try {
            monthlyBudget.setId(id);
            ServiceResponse<String> result = monthlyBudgetsService.updateMonthlyBudget(monthlyBudget);

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
    public Response deleteMonthlyBudget(@PathParam("id") int id) {
        try {
            ServiceResponse<String> result = monthlyBudgetsService.deleteMonthlyBudget(id);

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
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMonthlyBudgets() {
        try {
            ServiceResponse<List<MonthlyBudgets>> result = monthlyBudgetsService.getAllMonthlyBudgets();

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
    public Response getMonthlyBudgetById(@PathParam("id") int id) {
        try {
            ServiceResponse<MonthlyBudgets> result = monthlyBudgetsService.getMonthlyBudgetById(id);

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
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthlyBudgetsByUserId(@PathParam("userId") int userId) {
        try {
            ServiceResponse<List<MonthlyBudgets>> result = monthlyBudgetsService.getMonthlyBudgetsByUserId(userId);

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