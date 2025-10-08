///**
// * Created by chrystian.lozada.local (7/10/25)
// */
package com.example.simpsonsinfo.view.auth.register
//
//import androidx.lifecycle.ViewModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.update
//import javax.inject.Inject
//
//@HiltViewModel
//class RegisterViewModel @Inject constructor(): ViewModel() {
//
//    private val _uiState = MutableStateFlow(RegisterUiState())
//    val uiState: StateFlow<RegisterUiState> = _uiState
//
//    fun onPhoneChanged(number: String) {
//        _uiState.update { state ->
//            state.copy(phone = number)
//        }
//        if (number.isNotEmpty()) {
//            onCheckNumber()
//        } else {
//            _uiState.update { state ->
//                state.copy(isValidPhone = false)
//            }
//        }
//    }
//
//    fun onSecureCodeChanged(secureCode: String) {
//        _uiState.update { state ->
////            val secCode = getFormatSecureCode(secureCode)
//            // TODO Convertir a extension String
//            state.copy(secureCode = setFormatSecureCode(secureCode))
//        }
//        if (secureCode.isNotEmpty()) {
//            onCheckSecureCode()
//        } else {
//            _uiState.update { state ->
//                state.copy(isValidSecureCode = false)
//            }
//        }
//    }
//
//    private fun onCheckNumber() {
//        val isValid =
//            _uiState.value.phone.length == 9 && _uiState.value.phone.all { it.isDigit() } && (_uiState.value.phone.first() == '6' || _uiState.value.phone.first() == '7' || _uiState.value.phone.first() == '9')
//        _uiState.update { state ->
//            state.copy(isValidPhone = isValid)
//        }
//    }
//
//    private fun onCheckSecureCode() {
//        val isValid =
//            _uiState.value.secureCode.length == 7 && (_uiState.value.secureCode.first() == 'M' || _uiState.value.secureCode.first() == 'I')
//        _uiState.update { state ->
//            state.copy(isValidSecureCode = isValid)
//        }
//    }
//
//    private fun setFormatSecureCode(secureCode: String): String {
//        return if (secureCode.length > 3) {
//            if (secureCode.contains("-")) {
//                if(secureCode.length > 6) {
//                    secureCode.substring(0, 7).uppercase()
//                }else{
//                    secureCode.uppercase()
//                }
//            } else {
//                secureCode.substring(0, 3) + "-" + secureCode.substring(3).uppercase()
//            }
//        } else {
//            secureCode.uppercase()
//        }
//    }
//
//}
//
//data class RegisterUiState(
//    val phone: String = "",
//    val secureCode: String = "",
//    val isValidPhone: Boolean = false,
//    val isValidSecureCode: Boolean = false,
//)