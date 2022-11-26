package smart.parking.cot.security;

import smart.parking.cot.Entity.RoleDTO;
import smart.parking.cot.Entity.UserDTO;
import smart.parking.cot.security.SecurityService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("signup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SecurityResource {

    @Inject
    private SecurityService service;
    @POST
    public void create(@Valid UserDTO userDTO) {
        service.create(userDTO);
    }




    @DELETE
    @Path("user/{id}")
    @RolesAllowed("ADMIN")
    public void delete(@PathParam("id") String id) {
        service.delete(id);
    }

    @Path("{id}")
    @PUT
    public void changePassword(@PathParam("id") String id, @Valid UserDTO dto) {
        service.updatePassword(id, dto);
    }

    @Path("roles/{id}")
    @PUT
    @RolesAllowed("ADMIN")
    public void addRole(@PathParam("id") String id, RoleDTO dto){
        service.addRole(id, dto);
    }

    @Path("roles/{id}")
    @DELETE
    @RolesAllowed("ADMIN")
    public void removeRole(@PathParam("id") String id, RoleDTO dto){
        service.removeRole(id, dto);
    }

    @Path("me")
    @GET
    public UserDTO getMe() {
        return service.getUser();
    }

    @Path("users")
    @GET
    @RolesAllowed("ADMIN")
    public List<UserDTO> getUsers() {
        return service.getUsers();
    }

    @DELETE
    @PermitAll
    @Path("me")
    public void removeUser() {
        service.removeUser();
    }

    @DELETE
    @RolesAllowed("ADMIN")
    @Path("{id}")
    public void removeUser(@PathParam("id") String id) {
        service.removeUser(id);
    }

    @DELETE
    @PermitAll
    @Path("token/{token}")
    public void removeToken(@PathParam("token") String token) {
        service.removeToken(token);
    }
}
