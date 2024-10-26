package org.iesharia.hiittimer

import android.os.CountDownTimer

class CounterDown(var segundos: Int, var loquehacealhacertick: (Long) -> Unit) {
    private var restante = segundos * 1000L
    private var myCounter: CountDownTimer? = null
    private var isPaused = false

    fun start() {
        myCounter = object : CountDownTimer(restante, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isPaused) {
                    restante = millisUntilFinished
                    loquehacealhacertick(millisUntilFinished / 1000)
                }
            }

            override fun onFinish() {
                loquehacealhacertick(0)
            }
        }.start()
    }

    fun pause() {
        isPaused = true
        myCounter?.cancel()
    }

    fun resume() {
        isPaused = false
        start()
    }

    fun cancel() {
        isPaused = true
        myCounter?.cancel()
    }
}
