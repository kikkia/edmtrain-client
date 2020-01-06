package models

import java.util.*

/**
 * Represents an event on the EDMTrain website
 * @property id The event ID
 * @property link Link to the edmtrain page for the event
 * @property ticketLink Link to where to buy tickets
 * @property name Name for the event
 * @property ages Indicates what ages the event is for
 * @property isFestival Indicates if the event is a festival
 * @property isElectronic Indicates if the event is in the Electronic genre
 * @property isOtherGenre Indicates if the event is a genre other than electronic
 * @property date The date of the event
 * @property created Timestamp of when the event was created on the site
 * @property venue Representation of the venue the event is at
 * @property artists List of artists performing at the event.
 */
data class Event(val id: Int,
                 val link: String,
                 val ticketLink: String,
                 val name: String,
                 val ages: String,
                 val isFestival: Boolean,
                 val isElectronic: Boolean,
                 val isOtherGenre: Boolean,
                 val date: Date,
                 val created: Date,
                 val venue: Venue,
                 val artists: List<Artist>)