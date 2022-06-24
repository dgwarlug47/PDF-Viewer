package com.example.code


interface Subject {
    val observers: MutableList<Observer>

    fun attach(observer: Observer) {
        observers.add(observer)
    }

    fun notifyAllObservers() {
        val iterator = observers.iterator()
        while(iterator.hasNext()){
            iterator.next().update()
        }
    }
}