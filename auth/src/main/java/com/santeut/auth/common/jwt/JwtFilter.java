package com.santeut.auth.common.jwt;

import com.santeut.auth.common.exception.DataNotFoundException;
import com.santeut.auth.common.response.ResponseCode;
import com.santeut.auth.common.userDetail.CustomUserDetailsService;
import com.santeut.auth.entity.UserEntity;
import com.santeut.auth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    // HTTP 요청 시 사용자의 토큰을 꺼내서 유효한 토큰인지 검증하는 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String token = jwtTokenProvider.getTokenFromRequest(request);
        response.setContentType("text/html;charset=UTF-8");

        log.debug("token: "+ token);
        if (token == null){
            filterChain.doFilter(request, response);
            return;
        }

        String userId = jwtTokenProvider.extractUserId(token);
// && SecurityContextHolder.getContext().getAuthentication() == null
        if(userId != null){

            UserEntity userEntity = userRepository.findByUserId(Integer.parseInt(userId))
                    .orElseThrow(() -> new DataNotFoundException(ResponseCode.NOT_EXISTS_USER));

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEntity.getUserLoginId());
            log.trace("userDetails: "+ userDetails.getUsername());

            log.trace("ValidateToken: "+jwtTokenProvider.validateToken(token, userDetails));
            if(jwtTokenProvider.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
