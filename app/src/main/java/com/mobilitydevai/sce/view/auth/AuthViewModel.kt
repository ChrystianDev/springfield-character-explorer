/**
 * Created by chrystian.lozada.local (24/11/25)
 */
package com.mobilitydevai.sce.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.mobilitydevai.sce.domain.entity.UserEntity
import com.mobilitydevai.sce.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    private val _signInRequired = MutableStateFlow(false)
    val signInRequired = _signInRequired.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch(dispatcher) {
            userRepository.getCurrentUser().collect { user ->
                _currentUser.value = user
                _authState.value = if (user != null) {
                    AuthState.Authenticated(user)
                } else {
                    AuthState.Unauthenticated
                }
            }
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch(dispatcher) {
            _authState.value = AuthState.Loading
            val result = userRepository.signInWithGoogle(account)
            result.onSuccess { user ->
                _authState.value = AuthState.Authenticated(user)
                _currentUser.value = user
                _signInRequired.value = false
                _errorState.value = null
            }
            result.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Authentication failed")
                _errorState.value = exception.message
            }
        }
    }

    fun signOut() {
        viewModelScope.launch(dispatcher) {
            val result = userRepository.signOut()
            result.onSuccess {
                _authState.value = AuthState.Unauthenticated
                _currentUser.value = null
            }
            result.onFailure { exception ->
                _errorState.value = exception.message
            }
        }
    }

    fun requestSignIn() {
        _signInRequired.value = true
    }

    fun clearSignInRequired() {
        _signInRequired.value = false
    }

    fun clearError() {
        _errorState.value = null
    }

    fun isAuthenticated(): Boolean {
        return _authState.value is AuthState.Authenticated
    }
}

sealed class AuthState {
    data object Loading : AuthState()
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: UserEntity) : AuthState()
    data class Error(val message: String) : AuthState()
}
