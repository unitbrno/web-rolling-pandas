package rolling.pandas.server.service

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import rolling.pandas.server.loggerFor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SpringAuthenticationSuccessHandler : SavedRequestAwareAuthenticationSuccessHandler() {

    private val log = loggerFor(javaClass)

    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse, authentication: Authentication) {

        val userName = authentication.name

        log.info("User: " + userName)

        super.onAuthenticationSuccess(request, response, authentication)
    }
}