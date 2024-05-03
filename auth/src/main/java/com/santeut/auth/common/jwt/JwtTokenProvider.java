package com.santeut.auth.common.jwt;

import com.santeut.auth.common.exception.DataNotFoundException;
import com.santeut.auth.dto.response.JwtTokenResponseDto;
import com.santeut.auth.entity.RefreshToken;
import com.santeut.auth.repository.RefreshTokenRepository;
import com.santeut.auth.common.response.ResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenProvider {


    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${jwt.accessToken}")
    private Long accessTokenExpired;

    @Value("${jwt.refreshToken}")
    private Long refreshTokenExpired;

    private final RefreshTokenRepository refreshTokenRepository;

//    @Autowired
//    private Environment env;


    // 토큰 유효성 검증
    public boolean validateToken(String token, UserDetails userDetails){

        String userLoginId = extractUserLoginId(token);

        if (expiredToken(token)) throw new DataNotFoundException(ResponseCode.INVALID_ACCESS_TOKEN);

        return userLoginId.equals(userDetails.getUsername());
    }

    // 토큰 만료
    private boolean expiredToken (String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration (String token) {
        return extractAllClaims(token).getExpiration();
    }

    // 토큰에 있는 모든 클레임 추출
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 인코딩된 BASE64 값을 디코딩 후 Jwt키 값으로 생성
    private Key getKey() {
//        String jwtSecretKey = jwtSecretKey;
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    // 클레임 중에서 Subject 추출
    public String extractUserLoginId(String token){
        return extractAllClaims(token).getSubject();
    }

    // 사용자의 Header에서 Token 추출
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    // 사용자의 아이디, 발급 시간, 만료 시간 고려해서 JWT 키 생성
    private String generateToken(String userLoginId, Long expiredTime){

        return Jwts.builder()
                .subject(userLoginId)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + expiredTime))
                .signWith(getKey())
                .compact();
    }

    // 키 발급
    public JwtTokenResponseDto issueToken(String userLoginId){

//        Long accessTokenExpired = Long.parseLong(env.getProperty("jwt.accessToken"));
//        Long refreshTokenExpired = Long.parseLong(env.getProperty("jwt.refreshToken"));

        String accessToken = generateToken(userLoginId, accessTokenExpired);
        String refreshToken = generateToken(userLoginId, refreshTokenExpired);

        return new JwtTokenResponseDto("Bearer", accessToken, refreshToken);
    }

    // 토큰 재발급
    public JwtTokenResponseDto reissueToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);

        if(token == null){
            throw new DataNotFoundException(ResponseCode.INVALID_REFRESH_TOKEN);
        }

        String userLoginId = extractUserLoginId(token);

        if(userLoginId == null){
            throw new DataNotFoundException(ResponseCode.NOT_EXISTS_USER);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new DataNotFoundException(ResponseCode.INVALID_REFRESH_TOKEN));

        String checkId = extractUserLoginId(refreshToken.getRefreshToken());

        log.debug("userLoginId : "+ userLoginId +" / checkId : "+ checkId);

        // 현재 로그인한 사용자의 RefreshToken이랑 redis에 저장된 refreshToken이랑 일치하는지 비교
        if(!token.equals(refreshToken.getRefreshToken())){
            throw new DataNotFoundException(ResponseCode.NOT_MATCH_REFRESH_TOKEN);
        }

        return issueToken(userLoginId);
    }
}
