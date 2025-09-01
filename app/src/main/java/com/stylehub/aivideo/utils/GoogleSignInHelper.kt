package com.stylehub.aivideo.utils

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.stylehub.aivideo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleSignInHelper(
    private val activity: ComponentActivity
) {
    var onLoginResult: ((GoogleIdTokenCredential?) -> Unit)? = null

    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(activity)
    }

    fun signIn() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val clientId = activity.getString(R.string.google_web_client_id)
                val googleSignInOption = GetSignInWithGoogleOption
                    .Builder(clientId)
                    .build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleSignInOption)
                    .build()
                val result = credentialManager.getCredential(
                    request = request,
                    context = activity
                )
                handleSignInResult(result)
            } catch (e: GetCredentialException) {
                Log.e("GoogleSignInHelper", "Google sign in failed", e)
                onLoginResult?.invoke(null)
            }
        }
    }

    private fun handleSignInResult(result: GetCredentialResponse?) {
        if (result == null) {
            onLoginResult?.invoke(null)
            return
        }
        val credential = result.credential
        if (credential is androidx.credentials.CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                // 获取idToken、displayName、photoUrl等信息
                val idToken = googleIdTokenCredential.idToken
                val displayName = googleIdTokenCredential.displayName ?: ""
                val photoUrl = googleIdTokenCredential.profilePictureUri?.toString() ?: ""
                onLoginResult?.invoke(googleIdTokenCredential)
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("GoogleSignInHelper", "Invalid google id token response", e)
                onLoginResult?.invoke(null)
            }
        } else {
            onLoginResult?.invoke(null)
        }
    }

    fun signOut() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                credentialManager.clearCredentialState(
                    request = ClearCredentialStateRequest()
                )
            } catch (e: Exception) {
                Log.e("GoogleSignInHelper", "Sign out failed", e)
            }
        }
    }
} 