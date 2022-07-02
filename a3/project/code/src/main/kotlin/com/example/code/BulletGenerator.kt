package com.example.code


class BulletGenerator(val enemiesVBox: EnemiesVBox) : Observer{
    private val totalNumEnemies = 50
    var observersManager: ObserversManager? = null
    private var state = 0

    private fun generateBullets(){
        val seed1 = (0 until totalNumEnemies).random()
        val pair = enemiesVBox.getNewBulletPosition(seed1)
        val x = pair.first
        val y = pair.second
        val bullet = Bullet(x, y, 20.0, 20.0, BulletOwners.VillanType3)
        observersManager?.add1ToCollisionHandler(bullet)
        observersManager?.addToPane(bullet)
        observersManager?.addToTimer(bullet)
    }

    override fun update(){
        state += 1
        if (state%50 == 0){
            generateBullets()
        }
    }
}