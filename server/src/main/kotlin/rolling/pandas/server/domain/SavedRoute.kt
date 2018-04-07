package rolling.pandas.server.domain

import javax.persistence.*

@Entity
data class SavedRoute(
        @Id
        @GeneratedValue
        var id: Long
) {
    @OneToMany
    var routePoints: List<Place> = listOf()
    @ElementCollection
    var routePaths: List<String> = listOf()
    var name: String = ""
}