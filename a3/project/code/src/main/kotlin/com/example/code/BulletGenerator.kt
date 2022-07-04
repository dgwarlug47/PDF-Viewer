package com.example.code


class BulletGenerator(private val enemiesVBox: EnemiesVBox) : Observer{
    private val totalNumEnemies = 50
    var observersManager: ObserversManager? = null
    private var state = 0

    private fun generateBullets(){
        val seed1 = (0 until totalNumEnemies).random()
        val triple = enemiesVBox.getNewBulletPosition(seed1)
        val x = triple.first
        val y = triple.second
        var bulletOwners = BulletOwners.VillanType1
        if (triple.third == EnemyType.type3){
            bulletOwners = BulletOwners.VillanType3
        }
        if (triple.third == EnemyType.type2){
            bulletOwners = BulletOwners.VillanType2
        }
        val bullet = Bullet(x, y, 20.0, 20.0, bulletOwners)
        observersManager?.add1ToCollisionHandler(bullet)
        observersManager?.addToPane(bullet)
        observersManager?.queueAddToTimer(bullet)
    }

    override fun update(){
        state += 1
        if (!DEBUG) {
            if (state % 50 == 0) {
                generateBullets()
            }
        }
        if (DEBUG) {
            if (state % 50 == 0) {
                generateBullets()
            }
        }
    }
}