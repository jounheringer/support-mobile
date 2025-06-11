package com.reringuy.support.presentation.login

import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reringuy.support.auth.biometric.BiometricPromptManager
import com.reringuy.support.auth.biometric.BiometricResult
import com.reringuy.support.helper.OperationHandler
import com.reringuy.support.helper.rememberFlowWithLifecycle
import com.reringuy.support.models.data.EmailPassword
import com.reringuy.support.presentation.components.Loading

@Composable
fun LoginScreenWrapper(
    biometricPromptManager: BiometricPromptManager,
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit,
) {
    val promptResult by biometricPromptManager.promptResult.collectAsStateWithLifecycle(null)
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.effect)
    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            println("Activity result: $it")
        }
    )

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    LaunchedEffect(promptResult) {
        when (promptResult) {
            is BiometricResult.AuthenticationError -> {
                Toast.makeText(context, "Erro de autenticação.", Toast.LENGTH_SHORT).show()
            }
            BiometricResult.AuthenticationSuccess -> {
                Toast.makeText(context, "Autenticado com sucesso.", Toast.LENGTH_SHORT).show()
                onLogin()
            }
            BiometricResult.AuthenticationNotSet -> {
                if (Build.VERSION.SDK_INT >= 30) {
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                        )
                    }
                    enrollLauncher.launch(enrollIntent)
                }
            }
            null -> {}
            else -> {
                Toast.makeText(context, "Erro de autenticação.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(effect) {
        effect.collect {
            when (it) {
                is LoginReducer.LoginEffects.LoginError -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                }
            }
        }
    }

    when (state.currentUser) {
        is OperationHandler.Error -> {
            LoginScreen(viewModel::onLogin)
        }
        is OperationHandler.Success<*> -> {
            Log.d("prompt", "miau")
            biometricPromptManager.showBiometricPrompt(
                title = "Valide identidade",
                description = "Valide sua identidade para continuar"
            )
        }
        else -> Loading()
    }

}

@Composable
fun LoginScreen(
    onLogin: (EmailPassword) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleLogin()
            FormLogin(onLogin)
        }
    }
}

@Composable
fun TitleLogin() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Bem Vindo",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Sistema de suporte reringuy",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun FormLogin(onClick: (EmailPassword) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.onBackground.copy(0.2f),
                RoundedCornerShape(25.dp)
            )
            .padding(18.dp, 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") },
                    placeholder = { Text(text = "reringuy@test.com") }
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Senha") },
                    placeholder = { Text(text = "123456") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                                "Mostrar senha"
                            )
                        }
                    }
                )
            }
            Button(
                onClick = { onClick(EmailPassword(email, password)) },
                enabled = email.isNotBlank() && password.isNotBlank(),
            ) {
                Text(text = "Entrar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen {  }
}