package services;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Apartment;
import beans.ApartmentStatus;
import beans.Reservation;
import beans.ReservationStatus;
import dao.ApartmentDAO;
import dao.ReservationDAO;
import dao.UserDAO;

public class ReservationService {

	@Context
	ServletContext context;
	
	@PostConstruct 
	public void init() {
		
		if(context.getAttribute("ReservationDAO") == null) {
			String contextPath = context.getRealPath("");
			context.setAttribute("ReservationDAO", new ReservationDAO(contextPath));
		}
	}
	
	@GET
	@Path("/reservations/all")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAll(@Context HttpServletRequest request) {
		
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		return dao.getReservations().values();
	}
	
	@GET
	@Path("/reservations/all-created")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAllCreated(@Context HttpServletRequest request) {
		
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		return dao.createdReservations();
	}
	
	@GET
	@Path("/reservations/all-completed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAllCompleted(@Context HttpServletRequest request) {
		
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		return dao.completedReservations();
	}
	
	@GET
	@Path("/reservations/all-denied")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAllDenied(@Context HttpServletRequest request) {
		
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		return dao.deniedReservations();
	}
	
	@GET
	@Path("/reservations/all-cancelled")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAllCancelled(@Context HttpServletRequest request) {
		
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		return dao.cancelledReservations();
	}
	
	@GET
	@Path("/reservations/all-accepted")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAllAccepted(@Context HttpServletRequest request) {
		
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		return dao.acceptedReservations();
	}
	
	@GET
	@Path("/reservations/all/{usernmae}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getAllMyReservations(@PathParam("username") String username,@Context HttpServletRequest request) {
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
	
		return dao.allMyReservations(username);
	}
	
	@POST
	@Path("/reservations/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(Reservation r, @Context HttpServletRequest request)
	{
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		
		r.setReservationStatus(ReservationStatus.CREATED);
		
		long nights = Duration.between(LocalDate.parse(r.getReservedFrom()), LocalDate.parse(r.getReservedTill())).toDays();
		int intValue = (int) nights;
		
		r.setNumberOfNights(intValue);
		
		ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		Apartment a = apartments.findApartmentByName(r.getApartment());
		r.setPrice(a.getPrice()*intValue);
		
		dao.getReservations().put(r.getIdOne().toString(), r);
		context.setAttribute("ReservationDAO", dao);
		
		dao.saveReservation(context.getRealPath(""), dao);
		
		return Response.ok().build();
		
	}
	
	
}
