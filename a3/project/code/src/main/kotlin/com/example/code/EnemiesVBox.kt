package com.example.code

import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import java.lang.Float.*

class EnemiesVBox() :  VBox(), Observer{
    private val enemyList : MutableList<Enemy> = mutableListOf()
    var direction = 1
    var currentXVelocity = 1.0

    var observersManager: ObserversManager? = null

    val enemyHBox1 = EnemiesHBox(EnemyType.type1, 0)
    val enemyHBox2 = EnemiesHBox(EnemyType.type2, 1)
    val enemyHBox3 = EnemiesHBox(EnemyType.type2, 2)
    val enemyHBox4 = EnemiesHBox(EnemyType.type2, 3)
    val enemyHBox5 = EnemiesHBox(EnemyType.type3, 4)

    // enemies offset bound
    var enemiesLeftOffsetBound = 0.0
    var enemiesRightOffsetBound = 0.0

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
            this.translateY = this.translateY + 3
            direction = -direction
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
    fun getNewBulletPosition(seed: Int): Pair<Double, Double>{
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
                        return Pair(this.translateX + child.localToParentTransform.tx
                            , this.translateY + hbox.localToParentTransform.ty)
                    }
                    it += 1
                }
            }
        }
    }
    fun enemyWasHit(){
        updateEnemiesBounds()
        currentXVelocity += 0.2
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