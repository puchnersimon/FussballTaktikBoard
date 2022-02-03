package at.fhhgb.mc.pro_fuballtaktikboard

class Point constructor(
    override var x: Float = 0f,
    override var y: Float = 0f,
    var color: Int = 0
): Object {

    fun returnX(): Float {
        return x
    }
    fun returnY():Float{
        return y
    }
}


