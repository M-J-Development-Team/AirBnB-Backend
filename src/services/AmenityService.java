package services;

import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.Collection;

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
import javax.ws.rs.core.Response;

import beans.Amenities;
import beans.AmenityStatus;
import beans.Apartment;
import beans.User;
import dao.AmenitiesDAO;
import dao.ApartmentDAO;
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
	@Path("/edit")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response edit(Amenities amenity) {
		
		AmenitiesDAO dao = (AmenitiesDAO) context.getAttribute("AmenitiesDAO");
		
		Amenities a = dao.findById(amenity.getIdOne().toString());
		if(a != null) {
			if(!amenity.getName().equals(a.getName())) {
				a.setName(amenity.getName());
			}
		}
		
		return Response.ok(amenity).build();
	}
	
	
	@DELETE
	@Path("/amenity/delete/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("name") String name) {
		AmenitiesDAO dao = (AmenitiesDAO) context.getAttribute("AmenitiesDAO");
		ApartmentDAO apDao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		Amenities amenity = dao.findByName(name);
		amenity.setAmenityStatus(AmenityStatus.DELETED);
		
		ArrayList<Apartment> apps = apDao.allApartments();
		
		if(apps != null) {
			for(Apartment a : apps) {
				for(Amenities am : a.getAmenities()) {
					if(am.equals(amenity.getName())) {
						a.getAmenities().remove(am);
					}
				}
			}
		}
		
		context.setAttribute("AmenitiesDAO", dao);
		dao.saveAmenities(context.getRealPath(""), dao);
		
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
	
	@GET
	@Path("/amenity/avaliable/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Amenities> allAvaliableForApartment(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {
		
		AmenitiesDAO amenities = (AmenitiesDAO) context.getAttribute("AmenitiesDAO");
		ApartmentDAO daoAp = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		//amenities.findAmenitiesForApartment(daoAp.findApartmentById(idOne));
		
		return amenities.findAmenitiesForApartment(daoAp.findApartmentById(idOne));
	}
	
	
	


}
