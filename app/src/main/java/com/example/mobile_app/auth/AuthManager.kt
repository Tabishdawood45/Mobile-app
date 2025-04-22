package com.example.mobile_app.auth
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun logout(navController: NavController) {

        FirebaseAuth.getInstance().signOut()


        navController.navigate("login_screen") {
            popUpTo("home_screen") { inclusive = true }
        }
    }


    fun deleteUserAccount(password: String, onResult: (Boolean, String?) -> Unit) {
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(user?.email ?: "", password)


        user?.reauthenticate(credential)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                user.delete().addOnCompleteListener { deleteTask ->
                    if (deleteTask.isSuccessful) {
                        onResult(true, null) // Account deleted successfully
                    } else {
                        onResult(false, "Failed to delete account") // Handle deletion failure
                    }
                }
            } else {
                onResult(false, "Authentication failed")
            }
        }
    }



    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
