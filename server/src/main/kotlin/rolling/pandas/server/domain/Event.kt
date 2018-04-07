package rolling.pandas.server.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Event(
        @Id
        @GeneratedValue
        val id: Long
) {
    var eventUrl: String? = null
    @Type(type = "text")
    var description: String? = null
    @Column(unique = true)
    lateinit var name: String
    var address: String? = null
    @Embedded
    var location: Location = Location(0.0, 0.0)
    var icon: String? = null
    @JsonProperty("start")
    var startTime: LocalDateTime? = null
    @JsonProperty("end")
    var endTime: LocalDateTime? = null

    override fun toString(): String {
        return "Event(id=$id, eventUrl=$eventUrl, description=$description, name='$name', address=$address, location=$location, icon=$icon, startTime=$startTime, endTime=$endTime)"
    }


}