package org.iesharia.hiittimer

import android.os.CountDownTimer

class CounterDown(var segundos: Int, var loquehacealhacertick: (Long) -> Unit) {
    private var counterState: Boolean = false
    private var restante: Long = segundos * 1000L
    private var myCounter: CountDownTimer? = null

    fun start() {
        counterState = true
        myCounter = object : CountDownTimer(restante, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (counterState) {
                    restante = millisUntilFinished
                    loquehacealhacertick(millisUntilFinished / 1000)
                }
            }

            override fun onFinish() {
                counterState = false
                loquehacealhacertick(0)
            }
        }.start()
    }

    fun pause() {
        counterState = false
        myCounter?.cancel()
    }

    fun resume() {
        counterState = true
        start()
    }

    fun cancel() {
        counterState = false
        myCounter?.cancel()
    }
}
