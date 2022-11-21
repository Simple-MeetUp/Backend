package groupware.simplemeetup.service;

import groupware.simplemeetup.entity.User;
import groupware.simplemeetup.exception.IllegalTokenException;
import groupware.simplemeetup.exception.InvalidTokenException;
import groupware.simplemeetup.exception.InvalidUserException;
import groupware.simplemeetup.repository.UserRepository;
import groupware.simplemeetup.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String getTokenByHeader(HttpServletRequest httpServletRequest) {
        final String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new IllegalTokenException();

        return authHeader.substring("Bearer ".length());
    }

    public User getUserByToken(HttpServletRequest httpServletRequest) {

        final String token = getTokenByHeader(httpServletRequest);

        if(jwtUtil.isTokenExpired(token)) throw new InvalidTokenException();

        return userRepository.findUserByEmail(jwtUtil.getEmailByToken(token)).orElseThrow(InvalidUserException::new);
    }
}
