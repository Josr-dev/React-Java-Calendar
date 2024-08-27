package com.rodrigo.calendar.auth.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.calendar.auth.TokenJwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter{

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            
            String header = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);
            if ( header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN) ){
                chain.doFilter(request, response);
                return;
            }
            String token = header.replace(TokenJwtConfig.PREFIX_TOKEN, "");
            // byte[] tokenDecodebytes = Base64.getDecoder().decode(token);
            // String tokenDecode = new String(tokenDecodebytes);
            // String[] tokenArray = tokenDecode.split("\\.");
            // String secretKey = tokenArray[0];
            // String username = tokenArray[1];

            try {

                Claims claims = Jwts
                    .parser()
                    .verifyWith(TokenJwtConfig.SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

                String username = claims.getSubject();
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null , authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            } catch(JwtException e) {
                Map<String, String> bodyResponse = new HashMap<>();
                bodyResponse.put("error", e.getMessage());
                bodyResponse.put("message", "El token no es valido");
                response.getWriter().write(new ObjectMapper().writeValueAsString(bodyResponse));
                response.setStatus(401);
                response.setContentType("application/json");
            }
    }
}
