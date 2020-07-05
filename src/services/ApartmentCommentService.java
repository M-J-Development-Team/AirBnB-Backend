package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

import beans.Amenities;
import beans.AmenityStatus;
import beans.Apartment;
import beans.ApartmentComment;
import beans.CommentStatus;
import beans.User;
import dao.AmenitiesDAO;
import dao.ApartmentCommentDAO;
import dao.ApartmentDAO;
import dao.UserDAO;

@Path("")
public class ApartmentCommentService {

	@Context
	ServletContext context;
	
	@PostConstruct 
	public void init() {
		
		if(context.getAttribute("ApartmentCommentDAO") == null) {
			String contextPath = context.getRealPath("");
			context.setAttribute("ApartmentCommentDAO", new ApartmentCommentDAO(contextPath));
		}		
	}
	
	
	@POST
	@Path("/submit-comment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addComment(ApartmentComment comment, @Context HttpServletRequest request)
	{	
		context.setAttribute("ApartmentDAO", new ApartmentDAO(context.getRealPath("")));
		ApartmentCommentDAO apartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");
		ApartmentDAO apartmentDAO = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		Apartment a = apartmentDAO.findApartmentById(comment.getApartmentId().toString());
		
		if(a.getComments().size() != 0) {
		float total =0;
		
		for(String c : a.getComments()) {
			ApartmentComment apCom =  apartmentCommentDAO.findById(UUID.fromString(c));
			total = total + apCom.getRating();		
		}
		
		total = total + comment.getRating();
		double rating = total/(a.getComments().size()+1);
		a.setRating(rating);
		}else {
			a.setRating(comment.getRating());
		}
		
		a.getComments().add(comment.getIdOne().toString());
		apartmentCommentDAO.getComments().put(comment.getIdOne().toString(), comment);
		
		context.setAttribute("ApartmentCommentDAO", apartmentCommentDAO);
		apartmentCommentDAO.saveComment(context.getRealPath(""), apartmentCommentDAO);
		
		context.setAttribute("ApartmentDAO", apartmentDAO);
		apartmentDAO.saveApartment(context.getRealPath(""), apartmentDAO);
		

		
		return Response.ok().build();
		
	}
	
	
	@GET
	@Path("/get-all-comments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ApartmentComment> getAllComments(@Context HttpServletRequest request) {
		
		ApartmentCommentDAO apartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");				
		return apartmentCommentDAO.getAllComments();
	
	}
	
	@GET
	@Path("/get-all-approved-by-apartment/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ApartmentComment> getAllCommentsByApartmentApproved(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {
		
		ApartmentCommentDAO apartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");	
		return apartmentCommentDAO.getAllCommentsFromApartmentApproved(UUID.fromString(idOne));
	}
	
	@GET
	@Path("/get-all-unapproved-for-host/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ApartmentComment> getAllCommentsUnapproved(@PathParam("username") String username,@Context HttpServletRequest request) {
		
		ApartmentCommentDAO apartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");
		context.setAttribute("UserDAO", new UserDAO(context.getRealPath("")));
		context.setAttribute("ApartmentDAO", new ApartmentDAO(context.getRealPath("")));
		
		UserDAO userdao = (UserDAO) context.getAttribute("UserDAO");
		ApartmentDAO apartmentdao = (ApartmentDAO) context.getAttribute("ApartmentDAO");
		
		User host = userdao.findUserByUsername(username);
		ArrayList<Apartment> hostApartmentsActive = apartmentdao.allActiveApartmentsFromHost(host);
		
		return apartmentCommentDAO.getAllCommentsUnapprovedForHost(username,hostApartmentsActive);
	
	}
	
	@GET
	@Path("/get-all-comments-by-apartment/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ApartmentComment> getAllCommentsByApartment(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {
		
		ApartmentCommentDAO apartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");				
		return apartmentCommentDAO.getAllCommentsFromApartment(UUID.fromString(idOne));
	
	}
	
	@POST
	@Path("/set-comment-visible/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setCommentToVisible(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {
		
		ApartmentCommentDAO ApartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");		
		if(ApartmentCommentDAO.setCommentToVisible(UUID.fromString(idOne))){
			context.setAttribute("ApartmentCommentDAO", ApartmentCommentDAO);
			ApartmentCommentDAO.saveComment(context.getRealPath(""), ApartmentCommentDAO);	
			
			return Response.ok().build();
		}else {
			return Response.serverError().build();
		}
	
	}
	
	@POST
	@Path("/set-comment-declined/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setCommentToDeclined(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {
		
		ApartmentCommentDAO ApartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");		
		if(ApartmentCommentDAO.setCommentToDeclined(UUID.fromString(idOne))){
			context.setAttribute("ApartmentCommentDAO", ApartmentCommentDAO);
			ApartmentCommentDAO.saveComment(context.getRealPath(""), ApartmentCommentDAO);	
			
			return Response.ok().build();
		}else {
			return Response.serverError().build();
		}
	
	}
	

	
}
