package rolling.pandas.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import rolling.pandas.server.dao.UserRepository
import rolling.pandas.server.domain.User
import rolling.pandas.server.loggerFor
import java.security.Principal

@RestController
class UserController(private val userRepository: UserRepository) {

    private val log = loggerFor(javaClass)

    @GetMapping("/user")
    fun user(principal: Principal): Principal {
        return principal
    }

    @PostMapping("/user")
    fun post(@RequestBody user: User) {
        log.info("saving user: $user")
        userRepository.save(user)
    }
}