package br.com.leonardo.demonstrativo.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.leonardo.demonstrativo.model.Usuario;
import br.com.leonardo.demonstrativo.repository.UsuarioRepository;

@Service
public class AuthenticationService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository repositoryUsuario;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repositoryUsuario.findByEmail(username);
		if(usuario.isPresent()) {
			return usuario.get();
		}
		throw new UsernameNotFoundException("Dados invalido");
	}
	
}
