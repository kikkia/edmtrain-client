import com.sun.istack.internal.NotNull
import exceptions.APIException
import models.Event
import models.Location
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.json.JSONObject
import utils.ParseUtils

import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.HashMap

class EdmtrainClient(builder: Builder) {

    private val token: String?

    init {
        this.token = builder.token
    }

    class Builder {
        internal var token: String? = null

        fun setToken(token: String) {
            this.token = token
        }

        fun build(): EdmtrainClient {
            return EdmtrainClient(this)
        }
    }

    inner class EventQuery {
        private val args: MutableMap<String, String>
        private val formatter: DateTimeFormatter

        init {
            args = HashMap()
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        }

        fun withEventName(@NotNull name: String): EventQuery {
            this.args["eventName"] = name
            return this
        }

        fun withArtistIds(@NotNull artistIds: List<Int>): EventQuery {
            val idStrings = artistIds.map { it.toString() }
            this.args["artistIds"] = idStrings.joinToString(",")
            return this
        }

        fun withVenueIds(@NotNull venueIds: List<Int>): EventQuery {
            val idStrings = venueIds.map { it.toString() }
            this.args["venueIds"] = idStrings.joinToString(",")
            return this
        }

        fun withLocationIds(@NotNull locationIds: List<Int>): EventQuery {
            val idStrings = locationIds.map { it.toString() }
            this.args["locationIds"] = idStrings.joinToString(",")
            return this
        }

        fun withStartDate(@NotNull startDate: LocalDate): EventQuery {
            this.args["startDate"] = startDate.format(formatter)
            return this
        }

        fun withEndDate(@NotNull endDate: LocalDate): EventQuery {
            this.args["endDate"] = endDate.format(formatter)
            return this
        }

        fun withCreatedStartDate(@NotNull createdStartDate: LocalDate): EventQuery {
            this.args["createdStartDate"] = createdStartDate.format(formatter)
            return this
        }

        fun withCreatedEndDate(@NotNull createdEndDate: LocalDate): EventQuery {
            this.args["createdEndDate"] = createdEndDate.format(formatter)
            return this
        }

        fun isFestival(isFestival: Boolean): EventQuery {
            this.args["festivalInd"] = isFestival.toString()
            return this
        }

        fun includeElectronicGenre(includeElectronicGenre: Boolean): EventQuery {
            this.args["includeElectronicGenreInd"] = includeElectronicGenre.toString()
            return this
        }

        fun includeOtherGenre(includeOtherGenre: Boolean): EventQuery {
            this.args["includeOtherGenreInd"] = includeOtherGenre.toString()
            return this
        }

        @Throws(APIException::class)
        fun get(): List<Event> {
            val url = buildUrl(this.args)
            try {
                HttpClients.createDefault().use { client ->
                    val get = HttpGet(url)
                    val response = client.execute(get)
                    val json = IOUtils.toString(response.entity.content)
                    val respJson = JSONObject(json)

                    return ParseUtils.parseEventsFromResponse(respJson)
                }
            } catch (e: IOException) {
                throw APIException("Something went wrong getting a connection to the api: " + e.message)
            }

        }
    }

    inner class LocationQuery {
        private val args: MutableMap<String, String>

        init {
            args = HashMap()
        }

        fun withState(state: String): LocationQuery {
            args["state"] = state
            return this
        }

        fun withCity(city: String): LocationQuery {
            args["city"] = city
            return this
        }

        @Throws(APIException::class)
        fun get(): List<Location> {
            val url = buildUrl(this.args)

            try {
                HttpClients.createDefault().use { client ->
                    val get = HttpGet(url)
                    val response = client.execute(get)
                    val json = IOUtils.toString(response.entity.content)
                    val respJson = JSONObject(json)

                    return ParseUtils.parseLocationsFromResponse(respJson)
                }
            } catch (e: IOException) {
                throw APIException("Something went wrong getting a connection to the api: " + e.message)
            }

        }
    }

    private fun buildUrl(args: Map<String, String>): String {
        val baseUrl = "https://edmtrain.com/api/"
        val url = StringBuilder(baseUrl + "events?")
        for ((key, value) in args) {
            url.append(key).append("=").append(value).append("&")
        }
        return url.append("client=").append(token).toString()
    }
}
