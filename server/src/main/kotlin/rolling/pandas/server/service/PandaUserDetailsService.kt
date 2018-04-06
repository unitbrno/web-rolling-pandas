package rolling.pandas.server.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import rolling.pandas.server.dao.UserRepository

@Service
class PandaUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails = userRepository.findByLogin(username ?: "")
}