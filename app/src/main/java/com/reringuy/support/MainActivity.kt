package com.reringuy.support

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.reringuy.support.navigation.Destination
import com.reringuy.support.ui.theme.SupportTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SupportTheme {
                SupportScaffold()
            }
        }
    }
}

@Composable
fun SupportScaffold() {
    val snackBarHost = remember { SnackbarHostState() }
    Scaffold(
        topBar = { SupportTopAppBar() },
        bottomBar = { SupportBottomAppBar() },
        snackbarHost = { SnackbarHost(hostState = snackBarHost) },
//        floatingActionButton = TODO(),
//        floatingActionButtonPosition = TODO(),
//        containerColor = TODO(),
//        contentColor = TODO(),
//        contentWindowInsets = TODO()
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) { }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportTopAppBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.onBackground.copy(0.1f),
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = "Bem Vindo") }
    )
}

@Composable
fun SupportBottomAppBar() {
//    val navController = rememberNavController()
    val startDestination = Destination.HOME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    NavigationBar(contentColor = MaterialTheme.colorScheme.primary) {
        Destination.entries.forEachIndexed { key, value ->
            NavigationBarItem(
                selected = selectedDestination == key,
                onClick = { selectedDestination = key },
                icon = {
                    Icon(imageVector = value.icon, contentDescription = value.contentDescription)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SupportTheme {
        SupportScaffold()
    }
}