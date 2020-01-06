package models

/**
 * Represents a venue
 * @property id The id of the venue
 * @property name The name of the venue
 * @property location A general location of the venue
 * @property address The street address of the venue
 * @property state If the venue is in the USA, represents the state.
 * @property latitude Latitude of the venue
 * @property longitude Longitude of the venue
 */
data class Venue(val id: Int,
                 val name: String,
                 val location: String,
                 val address: String,
                 val state: String,
                 val latitude: Double,
                 val longitude: Double)