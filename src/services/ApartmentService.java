package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
import beans.ApartmentComment;
import beans.ApartmentStatus;
import beans.Filter;
import beans.RentPeriod;
import beans.Reservation;
import beans.ReservationStatus;
import beans.User;
import dao.AmenitiesDAO;
import dao.ApartmentCommentDAO;
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
	@Path("/apartments/all-active")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Apartment> getAllActive(@Context HttpServletRequest request) {

		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");

		return dao.getAllActiveApartments();
	}

	@GET
	@Path("/apartments/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOne(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {

		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		Apartment a = dao.findApartmentById(idOne);
		return Response.ok(a).build();
	}
	
	@POST
	@Path("/apartments/filter")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Apartment> filter(Filter filter, @Context HttpServletRequest request) {

		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		ArrayList<Apartment> apartments = dao.filterApartments(filter, dao.getAllActiveApartments());
		return apartments;
	}

	@POST
	@Path("/apartments/adddate/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDates(RentPeriod rp,@PathParam("idOne") String idOne,@Context HttpServletRequest request) {

		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		Apartment a = dao.findApartmentById(idOne);
		boolean isAdded = a.addNewRentDates(rp.from, rp.to);
		if(isAdded) {
		dao.getApartments().put(dao.findApartmentById(idOne).getName(), a);
		context.setAttribute("ApartmentDAO", dao);
		dao.saveApartment(context.getRealPath(""), dao);
		return Response.ok(rp).build();
		}else {
			return Response.serverError().build();
		}

	}
	


	@GET
	@Path("/apartments/allactive/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Apartment> getAllFromHostActive(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {

		System.out.println("in active");

		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");

		ArrayList<Apartment> apps = dao.allActiveApartmentsFromHost(userdao.findbyID(idOne));


		return apps;
	}

	@GET
	@Path("/apartments/inactive/all/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Apartment> getAllFromHostInactive(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {

		System.out.println("in deleted");

		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");

		ArrayList<Apartment> apps = dao.allInactiveApartmentsFromHost(userdao.findbyID(idOne));

		for(Apartment a : apps) {
			System.out.println(a.getName()+" "+a.getStatus());
		}

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
		a.setIdOne(UUID.randomUUID());
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

		System.out.println(a);

		return Response.ok(a).build();

	}


	@DELETE
	@Path("/apartments/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") String id) {
		
		context.setAttribute("ReservationDAO", new ReservationDAO(context.getRealPath("")));
		
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		ReservationDAO resdDao = (ReservationDAO) context.getAttribute("ReservationDAO");
		
		Apartment ap = dao.findById(id);

		if(ap.getReservations().size() != 0) {
			for(String r : ap.getReservations()) {
				Reservation reservation = resdDao.findReservationById(UUID.fromString(r));
				reservation.setReservationStatus(ReservationStatus.CANCLED);
			}
		}

		ap.setStatus(ApartmentStatus.DELETED);
		context.setAttribute("ApartmentDAO", dao);
		dao.saveApartment(context.getRealPath(""), dao);

		
		return Response.ok().build();
	}
	
	
	
	@POST
	@Path("/editapartment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response edit(Apartment a) {		
		ApartmentDAO dao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		context.setAttribute("ApartmentCommentDAO", new ApartmentCommentDAO(context.getRealPath("")));
		ApartmentCommentDAO daoComments = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");
		
		Apartment apartment = dao.findByUUID(a.getIdOne());
		
		if(apartment != null) {
			Apartment obj = dao.getApartments().get(apartment.getName());
			
		if(a.getName() != "") {
			if(!a.getName().equals(apartment.getName())) {
				dao.getApartments().remove(apartment.getName(),apartment);
				for(ApartmentComment comment: daoComments.getAllCommentsFromApartment(apartment.getName())) {
					comment.setApartmentName(a.getName());
				}
				apartment.setName(a.getName());
			}
		}
		
		if(a.getCheckInTime() != "") {
			if(!a.getCheckInTime().equals(apartment.getCheckInTime())) {
				apartment.setCheckInTime(a.getCheckInTime());
			}
		}
		
		if(a.getCheckOutTime() != "") {
			if(!a.getCheckOutTime().equals(apartment.getCheckOutTime())) {
				apartment.setCheckOutTime(a.getCheckOutTime());
			}
		}
		
		if(a.getPrice() > 0) {
			if(a.getPrice() != apartment.getPrice()) {
				apartment.setPrice(a.getPrice());
			}
		}
		
		if(a.getNumberOfGuests() > 0) {
			if(a.getNumberOfGuests() != apartment.getNumberOfGuests()) {
				apartment.setNumberOfGuests(a.getNumberOfGuests());
			}
		}
		
		if(a.getNumberOfRooms() > 0) {
			if(a.getNumberOfRooms() != apartment.getNumberOfRooms()) {
				apartment.setNumberOfRooms(a.getNumberOfRooms());
			}
		}
		
		if(a.getAmenities().size() != 0) {
			if(!a.getAmenities().equals(apartment.getAmenities())) {
				apartment.setAmenities(a.getAmenities());
			}
		}
		
		if(a.getLocation() != null) {
			if(a.getLocation().equals(apartment.getLocation())) {
				apartment.setLocation(a.getLocation());
			}
		}
		
		
		context.setAttribute("ApartmentDAO", dao);
		dao.getApartments().put(a.getName(), obj);
		dao.saveApartment(context.getRealPath(""), dao);
		
		context.setAttribute("ApartmentCommentDAO", daoComments);
		daoComments.saveComment(context.getRealPath(""), daoComments);
	
		return Response.ok(apartment).build();
		} else {
			
			return Response.status(400).build();
			
		}
	}
	

}
