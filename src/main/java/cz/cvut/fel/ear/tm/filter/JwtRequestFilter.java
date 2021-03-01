package cz.cvut.fel.ear.tm.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.ear.tm.response.ErrorInfo;
import cz.cvut.fel.ear.tm.rest.RestExceptionHandler;
import cz.cvut.fel.ear.tm.service.MyUserDetailsService;
import cz.cvut.fel.ear.tm.service.UserService;
import cz.cvut.fel.ear.tm.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;


        if(authorizationHeader != null){
            if(authorizationHeader.startsWith("Bearer ")){
                try{
                    jwt = authorizationHeader.substring(7);
                    username = jwtUtil.extractUsername(jwt);
                } catch (ExpiredJwtException e){
                    ResponseEntity<ErrorInfo> responseEntity = restExceptionHandler.expiredJwt(request, e);

                    // build response
                    response.setStatus(responseEntity.getStatusCode().value());
                    response.setContentType("application/json");

                    ObjectMapper mapper = new ObjectMapper();
                    PrintWriter out = response.getWriter();
                    out.print(mapper.writeValueAsString(responseEntity.getBody()));
                    out.flush();
                    return;
                }
            }
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
