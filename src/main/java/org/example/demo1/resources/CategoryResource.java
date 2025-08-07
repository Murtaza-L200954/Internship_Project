package org.example.demo1.resources;

import org.example.demo1.domain.models.Categories;
import org.example.demo1.domain.dto.ServiceResponse;
import org.example.demo1.domain.service.CategoriesService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/category")
public class CategoryResource {

    private final CategoriesService categoriesService = new CategoriesService();

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Categories categories) {
        try{
            ServiceResponse<String> result = categoriesService.createCategory(categories);

            return Response.status(result.getStatusCode())
                    .entity(result)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal Server Error")
                    .build();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Categories categories) {
        try{
            categories.setId(id);
            ServiceResponse<String> result = categoriesService.updateCategory(categories);

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
    public Response deleteCategory(@PathParam("id") int id){
      try {
          ServiceResponse<String> result = categoriesService.deleteCategory(id);

          return Response.status(result.getStatusCode())
                  .entity(result)
                  .build();
      }  catch (Exception e) {
          e.printStackTrace();

          return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                  .entity("Unexpected error, try later")
                  .build();
      }
    }
}
