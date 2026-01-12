package com.arturmaslov.lumic

import android.content.Context
import android.media.MediaRecorder
import com.arturmaslov.lumic.utils.AudioUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verifyOrder
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.io.path.createTempDirectory

@OptIn(ExperimentalCoroutinesApi::class)
class AudioUtilsTest {

    private lateinit var audioUtils: AudioUtils
    private val mockContext = mockk<Context>(relaxed = true)
    private val mockMediaRecorder = mockk<MediaRecorder>(relaxed = true)

    @Before
    fun setUp() {
        // Mock MediaRecorder's behavior globally
        mockkConstructor(MediaRecorder::class)
        every { anyConstructed<MediaRecorder>().setAudioSource(any()) } answers {
            mockMediaRecorder.setAudioSource(firstArg())
        }
        every { anyConstructed<MediaRecorder>().setOutputFormat(any()) } answers {
            mockMediaRecorder.setOutputFormat(firstArg())
        }
        every { anyConstructed<MediaRecorder>().setAudioEncoder(any()) } answers {
            mockMediaRecorder.setAudioEncoder(firstArg())
        }
        every { anyConstructed<MediaRecorder>().setOutputFile(ofType<String>()) } answers {
            mockMediaRecorder.setOutputFile(firstArg<String>())
        }
        every { anyConstructed<MediaRecorder>().prepare() } answers { mockMediaRecorder.prepare() }
        every { anyConstructed<MediaRecorder>().start() } answers { mockMediaRecorder.start() }
        every { anyConstructed<MediaRecorder>().maxAmplitude } returnsMany listOf(100, 200, 300)

        // Instantiate AudioUtils
        audioUtils = AudioUtils(mockContext)
    }

    @After
    fun tearDown() {
        unmockkAll() // Clear all mocks after tests
    }

    @Test
    fun `startRecording should setup and start MediaRecorder`() = runTest {
        // Mock context behavior
        val testFileDir = File("rec_audio.mp3")
        every { mockContext.filesDir } returns testFileDir

        audioUtils.startRecording()

        val expectedFilePath = File(mockContext.filesDir, "rec_audio.mp3").absolutePath

        // Verify MediaRecorder methods were called in order
        verifyOrder {
            mockMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mockMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mockMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mockMediaRecorder.setOutputFile(expectedFilePath)
            mockMediaRecorder.prepare()
            mockMediaRecorder.start()
        }
    }

    @Test
    fun `deleteRecording should clear volumeData and delete output file`() {
        val tempDirPath = createTempDirectory("audio_test_")
        val tempDir = tempDirPath.toFile()

        val realFile = File(tempDir, "rec_audio.mp3")
        realFile.writeText("dummy")

        every { mockContext.filesDir } returns tempDir

        audioUtils.startRecording()
        audioUtils.stopRecording()
        audioUtils.deleteRecording()

        assertFalse(realFile.exists())
        assertTrue(audioUtils.volumeData.isEmpty())

        tempDir.deleteRecursively() // optional cleanup
    }
}

