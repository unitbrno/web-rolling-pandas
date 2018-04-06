package rolling.pandas.server.service.facebook

import facebook4j.Facebook
import facebook4j.FacebookFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacebookConfig {
    @Bean
    fun facebook(): Facebook = FacebookFactory().instance
}