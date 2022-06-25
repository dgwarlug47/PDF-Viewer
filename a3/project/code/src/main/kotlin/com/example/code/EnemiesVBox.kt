package com.example.code

import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle

class EnemiesVBox() :  VBox(), Observer{
    private val enemyList : MutableList<Enemy> = mutableListOf()
    var direction = 1

    var observersManager: ObserversManager? = null

    init {
        val enemyHBox1 = EnemiesHBox(EnemyType.type1)
        val enemyHBox2 = EnemiesHBox(EnemyType.type2)
        val enemyHBox3 = EnemiesHBox(EnemyType.type2)
        val enemyHBox4 = EnemiesHBox(EnemyType.type2)
        val enemyHBox5 = EnemiesHBox(EnemyType.type3)

        children.add(enemyHBox1)
        children.add(enemyHBox2)
        children.add(enemyHBox3)
        children.add(enemyHBox4)
        children.add(enemyHBox5)
    }
    override fun update(){
        if (this.translateX + this.width >= CANVAS_WIDTH || this.translateX < 0){
            this.translateY = this.translateY + 3
            direction = -direction
        }
        this.translateX = this.translateX + direction*3
    }
    fun attach(){
        for (hbox in children){
            for (child in (hbox as HBox).children) {
                observersManager?.add1ToCollisionHandler(child as Rectangle)
            }
        }
    }
    fun getBulletPosition(seed: Int): Pair<Double, Double>{
        var it = 0
        while (true) {
            for (hbox in children) {
                for (child in (hbox as HBox).children) {
                    if (seed == it) {
                        println("seed again")
                        println(hbox.localToParentTransform.ty)
                        return Pair(this.translateX + child.localToParentTransform.tx
                            , this.translateY + hbox.localToParentTransform.ty)
                    }
                    it += 1
                }
            }
        }
    }
}