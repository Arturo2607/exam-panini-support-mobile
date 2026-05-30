package com.panini.tickets.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.panini.tickets.core.FeatureFlags
import com.panini.tickets.core.UserMessages
import com.panini.tickets.ui.components.AppScaffold
import com.panini.tickets.ui.create.CreateTicketScreen
import com.panini.tickets.ui.detail.TicketDetailScreen
import com.panini.tickets.ui.login.LoginScreen
import com.panini.tickets.ui.tickets.TicketsScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    var currentUserId by rememberSaveable { mutableIntStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN
    ) {
        composable(AppDestinations.LOGIN) {
            AppScaffold(
                title = UserMessages.Login.TITLE
            ) { padding ->
                LoginScreen(
                    onLoginSuccess = { userId ->
                        currentUserId = userId
                        navController.navigate(AppDestinations.TICKETS) {
                            popUpTo(AppDestinations.LOGIN) { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(AppDestinations.TICKETS) {
            val snackbarHostState = remember { SnackbarHostState() }
            AppScaffold(
                title = UserMessages.Tickets.TITLE,
                snackbarHostState = snackbarHostState,
                floatingActionButton = {
                    // FEATURE FLAG: hides the FAB (and the entire create-ticket flow)
                    // when ENABLE_TICKET_CREATION is false.
                    if (FeatureFlags.ENABLE_TICKET_CREATION) {
                        FloatingActionButton(
                            onClick = { navController.navigate(AppDestinations.TICKET_CREATE) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = UserMessages.Accessibility.ADD
                            )
                        }
                    }
                }
            ) { padding ->
                TicketsScreen(
                    onTicketClick = { ticket ->
                        navController.navigate(AppDestinations.ticketDetail(ticket.ticketId))
                    },
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(AppDestinations.TICKET_CREATE) {
            val snackbarHostState = remember { SnackbarHostState() }
            AppScaffold(
                title = UserMessages.TicketCreate.TITLE,
                showBack = true,
                onBackClick = { navController.popBackStack() },
                snackbarHostState = snackbarHostState
            ) { padding ->
                CreateTicketScreen(
                    createdBy = currentUserId,
                    snackbarHostState = snackbarHostState,
                    onSaved = { navController.popBackStack() },
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(
            route = AppDestinations.TICKET_DETAIL,
            arguments = listOf(navArgument(AppDestinations.ARG_TICKET_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getInt(AppDestinations.ARG_TICKET_ID)
                ?: return@composable
            val snackbarHostState = remember { SnackbarHostState() }
            AppScaffold(
                title = UserMessages.TicketDetail.TITLE,
                showBack = true,
                onBackClick = { navController.popBackStack() },
                snackbarHostState = snackbarHostState
            ) { padding ->
                TicketDetailScreen(
                    ticketId = ticketId,
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}
