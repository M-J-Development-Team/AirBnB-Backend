package services;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Apartment;
import beans.Reservation;
import dao.ApartmentDAO;
import dao.ReservationDAO;

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
	
	
}
