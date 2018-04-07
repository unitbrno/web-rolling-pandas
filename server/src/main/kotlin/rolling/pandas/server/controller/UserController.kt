package rolling.pandas.server.controller

import org.apache.catalina.servlet4preview.http.HttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import rolling.pandas.server.dao.UserRepository
import rolling.pandas.server.domain.SpringIdentity
import rolling.pandas.server.loggerFor
import rolling.pandas.server.service.SpringRequestHelper


data class LoginObj(val login: String, val pass: String) {
    constructor() : this("", "")
}

@RestController
class UserController(private val userRepository: UserRepository, private val authenticationManager: AuthenticationManager, private val springRequestHelper: SpringRequestHelper) {

    private val log = loggerFor(javaClass)

    @GetMapping("/login-api")
    fun getInfo(request: HttpServletRequest): Map<String, String> = springRequestHelper.getTokenInfo(request)

    @PostMapping("/login-api")
    fun post(@RequestBody loginObj: LoginObj, request: HttpServletRequest): SpringIdentity {

        val username = loginObj.login
        val pass = loginObj.pass

        val token = UsernamePasswordAuthenticationToken(username, pass)

        request.getSession()

        token.details = WebAuthenticationDetails(request)

        val authentication = authenticationManager.authenticate(token)

        SecurityContextHolder.getContext().authentication = authentication
        return SpringIdentity(username, authentication.isAuthenticated)
    }
}