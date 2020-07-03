package services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import beans.User;
import dao.ApartmentDAO;
import dao.ReservationDAO;
import dao.UserDAO;

@Path("")
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
		
		for(Reservation r : dao.getReservations().values()) {
			if(LocalDate.parse(r.getReservedTill()).isAfter(LocalDate.now()) && r.getReservationStatus().equals(ReservationStatus.ACCEPTED)) {
				r.setReservationStatus(ReservationStatus.COMPLETED);
			}
		}
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
		
		long n =  ChronoUnit.DAYS.between(LocalDate.parse(r.getReservedFrom()), LocalDate.parse(r.getReservedTill()));
		int intValue = (int) n;
		
		r.setNumberOfNights(intValue);
		
		ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		Apartment a = apartments.findApartmentByName(r.getApartment());
		r.setPrice(a.getPrice()*intValue);
		
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");
		User user = userdao.findUserByUsername(r.getGuest());
		
		
		dao.getReservations().put(r.getIdOne().toString(), r);
		context.setAttribute("ReservationDAO", dao);
		
		LocalDate start = LocalDate.parse(r.getReservedFrom());
		LocalDate end = LocalDate.parse(r.getReservedTill());
		
		ArrayList<String> totalDates = new ArrayList<String>();
		
		while (!start.isAfter(end)) {
		    totalDates.add(start.toString());
		    start = start.plusDays(1);
		}
		
		a.setRentedDates(totalDates);
		r.setRentedDates(totalDates);
		for(String date : totalDates) {
			a.getFreeDates().remove(date);
		}
		
		dao.saveReservation(context.getRealPath(""), dao);
		a.getReservations().add(r.getIdOne().toString());
		user.getReservations().add(r.getIdOne().toString());
		apartments.saveApartment(context.getRealPath(""), apartments);
		userdao.saveUser(context.getRealPath(""), userdao);
		
		return Response.ok().build();
		
	}
	
	@POST
	@Path("/reservations/cancel/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancel(@PathParam("idOne") String idOne, @Context HttpServletRequest request)
	{
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		Reservation r = dao.findReservationById(UUID.fromString(idOne));
		Apartment a = apartments.findApartmentByName(r.getApartment());
		List<String> toRemove = new ArrayList<String>();
		
		for(String date : r.getRentedDates()) {
			toRemove.add(date);
		}
		
		a.getRentedDates().removeAll(toRemove);
		a.getFreeDates().addAll(toRemove);
		a.getReservations().remove(idOne);
		
		r.setReservationStatus(ReservationStatus.CANCLED);
		
		dao.saveReservation(context.getRealPath(""), dao);
		apartments.saveApartment(context.getRealPath(""), apartments);
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/reservations/approve/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response approve(@PathParam("idOne") String idOne, @Context HttpServletRequest request)
	{
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		Reservation r = dao.findReservationById(UUID.fromString(idOne));
		
		r.setReservationStatus(ReservationStatus.ACCEPTED);
		
		return Response.ok().build();
	}
	
	
}
