package rolling.pandas.server.domain

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
data class Place(
        @Id
        @GeneratedValue
        var id: Long
) {
    var name: String = ""
    var description: String = ""
    @JsonProperty("icon_url")
    var iconUrl: String = ""
    var type: String = ""
    @Embedded
    var location: Location = Location(0.0, 0.0)
    @ElementCollection
    var tags: List<String> = emptyList()

    override fun toString(): String {
        return "Place(id=$id, name='$name', description='$description', iconUrl='$iconUrl', type='$type', location=$location, tags=$tags)"
    }


}