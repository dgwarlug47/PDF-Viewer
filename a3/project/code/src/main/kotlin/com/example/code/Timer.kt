package com.example.code

import javafx.animation.AnimationTimer

class Timer () : Subject{
    override var observers: MutableList<Observer> = mutableListOf()
    private val timer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            notifyAllObservers()
        }
    }
    init {
        timer.start()
    }
}