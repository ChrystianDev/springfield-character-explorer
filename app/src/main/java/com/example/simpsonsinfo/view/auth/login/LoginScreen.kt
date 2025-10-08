package com.example.simpsonsinfo.view.auth.login
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.example.simpsonsinfo.R
//import com.example.simpsonsinfo.ui.theme.GrayDev100
//import com.example.simpsonsinfo.ui.theme.GrayDev70
//import com.example.simpsonsinfo.ui.theme.GrayDev90
//import com.example.simpsonsinfo.view.core.components.CustomText
//import com.example.simpsonsinfo.view.core.components.SpaceV10
//import com.example.simpsonsinfo.view.core.components.SpaceV5
//import com.example.simpsonsinfo.view.core.components.SpaceVy
//
////@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun LoginScreen(
//    loginViewModel: LoginViewModel = hiltViewModel(),
//    onCreateAccount: () -> Unit
//) {
//
//    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//            .padding(horizontal = 20.dp, vertical = 55.dp)
//    ) {
//        CustomText(
//            text = stringResource(R.string.login_select_language),
//            style = MaterialTheme.typography.bodyMedium,
//
//            )
//        Image(
//            painterResource(R.drawable.instagram_logo_03),
//            contentDescription = "Image Instadev",
//            modifier = Modifier.size(60.dp)
//        )
//        Column {
//            OutlinedTextField(
//                value = uiState.username,
//                onValueChange = {
//                    loginViewModel.onUsernameChanged(it)
//                },
//                label = {
//                    CustomText(text = stringResource(R.string.login_input_username))
//                },
//                colors = OutlinedTextFieldDefaults.colors(
////                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
////                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
//                    focusedBorderColor = GrayDev90,
//                    unfocusedBorderColor = GrayDev100
//                ),
//                singleLine = true,
//                shape = MaterialTheme.shapes.large,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(60.dp)
//
//            )
//            SpaceV5()
//            OutlinedTextField(
//                value = uiState.password,
//                onValueChange = {
//                    loginViewModel.onPasswordChanged(it)
//                },
//                label = {
//                    CustomText(text = stringResource(R.string.login_input_password))
//                },
//                trailingIcon = {
//                    Icon(
//                        painter = painterResource(R.drawable.ic_show_password_foreground),
//                        contentDescription = "Show Password",
//                        tint = GrayDev90,
//                        modifier = Modifier.size(50.dp),
//
//                        )
//                },
//                colors = OutlinedTextFieldDefaults.colors(
////                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
////                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
//                    focusedBorderColor = GrayDev90,
//                    unfocusedBorderColor = GrayDev100
//                ),
//                singleLine = true,
//                shape = MaterialTheme.shapes.large,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(60.dp)
//
//            )
//            SpaceVy(12.dp)
//            Button(
//                enabled = uiState.isLoginEnabled,
//                onClick = {},
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(45.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//            ) {
//                CustomText(
//                    text = stringResource(R.string.login_button_access),
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//            TextButton(onClick = {}) {
//                CustomText(
//                    text = stringResource(R.string.login_button_forget_password),
//                    modifier = Modifier.fillMaxWidth(),
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            OutlinedButton(
//                onClick = {
//                    onCreateAccount()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(25.dp))
//                    .height(45.dp),
//                colors = ButtonDefaults.outlinedButtonColors(
//                    contentColor = MaterialTheme.colorScheme.primary,
//                ),
//                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
//            ) {
//                CustomText(
//                    text = stringResource(R.string.login_button_register_new_account),
//                    color = MaterialTheme.colorScheme.primary,
//                )
//
//            }
//            SpaceV10()
//            Icon(
//                painterResource(R.drawable.meta_logo_2),
//                contentDescription = "Image meta Instadev",
//                modifier = Modifier.height(30.dp),
//                tint = GrayDev70,
//            )
//        }
//    }
//
//}
