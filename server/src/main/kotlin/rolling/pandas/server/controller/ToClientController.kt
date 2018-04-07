package rolling.pandas.server.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ToClientController {

    @ResponseBody
    @RequestMapping("/toClient")
    fun get(): String {
        return "<script>window.location.replace(\"http://localhost:4200\");\n</script>"
    }
}