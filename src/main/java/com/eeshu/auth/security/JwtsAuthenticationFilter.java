package com.eeshu.auth.security;

import com.eeshu.auth.repository.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtsAuthenticationFilter extends OncePerRequestFilter {
    private  final  JwtService jwtService;
    private  final UserRepository userRepository;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header=request.getHeader("Authorization");
        log.debug("Authorization header: {}", header);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


            String token=header.substring(7);
            try {
                Jws<Claims> claims = jwtService.parse(token);
                Claims payload = claims.getPayload();

                String userId = claims.getPayload().getSubject();
                UUID uuid = UUID.fromString(userId);
                userRepository.findById(uuid).ifPresent(user -> {
                    //user milchuka h ab hame
                    List<GrantedAuthority> authorities = user.getRoles() == null ? List.of() : user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(user.getEmail(),null,authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
                boolean b = jwtService.validateToken(token);
            } catch (ExpiredJwtException e) {
                e.printStackTrace();

            }catch (MalformedJwtException e){
                e.printStackTrace();

            }catch (JwtException e){
                e.printStackTrace();

            }catch (Exception e){
                e.printStackTrace();

            }

       filterChain.doFilter(request,response);
    }
}
