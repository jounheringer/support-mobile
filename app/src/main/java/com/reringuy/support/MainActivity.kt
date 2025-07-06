package com.reringuy.support

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reringuy.support.navigation.Destination
import com.reringuy.support.navigation.NavigationMap
import com.reringuy.support.presentation.screens.home.HomeScreen
import com.reringuy.support.presentation.screens.newTask.NewTaskScreen
import com.reringuy.support.presentation.screens.taskDetails.TaskDetailsScreen
import com.reringuy.support.presentation.screens.taskList.TaskListScreen
import com.reringuy.support.presentation.theme.SupportTheme

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
    var pageTitle by remember { mutableStateOf(Destination.HOME.route.route) }
    val snackBarHost = remember { SnackbarHostState() }
    val navigationController = rememberNavController()
    Scaffold(
        topBar = { SupportTopAppBar(pageTitle) },
        bottomBar = { SupportBottomAppBar {
            pageTitle = it.route.route
            navigationController.navigate(it.route)
        } },
        snackbarHost = { SnackbarHost(hostState = snackBarHost) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                pageTitle = NavigationMap.NewTask.route
                navigationController.navigate(NavigationMap.NewTask)
            }) {
                Icon(Icons.Rounded.Add, "Adicionar chamado")
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navigationController,
                startDestination = Destination.HOME.route
            ) {
                composable<NavigationMap.Home> { HomeScreen() }
                composable<NavigationMap.TaskList> { TaskListScreen() }
                composable<NavigationMap.NewTask> { NewTaskScreen() }
                composable<NavigationMap.TaskDetails> { TaskDetailsScreen() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportTopAppBar(pageTitle: String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.onBackground.copy(0.1f),
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = pageTitle) }
    )
}

@Composable
fun SupportBottomAppBar(navigateTo: (Destination) -> Unit) {
    val startDestination = Destination.HOME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    NavigationBar(contentColor = MaterialTheme.colorScheme.primary) {
        Destination.entries.forEachIndexed { key, value ->
            NavigationBarItem(
                selected = selectedDestination == key,
                onClick = {
                    navigateTo(value)
                    selectedDestination = key
                },
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