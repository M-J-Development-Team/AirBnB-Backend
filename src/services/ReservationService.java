package services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import beans.ApartmentComment;
import beans.ApartmentStatus;
import beans.Reservation;
import beans.ReservationStatus;
import beans.Role;
import beans.User;
import dao.ApartmentCommentDAO;
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
			if(LocalDate.parse(r.getReservedTill()).isBefore(LocalDate.now()) && r.getReservationStatus().equals(ReservationStatus.ACCEPTED)) {
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
	@Path("/reservations/all/{username}")
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
		
		User host = userdao.findUserByUsername(a.getHost());
		host.getReservations().add(r.getIdOne().toString());
		
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
	@Path("/reservation/sortGuest/{way}/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Reservation> sort(@PathParam("way") String way, @PathParam("username") String username, @Context HttpServletRequest request) {
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		ArrayList<Reservation> reservations = dao.allMyReservations(username);
		
		if(way.equals("ascending")) {
			Collections.sort(reservations);
		} else if (way.equals("descending")) {
			Collections.sort(reservations, Collections.reverseOrder());
		}
		
		return reservations;
		
	}
	
	@POST
	@Path("/reservation/sortHost/{way}/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Reservation> sortHost(@PathParam("way") String way, @PathParam("username") String username, @Context HttpServletRequest request) {
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");
		
		User host = userdao.findUserByUsername(username);
		ArrayList<Reservation> reservations = dao.allReservationsForMyApartments(host);
		if(way.equals("ascending")) {
			Collections.sort(reservations);
		} else if (way.equals("descending")) {
			Collections.sort(reservations, Collections.reverseOrder());
		}
		
		return reservations;
		
	}
	
	@POST
	@Path("/reservation/sortAdmin/{way}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Reservation> sortAdmin(@PathParam("way") String way, @Context HttpServletRequest request) {
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		
		ArrayList<Reservation> reservations = dao.allReservations();
		if(way.equals("ascending")) {
			Collections.sort(reservations);
		} else if (way.equals("descending")) {
			Collections.sort(reservations, Collections.reverseOrder());
		}
		
		return reservations;
		
	}
	
	@POST
	@Path("/reservation/filter/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Reservation> filterByStatusAdmin(@PathParam("status") String status, @Context HttpServletRequest request) {
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		
		if(status.equals("CREATED")) {
			return dao.createdReservations();
		} else if(status.equals("COMPLETED")) {
			return dao.completedReservations();
		} else if(status.equals("DENIED")) {
			return dao.deniedReservations();
		} else if(status.equals("ACCEPTED")) {
			return dao.acceptedReservations();
		} else if(status.equals("CANCELED")) {
			return dao.cancelledReservations();
		}
		
		return dao.allReservations();
		
	}
	
	@POST
	@Path("/reservation/filterGuest/{status}/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Reservation> filterByStatusGuest(@PathParam("status") String status, @PathParam("username") String username, @Context HttpServletRequest request) {
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		
		ArrayList<Reservation> reservations = dao.allMyReservations(username);
		ArrayList<Reservation> toReturn = new ArrayList<Reservation>();
		
		if(status.equals("CREATED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.CREATED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		} else if(status.equals("COMPLETED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.COMPLETED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		} else if(status.equals("DENIED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.DENIED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		} else if(status.equals("ACCEPTED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.ACCEPTED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		} else if(status.equals("CANCELED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.CANCLED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		}
		
		return dao.allMyReservations(username);
		
	}
	
	@POST
	@Path("/reservation/filterHost/{status}/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Reservation> filterByStatusHost(@PathParam("status") String status, @PathParam("username") String username, @Context HttpServletRequest request) {
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");
		
		User host = userdao.findUserByUsername(username);
		ArrayList<Reservation> reservations = dao.allReservationsForMyApartments(host);
		ArrayList<Reservation> toReturn = new ArrayList<Reservation>();
		
		if(status.equals("CREATED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.CREATED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		} else if(status.equals("COMPLETED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.COMPLETED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		} else if(status.equals("DENIED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.DENIED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		} else if(status.equals("ACCEPTED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.ACCEPTED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		} else if(status.equals("CANCELED")) {
			for(Reservation r : reservations) {
				if(r.getReservationStatus().equals(ReservationStatus.CANCLED)) {
					toReturn.add(r);
				}
			}
			return toReturn;
		}
		
		return dao.allReservationsForMyApartments(host);
		
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
	@Path("/reservations/deny/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deny(@PathParam("idOne") String idOne, @Context HttpServletRequest request)
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
		
		r.setReservationStatus(ReservationStatus.DENIED);
		
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
	
	@GET
	@Path("/reservations/all-my-guests/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getAllMyGuests(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {
		
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		ArrayList<Reservation> r = dao.allReservations();
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");
		ApartmentDAO apartmentdao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		User host = userdao.findbyID(idOne);
		ArrayList<Apartment> hostApartmentsActive = apartmentdao.allActiveApartmentsFromHost(host);
		ArrayList<User> guests = dao.allGuestsForMyApartments(host.getUsername(),hostApartmentsActive , userdao,apartmentdao);
		return guests;
		
	}
	
	@GET
	@Path("/reservations/all-hosts-reservations/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Reservation> getAllHostsReservations(@PathParam("username") String username,@Context HttpServletRequest request) {
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");
		
		User host = userdao.findUserByUsername(username);
		return dao.allReservationsForMyApartments(host);
	}
	
	@GET
	@Path("/check-can-comment/{idOne}/{apartmentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkIfCanComment(@PathParam("idOne") String idOne,@PathParam("apartmentId") String apartmentId,@Context HttpServletRequest request) {
		
		ReservationDAO dao = (ReservationDAO) context.getAttribute("ReservationDAO");
		context.setAttribute("ApartmentDAO", new ApartmentDAO(context.getRealPath("")));
		ApartmentDAO apartmentdao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		context.setAttribute("ApartmentCommentDAO", new ApartmentCommentDAO(context.getRealPath("")));
		ApartmentCommentDAO apartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");
		
		context.setAttribute("UserDAO", new UserDAO(context.getRealPath("")));
		UserDAO userDAO = (UserDAO) context.getAttribute("UserDAO");
		
		User guest = userDAO.findbyID(idOne);
		ArrayList<String> s = apartmentdao.findApartmentById(apartmentId).getReservations();		
		ArrayList<Reservation> reservationsList = new ArrayList<Reservation>();
		
		for(String r : apartmentdao.findApartmentById(apartmentId).getReservations()) {
			Reservation res = dao.findReservationById(UUID.fromString(r));
			if((res.getReservationStatus().equals(ReservationStatus.DENIED) || res.getReservationStatus().equals(ReservationStatus.COMPLETED)) && res.getGuest().equals(guest.getUsername())) {
				if(!apartmentCommentDAO.checkHasCommented(idOne, apartmentId, context)) {
				return Response.ok().build();
				}else {
					return Response.serverError().build();
				}
			}
		}
		
		
		return  Response.serverError().build();
	
	}
	

}
