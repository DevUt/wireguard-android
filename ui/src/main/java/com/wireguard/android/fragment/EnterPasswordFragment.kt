/*
 * Copyright © 2017-2022 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wireguard.android.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.wireguard.android.R
import com.wireguard.android.activity.MainActivity
import com.wireguard.android.activity.passData
import com.wireguard.android.preference.PreferencesPreferenceDataStore
import java.math.BigInteger
import java.security.MessageDigest

class EnterPasswordFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_password, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val passStatus : TextView = view.findViewById(R.id.current_pass_status)
        val passButton : Button = view.findViewById(R.id.enter_password_button)
        val password : EditText = view.findViewById(R.id.password_input)
        val passwordManager = PreferencesPreferenceDataStore(lifecycleScope,requireContext().passData)
        val passStored = passwordManager.getString("password","failure")
        var count = 0
        var passRead : String = "holder"
        passButton.setOnClickListener {
            val failureToast = Toast.makeText(requireContext(),"Please try again",Toast.LENGTH_SHORT)
            if(count == 0){
                passRead = password.text.toString()
                count += 1
                passStatus.text = "Please Enter Your Password Again"
            }else{
                if(password.text.toString() == passRead){
                    if(getSHA512(passRead) == passStored){
                        startActivity(Intent(activity,MainActivity::class.java))
                    }else{
                        failureToast.show()
                        passStatus.text = getString(R.string.please_enter_your_password)
                        count = 0
                    }
                }else{
                    failureToast.show()
                    passStatus.text = getString(R.string.please_enter_your_password)
                    count = 0
                }
            }
        }
    }

    private fun getSHA512(input: String): String {
        val md: MessageDigest = MessageDigest.getInstance("SHA-512")
        val messageDigest = md.digest(input.toByteArray())

        // Convert byte array into signum representation
        val no = BigInteger(1, messageDigest)

        // Convert message digest into hex value
        var hashtext: String = no.toString(16)

        // Add preceding 0s to make it 128 chars long
        while (hashtext.length < 128) {
            hashtext = "0$hashtext"
        }
        // return the HashText
        return hashtext
    }
}