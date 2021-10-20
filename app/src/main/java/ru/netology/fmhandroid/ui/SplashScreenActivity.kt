package ru.netology.fmhandroid.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ru.netology.fmhandroid.R
import ru.netology.fmhandroid.databinding.ActivitySplashScreenBinding

@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {
    private val splashscreenImages = listOf(
        Triple(R.drawable.image_splashscreen_1,"Любая помощь важна и нужна", R.drawable.background_splash_screen_title_1),
        Triple(R.drawable.image_splashscreen_2, "Бережное отношение к пациентам и их близким", R.drawable.background_splash_screen_title_2),
        Triple(R.drawable.image_splashscreen_3, "Творческий и осознанный подход к жизни до конца", R.drawable.background_splash_screen_title_2),
        Triple(R.drawable.image_splashscreen_4, "Ответственно и осознанно нести добро людям", R.drawable.background_splash_screen_title_4),
        Triple(R.drawable.image_splashscreen_5, "Помощь – это создание комфорта для пациентов и их близких", R.drawable.background_splash_screen_title_5),
        Triple(R.drawable.image_splashscreen_6, "Ответственно и осознанно нести добро людям", R.drawable.background_splash_screen_title_1),
        Triple(R.drawable.image_splashscreen_7, "Творческий и осознанный подход к жизни пациента", R.drawable.background_splash_screen_title_6),
        Triple(R.drawable.image_splashscreen_8, "Добро есть везде и во всех", R.drawable.background_splash_screen_title_4),
        Triple(R.drawable.image_splashscreen_9, "Ответственная доброта", R.drawable.background_splash_screen_title_2),
        Triple(R.drawable.image_splashscreen_10, "Создание физического и психологического пространства для завершения жизни", R.drawable.background_splash_screen_title_3),
        Triple(R.drawable.image_splashscreen_11, "Творческий и осознанный подход к жизни пациента", R.drawable.background_splash_screen_title_6),
        Triple(R.drawable.image_splashscreen_12, "Чем больше мы принимаем добра, тем больше отдаем", R.drawable.background_splash_screen_title_1),
        Triple(R.drawable.image_splashscreen_13, "Хоспис – это воплощенная гуманность", R.drawable.background_splash_screen_title_5),
        Triple(R.drawable.image_splashscreen_14, "Хоспис — это призвание и служение человечеству", R.drawable.background_splash_screen_title_6),
        Triple(R.drawable.image_splashscreen_15, "Хоспис – это наука помощи и искусство ухода", R.drawable.background_splash_screen_title_1),
        Triple(R.drawable.image_splashscreen_16, "Ответственно и осознанно нести добро людям", R.drawable.background_splash_screen_title_3),
        Triple(R.drawable.image_splashscreen_17, "Хоспис – это компетентная помощь и любовь к пациентам", R.drawable.background_splash_screen_title_4)
    )
    private val splashscreenImage = splashscreenImages.random()

    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(binding.root)

        binding.splashscreenImageView.setBackgroundResource(splashscreenImage.first)
        binding.splashscreenTextView.apply {
            text = splashscreenImage.second
            setBackgroundResource(splashscreenImage.third)
        }

        Handler().postDelayed({
            startActivity(Intent(this, AppActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3000)
    }

    private fun makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
    }
}