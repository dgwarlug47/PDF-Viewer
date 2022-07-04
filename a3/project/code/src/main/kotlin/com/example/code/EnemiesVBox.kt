package com.example.code

import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.shape.Rectangle
import java.lang.Float.*

class EnemiesVBox() :  VBox(), Observer{
    private val classLoader = Thread.currentThread().contextClassLoader
    private val something = classLoader.getResource("fastinvader1.wav")?.toString()
    private val media = Media(something)
    var level = 1
        set (level){
            if (level == 1){
                currentXVelocity = 0.33
                offsetXVelocity = 0.02
            }
            if (level == 2){
                currentXVelocity = 0.66
                offsetXVelocity = 0.07
            }
            if (level == 3){
                currentXVelocity = 1.0
                offsetXVelocity = 0.15
            }
            field = level
        }
    private var direction = 1
    private var currentXVelocity = 1.0
    private var offsetXVelocity = 0.2

    var observersManager: ObserversManager? = null

    private val enemyHBox1 = EnemiesHBox(EnemyType.type1, 0)
    private val enemyHBox2 = EnemiesHBox(EnemyType.type2, 1)
    private val enemyHBox3 = EnemiesHBox(EnemyType.type2, 2)
    private val enemyHBox4 = EnemiesHBox(EnemyType.type2, 3)
    private val enemyHBox5 = EnemiesHBox(EnemyType.type3, 4)

    // enemies offset bound
    private var enemiesLeftOffsetBound = 0.0
    private var enemiesRightOffsetBound = 0.0

    init {
        children.add(enemyHBox1)
        children.add(enemyHBox2)
        children.add(enemyHBox3)
        children.add(enemyHBox4)
        children.add(enemyHBox5)
    }
    override fun update(){
        updateEnemiesBounds()
        if (this.translateX + this.enemiesRightOffsetBound >= CANVAS_WIDTH || this.translateX + this.enemiesLeftOffsetBound < 0){
            this.translateY = this.translateY + 20
            direction = -direction
            MediaPlayer(media).play()
            observersManager!!.generateBullets()
        }
        this.translateX = this.translateX + direction * currentXVelocity
    }
    fun attach(){
        for (hbox in children){
            for (child in (hbox as HBox).children) {
                observersManager?.add1ToCollisionHandler(child as Rectangle)
            }
        }
    }
    fun getNewBulletPosition(seed: Int): Triple<Double, Double, EnemyType>{
        var it = 0
        while (true) {
            for (hbox in children) {
                for (child in (hbox as HBox).children) {
                    if (child !is Enemy){
                        continue
                    }
                    if (!child.alive){
                        continue
                    }
                    if (seed == it) {
                        return Triple(this.translateX + child.localToParentTransform.tx
                            , this.translateY + hbox.localToParentTransform.ty, child.enemyType)
                    }
                    it += 1
                }
            }
        }
    }
    fun enemyWasHit(){
        updateEnemiesBounds()
        currentXVelocity += offsetXVelocity
    }
    private fun updateEnemiesBounds(){
        var leftBound = POSITIVE_INFINITY
        var rightBound = NEGATIVE_INFINITY
        for (hbox in children) {
            for (child in (hbox as HBox).children) {
                if (child !is Enemy){
                    continue
                }
                if (!child.alive) {
                    continue
                }
                leftBound = min(leftBound, child.boundsInParent.minX.toFloat())
                rightBound = max(rightBound, child.boundsInParent.maxX.toFloat())
            }
        }
        if (leftBound != POSITIVE_INFINITY){
            this.enemiesLeftOffsetBound = leftBound.toDouble()
        }
        if (rightBound != Float.NEGATIVE_INFINITY){
            this.enemiesRightOffsetBound = rightBound.toDouble()
        }
    }
}