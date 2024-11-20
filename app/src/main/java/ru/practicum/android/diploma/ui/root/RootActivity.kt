package ru.practicum.android.diploma.ui.root

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import java.util.Locale

class RootActivity : AppCompatActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppLocaleRu()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setAppLocaleRu() {
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration

        val locale = Locale("ru")

        config.setLocale(locale)

        resources.updateConfiguration(config, dm)
    }
}
