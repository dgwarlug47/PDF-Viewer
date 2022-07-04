package com.example.code


class BulletGenerator(private val enemiesVBox: EnemiesVBox) : Observer{
    private val totalNumEnemies = 50
    var observersManager: ObserversManager? = null

    fun generateBullets(){
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
        val seed = (0 until 52).random()
        if (!DEBUG or DEBUG) {
            if (seed == 0) {
                generateBullets()
            }
        }
    }
}