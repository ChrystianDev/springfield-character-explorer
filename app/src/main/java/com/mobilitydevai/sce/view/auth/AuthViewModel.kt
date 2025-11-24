/**
 * Created by chrystian.lozada.local (24/11/25)
 */
package com.mobilitydevai.sce.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.mobilitydevai.sce.analytics.AnalyticsManager
import com.mobilitydevai.sce.analytics.CrashlyticsManager
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
    private val analyticsManager: AnalyticsManager,
    private val crashlyticsManager: CrashlyticsManager,
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
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                    // Set user info in analytics and crashlytics
                    analyticsManager.setUserId(user.uid)
                    crashlyticsManager.setUserSession(
                        userId = user.uid,
                        userName = user.name,
                        userEmail = user.email
                    )
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch(dispatcher) {
            _authState.value = AuthState.Loading
            crashlyticsManager.recordBreadcrumb("AuthViewModel", "signInWithGoogle_started")

            val result = userRepository.signInWithGoogle(account)
            result.onSuccess { user ->
                _authState.value = AuthState.Authenticated(user)
                _currentUser.value = user
                _signInRequired.value = false
                _errorState.value = null

                // Log successful login
                analyticsManager.logLogin("google")
                crashlyticsManager.log("User signed in successfully with Google: ${user.uid}")
            }
            result.onFailure { exception ->
                _authState.value = AuthState.Error(exception.message ?: "Authentication failed")
                _errorState.value = exception.message

                // Log authentication error
                analyticsManager.logError("authentication", exception.message ?: "Unknown error")
                crashlyticsManager.logAuthenticationError(
                    exception.message ?: "Authentication failed",
                    exception
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch(dispatcher) {
            crashlyticsManager.recordBreadcrumb("AuthViewModel", "signOut_started")

            val result = userRepository.signOut()
            result.onSuccess {
                _authState.value = AuthState.Unauthenticated
                _currentUser.value = null

                // Log sign out
                analyticsManager.logCustomEvent("sign_out")
                crashlyticsManager.log("User signed out")
            }
            result.onFailure { exception ->
                _errorState.value = exception.message

                // Log sign out error
                analyticsManager.logError("sign_out", exception.message ?: "Unknown error")
                crashlyticsManager.recordException(exception)
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
