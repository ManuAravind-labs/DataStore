package com.android4you.preferencedatastore

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android4you.preferencedatastore.SecondActivity.PreferencesKeys.REMEMBER
import com.android4you.preferencedatastore.SecondActivity.PreferencesKeys.USERNAME
import com.android4you.preferencedatastore.databinding.ActivitySecondBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val REMEMBER = booleanPreferencesKey("agree")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Main).launch {
            readFromDataStore.collect {
                binding.textView.text = it.username
                binding.textView2.text = it.remember.toString()
            }
        }


    }

    val readFromDataStore: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.d("DataStoreRepository", exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val username = preference[USERNAME] ?: ""
            val remember = preference[REMEMBER] ?: false
            UserPreferences(username, remember)
        }

}