package services;

import javax.ws.rs.core.MediaType;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import beans.Amenities;
import beans.AmenityStatus;
import beans.User;
import dao.AmenitiesDAO;
import dao.UserDAO;

@Path("")
public class AmenityService {
	
	@Context
	ServletContext context;
	
	@PostConstruct 
	public void init() {
		
		if(context.getAttribute("AmenitiesDAO") == null) {
			String contextPath = context.getRealPath("");
			context.setAttribute("AmenitiesDAO", new AmenitiesDAO(contextPath));
		}
		
	}
	
	@POST
	@Path("/addamenity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAmenity(Amenities a, @Context HttpServletRequest request)
	{
		AmenitiesDAO amenities = (AmenitiesDAO) context.getAttribute("AmenitiesDAO");
				
		if(amenities.getAmenities().containsKey(a.getName())) {
			return Response.status(400).build();
		}
		
		a.setAmenityStatus(AmenityStatus.ACTIVE);

		amenities.getAmenities().put(a.getName(), a);
		System.out.println(a.getIdOne().toString());
		context.setAttribute("AmenitiesDAO", amenities);
		
		amenities.saveAmenities(context.getRealPath(""), amenities);
		
		return Response.ok().build();
		
	}
	
	@POST
	@Path("/amenity/edit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response editAmenity(Amenities a, @Context HttpServletRequest request) {
		
		AmenitiesDAO amenities = (AmenitiesDAO) context.getAttribute("AmenitiesDAO");
		
		amenities.getAmenities().put(a.getName(),a);
			
		context.setAttribute("AmenitiesDAO", amenities);
		
		amenities.saveAmenities(context.getRealPath(""), amenities);
		
		return Response.ok(a).build();
	}
	
	@POST
	@Path("/amenity/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAmenity(Amenities a, @Context HttpServletRequest request) {
		
		AmenitiesDAO amenities = (AmenitiesDAO) context.getAttribute("AmenitiesDAO");
		
		a.setAmenityStatus(AmenityStatus.DELETED);
			
		context.setAttribute("AmenitiesDAO", amenities);
		
		amenities.saveAmenities(context.getRealPath(""), amenities);
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/amenity/all")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Amenities> all(@Context HttpServletRequest request) {
		
		AmenitiesDAO amenities = (AmenitiesDAO) context.getAttribute("AmenitiesDAO");		
		
		return amenities.getAllAmenities();
	}
	


}
