package com.example.simpsonsinfo.view.auth.register
//
//import android.content.Context
//import android.widget.Toast
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.setValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.example.simpsonsinfo.view.core.components.CustomText
//import androidx.compose.ui.unit.sp
//import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.simpsonsinfo.R
//import com.example.simpsonsinfo.ui.theme.GrayDev100
//import com.example.simpsonsinfo.ui.theme.GrayDev90
//import com.example.simpsonsinfo.view.core.components.CustomRegisterText
//import com.example.simpsonsinfo.view.core.components.SpaceH10
//import com.example.simpsonsinfo.view.core.components.SpaceV10
//import com.example.simpsonsinfo.view.core.components.SpaceVy
//
//@OptIn(ExperimentalMaterial3Api::class)
////@Preview(showBackground = true, showSystemUi = true)
//
//@Composable
//fun RegisterScreen(
//    registerViewModel: RegisterViewModel = hiltViewModel(),
//    context: Context
//) {
//
//    val uiState by registerViewModel.uiState.collectAsStateWithLifecycle()
//    var isLastPage by remember { mutableStateOf(false) }
//
//
//    Scaffold(topBar = {
//        TopAppBar(
//            title = {
//                Row(
//                    horizontalArrangement = Arrangement.Center,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(stringResource(if (isLastPage) R.string.register_input_secure_code else R.string.register_input_phone))
//                    SpaceH10()
//                    Icon(
//                        modifier = Modifier.clickable { },
//                        painter = painterResource(
//                            if (isLastPage) R.drawable.security_icon else R.drawable.phone_icon
//                        ),
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.onBackground
//                    )
//                }
//            },
//            navigationIcon = {
//                Icon(
//                    modifier = Modifier.clickable { },
//                    painter = painterResource(
//                        R.drawable.arrow_back
//                    ),
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onBackground
//                )
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = MaterialTheme.colorScheme.background
//            ),
//        )
//    }) { innerPadding ->
//
//        Column(
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.Top,
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//                .padding(innerPadding)
//                .padding(horizontal = 20.dp),
//        ) {
//            if (isLastPage) {
//                CustomRegisterText(
//                    text = stringResource(R.string.register_title_secure_code),
//                    color = MaterialTheme.colorScheme.onSecondary,
//                    fontSize = 25.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                SpaceV10()
//                CustomRegisterText(
//                    text = stringResource(R.string.register_subtitle_secure_code),
//                    color = MaterialTheme.colorScheme.onSecondary
//                )
//                SpaceVy(15.dp)
//                OutlinedTextField(
//                    value = uiState.secureCode,
//                    onValueChange = { secureCode ->
//                        registerViewModel.onSecureCodeChanged(secureCode)
//                    },
//                    label = {
//                        CustomText(text = stringResource(R.string.register_input_secure_code))
//                    },
//                    colors = OutlinedTextFieldDefaults.colors(
////                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
////                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
//                        focusedBorderColor = GrayDev90, unfocusedBorderColor = GrayDev100
//                    ),
//                    singleLine = true,
////                    lineLimits = TextFieldLineLimits.SingleLine,
//                    shape = MaterialTheme.shapes.large,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(60.dp)
//
//                )
//            } else {
//                CustomRegisterText(
//                    text = stringResource(R.string.register_title),
//                    color = MaterialTheme.colorScheme.onSecondary,
//                    fontSize = 25.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                SpaceV10()
//                CustomRegisterText(
//                    text = stringResource(R.string.register_subtitle),
//                    color = MaterialTheme.colorScheme.onSecondary
//                )
//                SpaceVy(15.dp)
//                OutlinedTextField(
//                    value = uiState.phone,
//                    onValueChange = {
//                        registerViewModel.onPhoneChanged(it)
//                    },
//                    label = {
//                        CustomText(text = stringResource(R.string.register_input_phone))
//                    },
//                    colors = OutlinedTextFieldDefaults.colors(
////                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
////                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
//                        focusedBorderColor = GrayDev90, unfocusedBorderColor = GrayDev100
//                    ),
//                    singleLine = true,
//                    shape = MaterialTheme.shapes.large,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(60.dp)
//
//                )
//            }
//            SpaceV10()
//            CustomRegisterText(text = stringResource(R.string.register_info_external_notifications))
//            SpaceV10()
//            Button(
//                enabled = if (isLastPage) true else uiState.isValidPhone,
//                onClick = {
//                    registerViewModel.onPhoneChanged("")
//                    registerViewModel.onSecureCodeChanged("")
//                    isLastPage = !isLastPage
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(45.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//            ) {
//                CustomText(
//                    text = stringResource(if (isLastPage) R.string.register_button_back else R.string.register_button_next),
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//            SpaceV10()
//            if (isLastPage)
//                OutlinedButton(
//                    enabled = uiState.isValidSecureCode,
//                    onClick = {
//                        Toast.makeText(
//                            context,
//                            context.getString(R.string.register_toast_create_account),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(45.dp),
//                    border = BorderStroke(
//                        width = 1.dp,
//                        color = if (uiState.isValidSecureCode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onTertiary,
//                    )
//                ) {
//                    CustomText(
//                        text = stringResource(R.string.login_button_register_new_account),
//                        color = if (uiState.isValidSecureCode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onTertiary,
//                    )
//                }
//        }
//    }
//}