package com.panini.tickets

import androidx.compose.runtime.Composable
import com.panini.tickets.navigation.AppNavHost
import com.panini.tickets.ui.theme.AppTheme

@Composable
fun PaniniSupportApp() {
    AppTheme {
        AppNavHost()
    }
}
