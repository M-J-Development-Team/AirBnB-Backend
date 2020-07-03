package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Amenities;
import beans.AmenityStatus;
import beans.ApartmentComment;
import dao.AmenitiesDAO;
import dao.ApartmentCommentDAO;

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
		ApartmentCommentDAO apartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");
				

		apartmentCommentDAO.getComments().put(comment.getIdOne().toString(), comment);
		context.setAttribute("ApartmentCommentDAO", apartmentCommentDAO);
		
		apartmentCommentDAO.saveComment(context.getRealPath(""), apartmentCommentDAO);
		
		return Response.ok().build();
		
	}
	
}
