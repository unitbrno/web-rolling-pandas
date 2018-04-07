package rolling.pandas.server.service

import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Service
import rolling.pandas.server.loggerFor
import java.util.*
import javax.servlet.http.HttpServletRequest


@Service
class SpringRequestHelper {

    private val log = loggerFor(javaClass)

    fun getTokenInfo(request: HttpServletRequest): Map<String, String> {
        val map = HashMap<String, String>()
        val csrf = request.getAttribute(CsrfToken::class.java
                .name) as CsrfToken
        log.info("csrf: {}", csrf.token)
        map.put("_csrf.token", csrf.token)
        map.put("_csrf.header", csrf.headerName)
        map.put("_csrf.parameterName", csrf.parameterName)

        return map
    }
}