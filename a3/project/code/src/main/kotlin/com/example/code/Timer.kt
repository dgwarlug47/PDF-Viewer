package com.example.code

import javafx.animation.AnimationTimer

class Timer () : Subject{
    override var observers: MutableList<Observer> = mutableListOf()
    var queueObservers : MutableList<Observer> = mutableListOf()
    private val timer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            notifyAllObservers()
            for (observer in queueObservers){
                attach(observer)
            }
            queueObservers = mutableListOf()
        }
    }
    init {
        timer.start()
    }
    fun queueAttach(observer: Observer){
        queueObservers.add(observer)
    }
    fun stop(){
        timer.stop()
    }
}