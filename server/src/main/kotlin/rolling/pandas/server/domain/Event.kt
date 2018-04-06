package rolling.pandas.server.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Event(
        @Id
        @GeneratedValue
        val id: Long
) {
    lateinit var name: String
    @Embedded
    lateinit var location: Location
    lateinit var icon: String
    @JsonProperty("start")
    lateinit var startTime: Date
    @JsonProperty("end")
    lateinit var endTime: Date

    override fun toString(): String {
        return "Event(id=$id, name='$name', location=$location, icon='$icon', startTime=$startTime, endTime=$endTime)"
    }


}