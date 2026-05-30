package com.panini.tickets.core
object FeatureFlags {

    /**
     * Controls visibility of the FAB that navigates to the Create Ticket screen.
     * Set to false to hide ticket creation across the entire app in one line.
     */
    const val ENABLE_TICKET_CREATION: Boolean = true

    /**
     * Controls visibility of the "Change Priority" button in the Ticket Detail screen.
     * Set to false during periods when priority changes should be restricted
     * to admin users or backend-controlled workflows.
     */
    const val ENABLE_PRIORITY_CHANGE: Boolean = true
}
