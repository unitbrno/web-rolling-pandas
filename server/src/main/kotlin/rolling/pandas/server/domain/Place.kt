package rolling.pandas.server.domain

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
data class Place(
        @Id
        @GeneratedValue
        var id: Long
) {
    lateinit var name: String
    lateinit var description: String
    @JsonProperty("icon_url")
    lateinit var iconUrl: String
    lateinit var type: String
    @Embedded
    lateinit var location: Location
    @ElementCollection
    lateinit var tags: List<String>

    override fun toString(): String {
        return "Place(id=$id, name='$name', description='$description', iconUrl='$iconUrl', type='$type', location=$location, tags=$tags)"
    }


}