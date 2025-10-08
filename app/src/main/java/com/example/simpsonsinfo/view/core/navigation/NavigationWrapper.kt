package com.example.simpsonsinfo.view.core.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simpsonsinfo.view.simpsons.SimpsonsScreen
import com.example.simpsonsinfo.view.simpsons.SimpsonsViewModel

@Composable
fun NavigationWrapper(context: Context, simpsonsViewModel: SimpsonsViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Simpsons) {
//    NavHost(navController = navController, startDestination = Login) {

        composable<Simpsons> {
            SimpsonsScreen(simpsonsViewModel = simpsonsViewModel)
        }

//        composable<Login> {
//            LoginScreen(
//                onCreateAccount = {
//                    navController.navigate(Register)
//                }
//            )
//        }
//
//        composable<Register> {
//            RegisterScreen(context = context)
//        }

    }


}