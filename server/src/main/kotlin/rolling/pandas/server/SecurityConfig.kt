package rolling.pandas.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import rolling.pandas.server.service.PandaUserDetailsService

@EnableWebSecurity
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class SecurityConfig(private val pandaUserDetailsService: PandaUserDetailsService) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.formLogin()
                .loginProcessingUrl("/mlogin")
                .usernameParameter("login")
                .passwordParameter("pass")
                .successForwardUrl("/toClient")
                .and()
                .authorizeRequests()
                .antMatchers("/index.html", "/", "home", "/mlogin").permitAll()
                .anyRequest()
                .authenticated().and().csrf().disable()

        //csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(pandaUserDetailsService)
        daoAuthenticationProvider.setPasswordEncoder(encoder())
        return daoAuthenticationProvider
    }

    @Bean
    fun encoder() = BCryptPasswordEncoder(11)

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        //auth.authenticationProvider(authenticationProvider())
        auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER")
    }
}