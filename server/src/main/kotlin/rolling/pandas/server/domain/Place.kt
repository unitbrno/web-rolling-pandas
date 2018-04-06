package rolling.pandas.server.domain

import javax.persistence.*

@Entity
data class Place(
        @Id
        @GeneratedValue
        var id: Long
) {
    lateinit var name: String
    lateinit var description: String
    lateinit var icon: String
    @Embedded
    lateinit var location: Location
    @ElementCollection
    lateinit var tags: List<String>
}