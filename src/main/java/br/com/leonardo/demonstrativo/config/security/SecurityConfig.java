package br.com.leonardo.demonstrativo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.leonardo.demonstrativo.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private AuthenticationService serviceAutenticacao;
	
	@Autowired
	private TokenService serviceToken;
	
	@Autowired
	private UsuarioRepository repositoryUsuario;
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	 @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.userDetailsService(serviceAutenticacao).passwordEncoder(new BCryptPasswordEncoder());
		
	}
	 
	 @Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests()
		 .antMatchers(HttpMethod.GET ,"/topicos").permitAll()
		 .antMatchers(HttpMethod.GET ,"/topicos/*").permitAll()
		 .antMatchers(HttpMethod.GET ,"/cursos").permitAll()
		 .antMatchers(HttpMethod.POST ,"/auth").permitAll()
		 .antMatchers(HttpMethod.GET ,"/actuator/**").permitAll() //temporario
		 .antMatchers(HttpMethod.DELETE ,"/topicos/*").hasRole("INSTRUTOR")
		 .anyRequest().authenticated()
		 .and().csrf().disable()
		 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		 .and().addFilterBefore(new AuthenticationToken(serviceToken,repositoryUsuario), UsernamePasswordAuthenticationFilter.class);
	}
	 
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
        .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
		
	}
	
}
