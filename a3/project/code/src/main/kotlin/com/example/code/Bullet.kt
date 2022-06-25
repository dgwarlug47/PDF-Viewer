package com.example.code

import javafx.scene.image.Image
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import java.io.File

enum class BulletOwners{
    Players,
    VillanType1,
    VillanType2,
    VillanType3
}

class Bullet(x: Double, y: Double, width: Double, height: Double, bulletOwners: BulletOwners) : Rectangle(x, y, width, height), Observer{
    var direction = - 1
    init {
        var addrs = "1"
        if (bulletOwners == BulletOwners.VillanType2){
            addrs = "2"
        }
        if (bulletOwners == BulletOwners.VillanType3){
            addrs = "3"
        }
        var path = "space-invaders-assets/images/bullet$addrs.png"
        if (bulletOwners == BulletOwners.Players){
            path = "space-invaders-assets/images/player_bullet.png"
        }
        this.fill = ImagePattern(Image(File(path).toURI().toString()))
        if (bulletOwners != BulletOwners.Players){
            direction = 1
        }
    }

    override fun update() {
        this.translateY = this.translateY + direction*7
    }
}