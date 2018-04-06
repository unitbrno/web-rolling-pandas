package rolling.pandas.server.dao

import org.springframework.data.jpa.repository.JpaRepository
import rolling.pandas.server.domain.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): User
}