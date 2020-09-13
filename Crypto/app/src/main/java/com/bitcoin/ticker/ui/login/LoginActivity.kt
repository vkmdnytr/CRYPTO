package com.bitcoin.ticker.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.bitcoin.ticker.R
import com.bitcoin.ticker.util.listener.OnClickOkButtonListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()

        signInButton.setOnClickListener {
            val email=emailEditText.text.toString()
            val password=passwordEditText.text.toString()
            loginUser(email, password)
        }
        registerButton.setOnClickListener {
            val email=emailEditText.text.toString()
            val password=passwordEditText.text.toString()
            registerUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    finish()
                } else {
                    showGeneralErrorDialog(text =  task.exception?.localizedMessage)
                }
            })
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    finish()
                } else {
                    showGeneralErrorDialog(text =  task.exception?.localizedMessage)
                }

            }
    }

    private fun showGeneralErrorDialog(text:String?=null, isCancelable:Boolean=false, onOkClickListener: OnClickOkButtonListener?=null)= MaterialDialog(this)
        .show {
            title(text = getString(R.string.dialog_title_mistake))
            message(text = text?:getString(R.string.dialog_message_mistake))
            cancelable(isCancelable)
            positiveButton(R.string.ok)
            positiveButton {
                if(onOkClickListener==null){
                    dismiss()
                }else  {
                    onOkClickListener.onClickOkButton(this)
                }

            }
        }


}