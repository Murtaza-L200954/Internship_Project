package org.example.demo1.resources;

import org.example.demo1.domain.models.Bills;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.service.BillsService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Path("/bills")
public class BillsResource {

    private final BillsService billsService = new BillsService();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBill(Bills bill) {
        try {
            ServiceResponse<String> result = billsService.addBill(bill);

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
    public Response updateBill(@PathParam("id") int id, Bills bill) {
        try {
            bill.setId(id);
            ServiceResponse<String> result = billsService.updateBill(bill);

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
    public Response deleteBill(@PathParam("id") int id) {
        try {
            ServiceResponse<String> result = billsService.deleteBill(id);

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
    public Response getAllBills() {
        try {
            ServiceResponse<List<Bills>> result = billsService.getAllBills();

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
    public Response getBillById(@PathParam("id") int id) {
        try {
            ServiceResponse<Bills> result = billsService.getBillById(id);

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
    @Path("/pay/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response markBillAsPaid(@PathParam("id") int id) {
        try {
            ServiceResponse<String> result = billsService.markBillAsPaid(id);

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
    @Path("/upcoming")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpcomingBills(@QueryParam("date") String dateString) {
        try {
            LocalDate date = (dateString != null && !dateString.isEmpty())
                    ? LocalDate.parse(dateString)
                    : LocalDate.now();

            ServiceResponse<List<Bills>> result = billsService.getUpcomingBills(date);

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