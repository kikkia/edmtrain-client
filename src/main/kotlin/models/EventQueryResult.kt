package models

/**
 * Represents a response for an event query from the API
 * @property success True if the api call was successful
 */
data class EventQueryResult(val success: Boolean, val events: List<Event>)