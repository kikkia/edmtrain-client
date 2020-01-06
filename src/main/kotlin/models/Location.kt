package models

/**
 * Represents a location for events to occur in
 * @property id The id of the location
 * @property city The city representation
 * @property state The state the location is in
 * @property stateCode State abbreviation
 * @property latitude Latitude of the location
 * @property longitude Longitude of the location
 * @property link Link to the edmtrain page for the location
 */
data class Location(val id: Int,
                    val city: String,
                    val state: String,
                    val stateCode: String,
                    val latitude: Double,
                    val longitude: Double,
                    val link: String)