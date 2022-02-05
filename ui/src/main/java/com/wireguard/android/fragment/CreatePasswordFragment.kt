/*
 * Copyright © 2017-2022 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wireguard.android.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.wireguard.android.R
import com.wireguard.android.activity.MainActivity
import com.wireguard.android.activity.passData
import com.wireguard.android.preference.PreferencesPreferenceDataStore
import java.math.BigInteger
import java.security.MessageDigest


class CreatePasswordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val passwordButton : Button = view.findViewById(R.id.create_pass_but)
        val passInp : EditText = view.findViewById(R.id.create_pass_input)
        passwordButton.setOnClickListener {
            if(passwordButton.text.toString().isBlank()){
                Toast.makeText(requireContext(),"Empty strings not allowed",Toast.LENGTH_LONG).show()
            }else{
                val passwordManager = PreferencesPreferenceDataStore(lifecycleScope,requireContext().passData)
                passwordManager.putString("password",getSHA512(passInp.text.toString()))
                startActivity(Intent(activity,MainActivity::class.java))
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