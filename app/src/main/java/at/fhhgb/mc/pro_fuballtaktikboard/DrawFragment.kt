package at.fhhgb.mc.pro_fuballtaktikboard

import android.content.DialogInterface
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.AbsSeekBar
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.FragmentDrawBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrawFragment : Fragment(), SurfaceHolder.Callback, View.OnTouchListener, View.OnClickListener,
    DialogInterface.OnClickListener {

    private lateinit var binding: FragmentDrawBinding
    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var drawBitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDrawBinding.inflate(inflater, container, false)

        surfaceView = binding.surfaceView
        surfaceHolder = surfaceView.holder

        surfaceHolder.setFormat(PixelFormat.TRANSPARENT)
        surfaceHolder.addCallback(this)

        surfaceView.setZOrderOnTop(true)
        surfaceView.setBackgroundColor(Color.WHITE)
        surfaceView.setOnTouchListener(this)


        with(binding) {
            fragmentDrawPencil.setOnClickListener(this@DrawFragment)
            fragmentDrawDelete.setOnClickListener(this@DrawFragment)
            fragmentDrawRubber.setOnClickListener(this@DrawFragment)
            fragmentDrawText.setOnClickListener(this@DrawFragment)

            fragmentDrawBlack.setOnClickListener(this@DrawFragment)
            fragmentDrawWhite.setOnClickListener(this@DrawFragment)
            fragmentDrawRed.setOnClickListener(this@DrawFragment)
            fragmentDrawBlue.setOnClickListener(this@DrawFragment)
            fragmentDrawYellow.setOnClickListener(this@DrawFragment)
            fragmentDrawOrange.setOnClickListener(this@DrawFragment)

            fragmentDrawShirt.setOnClickListener(this@DrawFragment)
            fragmentDrawBall.setOnClickListener(this@DrawFragment)
            fragmentDrawLittleHat.setOnClickListener(this@DrawFragment)
            fragmentDrawCircle.setOnClickListener(this@DrawFragment)
            fragmentDrawGoal.setOnClickListener(this@DrawFragment)
            fragmentDrawFlag.setOnClickListener(this@DrawFragment)
        }

        //paint settings
        paint = Paint()
        paint.apply {
            isAntiAlias = true
            isDither = true
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 40f
        }
        return binding.root
    }



    // --------------------------------------------------

    //create new surfaceView
    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceHolder = holder
        path = Path()
    }

    //change surfaceView
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        surfaceHolder = holder
        drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    //reset surface when went back or forward
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        super.onDestroyView()
    }

    //draw line on canvas - connect the touched points and draw line
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null) {
            val x: Float = event.x
            val y: Float = event.y

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(x, y)
                }
                MotionEvent.ACTION_UP -> {
                    path.lineTo(x, y)
                }
                MotionEvent.ACTION_MOVE -> {
                    path.lineTo(x, y)
                }
            }

            //write path to canvas (from there into surfaceView)
            GlobalScope.launch {
                writePathToCanvas(path)
            }
        }
        surfaceView.performClick()
        return true
    }

    //geth path from with coordinates and load into canvas (then to surfaceView)
    private suspend fun writePathToCanvas(path: Path) {
        withContext(Dispatchers.IO) {
            var canvas = Canvas(drawBitmap)
            canvas.drawPath(path, paint)
            canvas = surfaceHolder.lockCanvas(null)
            canvas.drawBitmap(drawBitmap, 0f, 0f, null)
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    // --------------------------------------------------

    //set strength of pencil, rubber - and set color for color fragments
    override fun onClick(v: View?) {
        when (v?.id) {

            //delete -> overload canvas
            R.id.fragment_draw_delete -> {
                clearCanvas()
            }

            //set strength of pencil
            R.id.fragment_draw_pencil -> {
                paint.strokeWidth = 40f
            }

            //set strength of rubber and color
            R.id.fragment_draw_rubber -> {
                paint.strokeWidth = 40f
                paint.color = Color.WHITE
            }

            //get color for color fragments
            R.id.fragment_draw_black -> {
                paint.color = Color.BLACK
            }

            R.id.fragment_draw_white -> {
                paint.color = Color.WHITE
            }

            R.id.fragment_draw_red -> {
                paint.color = Color.RED
            }

            R.id.fragment_draw_blue -> {
                paint.color = Color.BLUE
            }

            R.id.fragment_draw_yellow -> {
                paint.color = Color.YELLOW
            }

            R.id.fragment_draw_orange -> {
                //used RGB-Code for color "orange"
                paint.setColor(Color.rgb(255, 165, 0))
            }
        }
        if (this::path.isInitialized) {
            path.reset()
        }
    }

    //clear canvas and load into surfaceview -> by deleting
    private fun clearCanvas() {
        var canvas = Canvas(drawBitmap)
        canvas.drawColor(Color.WHITE)
        canvas = surfaceHolder.lockCanvas(null)
        canvas.drawBitmap(drawBitmap, 0f, 0f, null)
        surfaceHolder.unlockCanvasAndPost(canvas)
        path.reset()
    }

    //return bitmap to activity
    public fun getBitmap(): Bitmap {
        return drawBitmap
    }

    //not used
    override fun onClick(dialog: DialogInterface?, which: Int) {
        TODO("Not yet implemented")
    }
}