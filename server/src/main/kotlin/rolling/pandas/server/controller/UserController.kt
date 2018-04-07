package rolling.pandas.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserController {

    @GetMapping("/user")
    fun user(principal: Principal): Principal {
        return principal
    }
}