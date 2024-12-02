package com.arturmaslov.lumic.utils

import android.media.MediaRecorder
import android.content.Context
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class AudioUtils(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    val volumeData = mutableListOf<Int>() // Store volume levels
    private var isRecording: Boolean = false
    private var outputFile: String? = null
    private var amplitudeCaptureJob: Job? = null

    private fun setOutputFile() {
        val fileName = "rec_audio.mp3"
        outputFile = File(context.filesDir, fileName).absolutePath
    }

    private fun setupMediaRecorder() {
        setOutputFile()
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFile)
        }
    }

    fun startRecording() {
        if (isRecording) {
            Timber.w("Recording is already in progress.")
            return
        } else {
            setupMediaRecorder()
            try {
                mediaRecorder?.prepare()
                mediaRecorder?.start()
            } catch (e: IllegalStateException) {
                Timber.e(e)
            }

            isRecording = true
            captureAmplitude()
        }
    }

    private fun captureAmplitude() {
        amplitudeCaptureJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    val amplitude = mediaRecorder?.maxAmplitude ?: 0 // Fetch current amplitude
                    delay(Constants.AMPLITUDE_RECORD_INTERVAL_MS)
                    volumeData.add(amplitude)
                } catch (e: IllegalStateException) {
                    Timber.e(e)
                }
            }
        }
    }

    fun stopRecording() {
        if (!isRecording) {
            Timber.w("Recording is not in progress.")
            return
        } else {
            amplitudeCaptureJob?.cancel()
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            isRecording = false
            deleteRecording()
        }
    }

    fun deleteRecording() {
        volumeData.clear()
        outputFile?.let { File(it).delete() }
        outputFile = null
    }
}