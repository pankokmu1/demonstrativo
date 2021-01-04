package br.com.leonardo.demonstrativo.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.leonardo.demonstrativo.model.Usuario;
import br.com.leonardo.demonstrativo.repository.UsuarioRepository;

public class AuthenticationToken extends OncePerRequestFilter {
	
	
	private TokenService serviceToken;
	
	private UsuarioRepository repositoryUsuario;
	
	

	public AuthenticationToken(TokenService serviceToken,UsuarioRepository repositoryUsuario) {
		super();
		this.serviceToken = serviceToken;
		this.repositoryUsuario = repositoryUsuario;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperarToken(request);
		boolean valido = serviceToken.validarToken(token);
		if(valido) {
			autenticarUsuario(token);
		}
		
		filterChain.doFilter(request, response);
		
	}

	private void autenticarUsuario(String token) {
		Long idUsuario = serviceToken.getIdUsuario(token);
		Usuario usuario = repositoryUsuario.findById(idUsuario).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null,usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;			
		}
		return token.substring(7,token.length());
	}
	
}
