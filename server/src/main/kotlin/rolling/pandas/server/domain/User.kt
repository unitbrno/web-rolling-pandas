package rolling.pandas.server.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue
        val id: Long
) : UserDetails {
    var login: String = ""
    var pass: String = ""
    val email: String = ""
    var firstName: String = ""
    var lastName: String = ""
    @ElementCollection(fetch = FetchType.EAGER)
    var roles: Set<String> = emptySet()

    @OneToMany
    var savedRoutes: List<SavedRoute> = emptyList()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles.map { GrantedAuthority { it } }.toMutableList()

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = login

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = pass

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
    override fun toString(): String {
        return "User(id=$id, login='$login', email='$email', firstName='$firstName', lastName='$lastName', roles=$roles, savedRoutes=$savedRoutes)"
    }

}