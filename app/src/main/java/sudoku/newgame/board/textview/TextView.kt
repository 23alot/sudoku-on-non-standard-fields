package sudoku.newgame.board.textview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TextView : View {
    var value : String = "1"
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        var width : Int = canvas.width
        var paint : Paint = Paint()
        paint.textSize = width.toFloat() - 20
        canvas.drawText(value, 0f, 0f, paint)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}
}
