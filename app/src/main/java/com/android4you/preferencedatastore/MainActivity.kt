package com.android4you.preferencedatastore

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android4you.preferencedatastore.MainActivity.PreferencesKeys.REMEMBER
import com.android4you.preferencedatastore.MainActivity.PreferencesKeys.USERNAME
import com.android4you.preferencedatastore.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")




class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val REMEMBER = booleanPreferencesKey("agree")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.button.setOnClickListener {
          CoroutineScope(IO).launch {
                saveData(binding.editTextTextPersonName.text.toString(),binding.checkBox.isChecked )

            }


            Intent(this, SecondActivity::class.java).apply {
                startActivity(this)
            }

        }

    }


    suspend fun saveData(username: String, agree: Boolean) {
        this.dataStore.edit { settings ->
            settings[USERNAME] = username
            settings[REMEMBER] = agree

        }
    }
}