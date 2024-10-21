package org.iesharia.hiittimer

import android.os.CountDownTimer

class CounterDown(var segundos: Int, var loquehacealhacertick: (Long) -> Unit) {
    private var counterState : Boolean = false
    val myCounter = object : CountDownTimer((segundos * 1000L), 1000) {
        override fun onTick(millisUntilFinished: Long) {
            if (counterState) loquehacealhacertick(millisUntilFinished / 1000)
        }
        override fun onFinish() {
            counterState = false
        }
    }

    fun start() {
        counterState = true
        this.myCounter.start()
    }
    fun cancel() {
        counterState = false
        this.myCounter.cancel()
    }
}
