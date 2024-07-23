package com.example.todo.Pomodoro

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.R
import com.example.todo.RegisterAndLogin.LoginActivity
import com.example.todo.cart.UserSoundDao

class Pomodoro: AppCompatActivity() {
    private val remainMinutesTextView: TextView by lazy {
        findViewById(R.id.remainMinutesTextView)

    }

    private val remainSecondsTextView: TextView by lazy {
        findViewById(R.id.remainSecondsTextView)
    }
    private val stopButton: Button by lazy {
        findViewById(R.id.stopButton)
    }
    private val startButton: Button by lazy {
        findViewById(R.id.startButton)
    }
    private val seekBar: SeekBar by lazy {
        findViewById(R.id.seekbar)
    }
    private val musicButton: Button by lazy {
        findViewById(R.id.musicButton)
    }
    lateinit var spinner: Spinner
    var userSoundDao: UserSoundDao? = null
    var userSoundslist = ArrayList<String>()
    lateinit var userSounds: Array<String>

    private var currentCountDownTimer: CountDownTimer? = null
    var isPlaying = false

    private val soundPool: SoundPool = SoundPool.Builder().build()
    private var tickingSoundId: Int? = null
    private var bellSoundId: Int? = null
    private var stopTime: Long? = null
    private var mediaPlayer: MediaPlayer? = null

    private var lastSelected: Int = 0
    private var isChanged: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.pomodoro)

        userSoundDao = UserSoundDao(this)
        spinner = findViewById(R.id.spinner)
        userSoundslist.add(getString(R.string.Default))
        val res = userSoundDao!!.getAllData(LoginActivity.userID)
        if (res.getCount() != 0) {
            val buffer = StringBuffer()
            var soundName: String
            while (res.moveToNext()) {
                buffer.append(res.getString(0))
                buffer.delete(0, buffer.length)
                buffer.append(res.getString(1))
                buffer.delete(0, buffer.length)
                buffer.append(res.getString(2))
                soundName = buffer.toString()
                userSoundslist.add(soundName)
                buffer.delete(0, buffer.length)
            }
        }
        userSounds = userSoundslist.toTypedArray()
        val adapterUnits = ArrayAdapter(
            this,
            androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,
            userSounds
        )
        spinner.setAdapter(adapterUnits)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val index: Int? = parent?.getSelectedItemPosition()
                if (index != lastSelected && index != null){
                    isChanged = true
                    lastSelected = index
                }
                else {
                    isChanged = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                isChanged = false
            }

        }

        bindViews()
        initSounds()
        musicButton.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, soundChoose())
                mediaPlayer?.start()
                isPlaying = true
            } else {
                if (isPlaying) {
                    mediaPlayer?.pause()
                    isPlaying = false
                } else {
                    if (!isChanged){
                        mediaPlayer?.start()
                    }
                    else {
                        mediaPlayer?.release()
                        mediaPlayer = null
                        mediaPlayer = MediaPlayer.create(this, soundChoose())
                        mediaPlayer?.start()
                    }
                    isPlaying = true
                }
            }
        }
        stopButton.setOnClickListener {
            stopCountDown()
        }
        startButton.setOnClickListener {
            keepCountDown()
        }
    }

    private fun bindViews() {
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    if(fromUser)
                        updateRemainTime(progress * 60 * 100L)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    stopCountDown()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar ?: return
                    stopCountDown()

                    if(seekBar.progress == 0){


                    }else{
                        startCountDown(seekBar)
                    }
                }
            }
        )
    }

    private fun stopCountDown() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        soundPool.autoPause()
        stopTime = System.currentTimeMillis()

    }

    private fun startCountDown(seekBar: SeekBar) {
        val remainMillis = if (stopTime != null) {
            seekBar.progress * 60 * 1000L - (System.currentTimeMillis() - stopTime!!)
        } else {
            seekBar.progress * 60 * 1000L
        }
        currentCountDownTimer = createCountDownTimer(remainMillis)
        currentCountDownTimer?.start()

        tickingSoundId?.let { soundId ->
            soundPool.play(soundId, 1F, 1F, 0, -1, 1F)
        }
    }





    private fun initSounds(){
        tickingSoundId = soundPool.load(this,R.raw.timer_ticking, 1)
        bellSoundId = soundPool.load(this, R.raw.timer_bell, 1)
    }

    private fun createCountDownTimer(initialMillis: Long) =
        object : CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {

                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }


    private fun completeCountDown() {
        updateRemainTime(0)
        updateSeekBar(0)

        soundPool.autoPause() //Ticking sound
        bellSoundId?.let { soundId ->
            soundPool.play(soundId, 1F, 1F, 0, 0, 1F)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
        Log.d(
            "MainActivity",
            "Text update" + remainMinutesTextView.text + ", " + remainSecondsTextView.text
        )
    }

    private fun updateSeekBar(remainMillis: Long) {
        seekBar.progress = (remainMillis / 1000 / 60).toInt()

    }
    private fun keepCountDown() {
        if (currentCountDownTimer == null) {
            val remainSeconds = try {
                remainSecondsTextView.text.toString().toInt()
            } catch (e: Exception) {
                0
            }
            val totalMillis = (seekBar.progress * 60 * 1000L) + (remainSecondsTextView.text.toString().toInt() * 1000L)
            currentCountDownTimer = createCountDownTimer(totalMillis)
            currentCountDownTimer?.start()
            tickingSoundId?.let { soundId ->
                soundPool.play(soundId, 1F, 1F, 0, -1, 1F)
            }
        }
    }

    //    override fun onStart() {
//        super.onStart()
//        Log.d("onStart","onStart")
//    }

    private fun soundChoose(): Int {
        val titles = resources.getStringArray(R.array.title_array)
        var soundID:Int = R.raw.first;
        if (spinner.selectedItem.toString().equals(getString(R.string.Default)))
            soundID = R.raw.first;
        if (spinner.selectedItem.toString().equals(titles[0]))
            soundID = R.raw.rain;
        if (spinner.selectedItem.toString().equals(titles[1]))
            soundID = R.raw.beach;
        if (spinner.selectedItem.toString().equals(titles[2]))
            soundID = R.raw.bell;
        if (spinner.selectedItem.toString().equals(titles[3]))
            soundID = R.raw.forest;
        if (spinner.selectedItem.toString().equals(titles[4]))
            soundID = R.raw.wind;
        return soundID
    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume","onResume")
        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("onPause","onPause")
        soundPool.autoPause()
    }

//    override fun onStop() {
//        super.onStop()
//        Log.d("OnStop","Onstop")
//    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        soundPool.release()

    }
}