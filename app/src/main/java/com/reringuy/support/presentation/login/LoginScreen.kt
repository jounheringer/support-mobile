package com.reringuy.support.presentation.login

import android.widget.Toast
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
import com.reringuy.support.helper.OperationHandler
import com.reringuy.support.helper.rememberFlowWithLifecycle
import com.reringuy.support.models.data.EmailPassword
import com.reringuy.support.models.entities.User
import com.reringuy.support.presentation.components.Loading
import com.reringuy.support.presentation.login.LoginReducer.LoginState

@Composable
fun LoginScreenWrapper(
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.effect)

    LaunchedEffect(effect) {
        effect.collect {
            when (it) {
                LoginReducer.LoginEffects.OnAuthenticated -> onLogin
                is LoginReducer.LoginEffects.OnError -> {
                    Toast.makeText(context, "Dados invalidos.", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    when (state.currentUser) {
//        Usuario nao encotrado entao fazer login
        is OperationHandler.Error -> {
            LoginScreen(state, viewModel::onLogin)
        }
//        Usuario encontrado entao chamar biometrics para validar login
        is OperationHandler.Success<*> -> {
        }
        else -> Loading()
    }

}

@Composable
fun LoginScreen(
    state: LoginState,
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
    var authData by remember { mutableStateOf(EmailPassword("", "")) }
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
                    value = authData.email,
                    onValueChange = { authData.email = it },
                    label = { Text(text = "Email") },
                    placeholder = { Text(text = "reringuy@test.com") }
                )
                OutlinedTextField(
                    value = authData.password,
                    onValueChange = { authData.password = it },
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
                onClick = { onClick(authData) },
                enabled = authData.isValid()
            ) {
                Text(text = "Entrar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val state = LoginState(
        loading = false,
        email = "joao.joao@gmail",
        password = "123",
        currentUser = OperationHandler.Success(User(
            id = 1,
            email = "joao",
            role = "ADMIN"
        ))
    )
    LoginScreen(state) {  }
}