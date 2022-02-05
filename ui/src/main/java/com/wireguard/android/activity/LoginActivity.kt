/*
 * Copyright © 2017-2022 WireGuard LLC. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wireguard.android.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.wireguard.android.R
import com.wireguard.android.fragment.CreatePasswordFragment
import com.wireguard.android.fragment.EnterPasswordFragment
import com.wireguard.android.preference.PreferencesPreferenceDataStore

val Context.passData : DataStore<Preferences> by preferencesDataStore(name = "pass")
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val passwordManager = PreferencesPreferenceDataStore(lifecycleScope,passData)
        val passRead = passwordManager.getString("password","not_set")
        if(passRead == "not_set"){
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CreatePasswordFragment>(R.id.fragment_container_view)
            }
        }else{
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<EnterPasswordFragment>(R.id.fragment_container_view)
            }
        }
    }

}