package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Amenities;
import beans.AmenityStatus;
import beans.Apartment;
import beans.ApartmentStatus;
import dao.AmenitiesDAO;
import dao.ApartmentDAO;
import dao.UserDAO;

@Path("")
public class ApartmentService {
	
	@Context
	ServletContext context;
	
	@PostConstruct 
	public void init() {
		
		if(context.getAttribute("UserDAO") == null) {
			String contextPath = context.getRealPath("");
			context.setAttribute("UserDAO", new UserDAO(contextPath));
		}
	}
	
	@GET
	@Path("/all")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context HttpServletRequest request) {
		
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
	
		
		return Response.ok(dao).build();	
	}
	
	
	@DELETE
	@Path("/delete/{idOne}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("idOne") String idOne) {
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		Apartment ap = dao.findApartmentById(idOne);
		ap.setApartmentStatus(ApartmentStatus.DELETED);
		
		context.setAttribute("ApartmentDAO", dao);
		
		return Response.ok().build();
	}
	

}
