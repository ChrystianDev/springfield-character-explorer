package com.example.simpsonsinfo.view.auth.login
//
//import android.util.Patterns
//import androidx.lifecycle.ViewModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.update
//import javax.inject.Inject
//
//@HiltViewModel
//class LoginViewModel @Inject constructor(val login: DoLogin): ViewModel() {
//    /**
//     * "MutableStateFlow" almacena y cambia el valor de la variable [_uiState]
//     */
//    private val _uiState = MutableStateFlow(LoginUiState())
//
//    /**
//     * "StateFlow" observa y lee el valor de la variable [_uiState]
//     */
//    val uiState: StateFlow<LoginUiState> = _uiState
//
//    // Modify username
//    fun onUsernameChanged(username: String) {
//        _uiState.update { state ->
//            state.copy(username = username)
////            state.copy(
////                username = username,
////                isLoginEnabled = isValidEmail(state.username) && isValidPassword(state.password)
////            )
//        }
//        validateFields()
//    }
//
//    // Modify password
//    fun onPasswordChanged(password: String) {
//        _uiState.update { state ->
//            state.copy(password = password)
////            state.copy(
////                password = password,
////                isLoginEnabled = isValidEmail(state.username) && isValidPassword(state.password)
////            )
//        }
//        validateFields()
//    }
//
//    //Check valid fields
//    private fun validateFields() {
//        val enabledLogin =
//            isValidEmail(_uiState.value.username) && isValidPassword(_uiState.value.password)
//        _uiState.update { state ->
//            state.copy(isLoginEnabled = enabledLogin)
//        }
//    }
//
//    private fun isValidEmail(username: String): Boolean =
//        Patterns.EMAIL_ADDRESS.matcher(username).matches()
//
//    private fun isValidPassword(password: String): Boolean = password.length >= 6
//
//}
//
//
//data class LoginUiState(
////    val username: String = "chrystian@gmail.com",
//    val username: String = "",
////    val password: String = "123456",
//    val password: String = "",
//    val isLoading: Boolean = false,
//    val isLoginEnabled: Boolean = false,
//)