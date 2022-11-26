package smart.parking.cot.security.oauth2;

import smart.parking.cot.Repository.UserTokenRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class Oauth2Authentication implements HttpAuthenticationMechanism {

    private static final Pattern CHALLENGE_PATTERN
            = Pattern.compile("^Bearer *([^ ]+) *$", Pattern.CASE_INSENSITIVE);

    @Inject
    private UserTokenRepository repository;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
                                                HttpMessageContext httpMessageContext) {


        final String authorization = request.getHeader("Authorization");

        Matcher matcher = CHALLENGE_PATTERN.matcher(Optional.ofNullable(authorization).orElse(""));
        if (!matcher.matches()) {
            return httpMessageContext.doNothing();
        }
        final String token = matcher.group(1);

        final Optional<AccessToken> optional = repository.findByAccessToken(token)
                .flatMap(u -> u.findAccessToken(token));

        if (!optional.isPresent()) {
            return httpMessageContext.responseUnauthorized();
        }

        final AccessToken accessToken = optional.get();
        final Optional<UserJWT> optionalUserJWT = UserJWT.parse(accessToken.getToken(), accessToken.getJwtSecret());
        if (optionalUserJWT.isPresent()) {
            final UserJWT userJWT = optionalUserJWT.get();
            return httpMessageContext.notifyContainerAboutLogin(userJWT.getUser(), userJWT.getRoles());
        } else {
            return httpMessageContext.responseUnauthorized();
        }
    }
}
