package com.panini.tickets.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.panini.tickets.core.UserMessages
import com.panini.tickets.ui.theme.AppBorder
import com.panini.tickets.ui.theme.AppOnSurface
import com.panini.tickets.ui.theme.AppPrimary
import com.panini.tickets.ui.theme.AppSecondaryText

private const val SIMULATED_USER_ID = 1

@Composable
fun LoginScreen(
    onLoginSuccess: (userId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor   = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedTextColor        = AppOnSurface,
        unfocusedTextColor      = AppOnSurface,
        focusedLabelColor       = AppPrimary,
        unfocusedLabelColor     = AppSecondaryText,
        focusedBorderColor      = AppPrimary,
        unfocusedBorderColor    = AppBorder,
        cursorColor             = AppPrimary
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = UserMessages.Login.INTRO,
            style = MaterialTheme.typography.bodyMedium,
            color = AppSecondaryText,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                error = null
            },
            label = { Text(text = UserMessages.Login.EMAIL_LABEL) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                error = null
            },
            label = { Text(text = UserMessages.Login.PASSWORD_LABEL) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = textFieldColors,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        error?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    error = UserMessages.Login.INVALID_CREDENTIALS
                } else {
                    onLoginSuccess(SIMULATED_USER_ID)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(text = UserMessages.Login.SUBMIT)
        }
    }
}