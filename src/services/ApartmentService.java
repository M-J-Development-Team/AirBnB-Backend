package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import beans.Reservation;
import beans.ReservationStatus;
import dao.AmenitiesDAO;
import dao.ApartmentDAO;
import dao.ReservationDAO;
import dao.UserDAO;

@Path("")
public class ApartmentService {
	
	@Context
	ServletContext context;
	
	@PostConstruct 
	public void init() {
		
		if(context.getAttribute("ApartmentDAO") == null) {
			String contextPath = context.getRealPath("");
			context.setAttribute("ApartmentDAO", new ApartmentDAO(contextPath));
		}
	}
	
	@GET
	@Path("/apartments/all")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Apartment> getAll(@Context HttpServletRequest request) {
		
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		return dao.getApartments().values();
	}
	
	@GET
	@Path("/apartments/allActive")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Apartment> getAllActive(@Context HttpServletRequest request) {
		
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		return dao.allActiveApartments();
	}
	

	@GET
	@Path("/apartments/all/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Apartment> getAllFromHostActive(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {
		
		System.out.println("here");
		
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");
		System.out.print("host is"+userdao.findbyID(idOne).getUsername());
		ArrayList<Apartment> apps = dao.allActiveApartmentsFromHost(userdao.findbyID(idOne));
	
		
		return apps;
	}
	
	@GET
	@Path("/apartments/deleted/all/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Apartment> getAllFromHostDeleted(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {
		
		System.out.println("here");
		
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");
		System.out.print("host is"+userdao.findbyID(idOne).getUsername());
		ArrayList<Apartment> apps = dao.allDeletedApartmentsFromHost(userdao.findbyID(idOne));
	
		
		return apps;
	}
	
	@GET
	@Path("/apartments/freedates/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getFreeDates(@PathParam("name") String name, @Context HttpServletRequest request) {
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		Apartment app = dao.findApartmentByName(name);
		return app.getFreeDates();
	}
	
	@POST
	@Path("/apartmentsadd")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(Apartment a, @Context HttpServletRequest request)
	{
		System.out.println("in here");
		ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("ApartmentDAO");
				
		if(apartments.getApartments().containsKey(a.getName())) {
			return Response.status(401).build();
		}
		
		a.setStatus(ApartmentStatus.ACTIVE);
		
		LocalDate start = LocalDate.parse(a.getDatesForRenting().get(0).getFrom());
		LocalDate end = LocalDate.parse(a.getDatesForRenting().get(0).getTo());
		
		ArrayList<String> totalDates = new ArrayList<String>();
		
		while (!start.isAfter(end)) {
		    totalDates.add(start.toString());
		    start = start.plusDays(1);
		}
		
		a.setFreeDates(totalDates);
		
		apartments.getApartments().put(a.getName(), a);
		context.setAttribute("ApartmentDAO", apartments);
		
		apartments.saveApartment(context.getRealPath(""), apartments);
		
		System.out.println(a.getHost());
		
		return Response.ok().build();
		
	}
	
	
	@DELETE
	@Path("/apartments/delete/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("name") String name) {
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		Apartment ap = dao.findApartmentByName(name);
		ReservationDAO resdDao = (ReservationDAO) context.getAttribute("ReservationDAO");
		
		for(String r : ap.getReservations()) {
			Reservation reservation = resdDao.findReservationById(r);
			reservation.setReservationStatus(ReservationStatus.CANCLED);
		}
		
		ap.setStatus(ApartmentStatus.DELETED);
		
		context.setAttribute("ApartmentDAO", dao);
		
		return Response.ok().build();
	}
	

}
