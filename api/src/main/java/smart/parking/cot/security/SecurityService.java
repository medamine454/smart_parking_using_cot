package smart.parking.cot.security;

import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;
import smart.parking.cot.Entity.Role;
import smart.parking.cot.Entity.RoleDTO;
import smart.parking.cot.Entity.User;
import smart.parking.cot.Entity.UserDTO;
import smart.parking.cot.Repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class SecurityService {

    @Inject
    @Database(DatabaseType.DOCUMENT)
    private UserRepository repository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private Event<RemoveUser> removeUserEvent;

    @Inject
    private Event<RemoveToken> removeTokenEvent;

    public void create(UserDTO userDTO) {
        if (repository.existsById(userDTO.getEmail())) {
            throw new UserAlreadyExistException("There is an user with this id: " + userDTO.getEmail());
        } else {
            User user = User.builder()
                    .withPasswordHash(passwordHash)
                    .withPassword(userDTO.getPassword())
                    .withEmail(userDTO.getEmail())
                    .withPhonenumber(userDTO.getPhonenumber())
                    .withRoles(getRole())
                    .build();
            repository.save(user);
        }
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public void updatePassword(String id, UserDTO dto) {

        final Principal principal = securityContext.getCallerPrincipal();
        if (isForbidden(id, securityContext, principal)) {
            throw new UserForbiddenException();
        }

        final User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.updatePassword(dto.getPassword(), passwordHash);
        repository.save(user);
    }


    public void addRole(String id, RoleDTO dto) {
        final User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.add(dto.getRoles());
        repository.save(user);

    }


    public void removeRole(String id, RoleDTO dto) {
        final User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.remove(dto.getRoles());
        repository.save(user);
    }

    public UserDTO getUser() {
        final User user = getLoggedUser();
        UserDTO dto = toDTO(user);
        return dto;
    }

    public List<UserDTO> getUsers() {
        return repository.findAll()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public User findBy(String username, String password) {
        final User user = repository.findById(username)
                .orElseThrow(() -> new UserNotAuthorizedException());

        if (passwordHash.verify(password.toCharArray(), user.getPassword())) {
            return user;
        }
        throw new UserNotAuthorizedException();

    }

    public User findBy(String username) {
        return repository.findById(username)
                .orElseThrow(() -> new UserNotAuthorizedException());
    }

    public void removeUser() {
        final User user = getLoggedUser();
        RemoveUser removeUser = new RemoveUser(user);
        removeUserEvent.fire(removeUser);
        repository.deleteById(user.getEmail());
    }

    public void removeUser(String userId) {
        final User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        RemoveUser removeUser = new RemoveUser(user);
        removeUserEvent.fire(removeUser);
        repository.deleteById(user.getEmail());
    }

    public void removeToken(String token) {
        final User loggedUser = getLoggedUser();
        RemoveToken removeToken = new RemoveToken(loggedUser, token);
        removeTokenEvent.fire(removeToken);
    }

    private User getLoggedUser() {
        final Principal principal = securityContext.getCallerPrincipal();
        if (principal == null) {
            throw new UserNotAuthorizedException();
        }
        return repository.findById(principal.getName())
                .orElseThrow(() -> new UserNotFoundException(principal.getName()));
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        return dto;
    }

    private Set<Role> getRole() {
        if (repository.count() == 0) {
            return Collections.singleton(Role.ADMIN);
        } else {
            return Collections.singleton(Role.USER);
        }
    }

    private boolean isForbidden(String id, SecurityContext context, Principal principal) {
        return !(context.isCallerInRole(Role.ADMIN.name()) || id.equals(principal.getName()));
    }


}
