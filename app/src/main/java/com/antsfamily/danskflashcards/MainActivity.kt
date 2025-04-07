package com.antsfamily.danskflashcards

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.antsfamily.danskflashcards.core.navigation.Navigator
import com.antsfamily.danskflashcards.ui.theme.DanskFlashCardsTheme
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appUpdateManager: AppUpdateManager

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            when (val resultCode = result.resultCode) {
                RESULT_OK -> {
                    val intent = this.intent
                    finish()
                    startActivity(intent)
                }

                RESULT_CANCELED -> {
                    Log.v(this::class.java.name, "User cancelled Update flow!")
                }

                else -> {
                    Log.v(this::class.java.name, "Update flow failed with resultCode:$resultCode")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DanskFlashCardsTheme {
                Navigator()
            }
        }
    }

    fun startAppUpdate(info: AppUpdateInfo) {
        val updateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()

        appUpdateManager.startUpdateFlowForResult(info, activityResultLauncher, updateOptions)
    }
}
