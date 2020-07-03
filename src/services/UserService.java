package services;

import java.util.ArrayList;
import java.util.Collection;
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
import beans.Apartment;

import beans.Role;
import beans.User;
import dao.UserDAO;

@Path("")
public class UserService {
	
	@Context
	ServletContext context;
	
	@PostConstruct 
	public void init() {
		
		if(context.getAttribute("UserDAO") == null) {
			String contextPath = context.getRealPath("");
			context.setAttribute("UserDAO", new UserDAO(contextPath));
		}
	}
	

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User u, @Context HttpServletRequest request) {
	
		UserDAO users = (UserDAO) context.getAttribute("UserDAO");
		
		User user = users.find(u);
		
		if(user == null) {
			return Response.status(400).build();
		}
		
		String idOne = UUID.randomUUID().toString();
		user.setIdOne(idOne);		
		
		context.setAttribute("UserDAO", users);
		
		users.saveUser(context.getRealPath(""), users);
		
		return Response.ok(idOne).build();		
		
	}
	
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(User u, @Context HttpServletRequest request) {

		UserDAO users = (UserDAO) context.getAttribute("UserDAO");
		System.out.println(users);
		
		if(users.getUsers().containsKey(u.getUsername())) {
			return Response.status(400).build();
		}
		
		u.setRole(Role.GUEST);
		users.getUsers().put(u.getUsername(), u);
		context.setAttribute("UserDAO", users);
		
		users.saveUser(context.getRealPath(""), users);
		
		return Response.ok().build();
		
	}
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void logout(String idOne, @Context HttpServletRequest request) {
		
		UserDAO users = (UserDAO) context.getAttribute("UserDAO"); 		
		
		User user = users.findbyID(idOne);
		user.setIdOne("");
		
		context.setAttribute("UserDAO", users);
		
		users.saveUser(context.getRealPath(""), users);
		
	}
	

	@GET
	@Path("/userinfo/{idOne}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserData(@PathParam("idOne") String idOne,@Context HttpServletRequest request) {
		
		
		UserDAO users = (UserDAO) context.getAttribute("UserDAO");
		
		User user = users.findbyID(idOne);
		
			
		if(user == null)
		{
			return Response.status(400).build();
		}
		
		return Response.ok(user).build();	
	}
	
	@GET
	@Path("/allusers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<User> getAllUsers(@Context HttpServletRequest request) {
		
		UserDAO users = (UserDAO) context.getAttribute("UserDAO");
	
		
		return users.getUsers().values();
	}
	
	
	@POST
	@Path("/addhost")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addHost(User u, @Context HttpServletRequest request) {
		
		UserDAO users = (UserDAO) context.getAttribute("UserDAO");
		System.out.println(users);
		
		if(users.getUsers().containsKey(u.getUsername())) {
			return Response.status(400).build();
		}
		
		u.setRole(Role.HOST);
		users.getUsers().put(u.getUsername(), u);
		context.setAttribute("UserDAO", users);
		
		users.saveUser(context.getRealPath(""), users);
		
		return Response.ok().build();

		
	}
	
	@POST
	@Path("/getallhostsactive/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMyActiveApartments(@PathParam("id") String id, @Context HttpServletRequest request) {
		
		UserDAO users = (UserDAO) context.getAttribute("UserDAO");
		
		User user = users.findbyID(id);
		
		if(user == null)
		{
			return Response.status(400).build();
		}
		
		ArrayList<Apartment> usersActiveApartments = users.myActiveApartments(user);
		
		context.setAttribute("ApartmentDAO", usersActiveApartments);
		
		return Response.ok(usersActiveApartments).build();
	}
	
	@POST
	@Path("/getallhostsinactive/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllMyInactiveApartments(@PathParam("id") String id, @Context HttpServletRequest request) {
		
		UserDAO users = (UserDAO) context.getAttribute("UserDAO");
		
		User user = users.findbyID(id);
		
		if(user == null)
		{
			return Response.status(400).build();
		}
		
		ArrayList<Apartment> usersInctiveApartments = users.myInactiveApartments(user);
		
		context.setAttribute("ApartmentDAO", usersInctiveApartments);
		
		return Response.ok(usersInctiveApartments).build();
	}
	
	@POST
	@Path("/edituser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response edit(User user) {
		
		System.out.println("editovan:"+user.getUsername());
		
		UserDAO dao = (UserDAO) context.getAttribute("UserDAO");
		
		User u = dao.findUserByUsername(user.getUsername());
		
		
		if(u != null) {
		
		if(user.getName() != "") {
			if(!user.getName().equals(u.getName())) {
				u.setName(user.getName());
			}
		}
		
		if(user.getLastname() != "") {
			if(!user.getLastname().equals(u.getLastname())) {
				u.setLastname(user.getLastname());
			}
		}
		
		if(user.getPassword() != "") {
			if(!user.getPassword().equals(u.getPassword())) {
				u.setPassword(user.getPassword());
			}
		}
		
		if(user.getGender() != null) {
			if(!user.getGender().equals(u.getGender())) {
				u.setGender(user.getGender());
			}
		}
		
		
		context.setAttribute("UserDAO", dao);
		dao.saveUser(context.getRealPath(""), dao);
		
		return Response.ok(u).build();
		} else {
			
			return Response.status(400).build();
			
		}
	}
	



}
