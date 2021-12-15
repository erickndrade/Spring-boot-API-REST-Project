package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.src.main.java.br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

//    private static final long expirationTime = 86400000;

    @Value("${forum.jwt.expiration}")
    private Long expirationTime;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {

        Usuario logado = (Usuario) authentication.getPrincipal();

        return Jwts.builder()
                .setIssuer("API do Fórum da Alura")
                .setSubject(logado.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();


    }
}
