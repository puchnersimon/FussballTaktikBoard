package at.fhhgb.mc.pro_fuballtaktikboard

import android.content.DialogInterface
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.widget.RelativeLayout
import androidx.core.graphics.scale
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
    private var listCanvas: MutableList<Canvas> = arrayListOf()
    var drawLine: Boolean = true
    private lateinit var soccerElement: Bitmap
    //for centering the elements by input-touching
    var centerX: Int = 50
    var centerY: Int = 50


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
        surfaceView.setBackgroundColor(Color.TRANSPARENT)
        surfaceView.setOnTouchListener(this)

        //listCanvas = mutableListOf<Canvas>()

        //set fragment clickable
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

            fragmentDrawLadder.setOnClickListener(this@DrawFragment)
            fragmentDrawShirtblue.setOnClickListener(this@DrawFragment)
            fragmentDrawShirtred.setOnClickListener(this@DrawFragment)
            fragmentDrawShirtorange.setOnClickListener(this@DrawFragment)
            fragmentDrawShirtviolet.setOnClickListener(this@DrawFragment)
            fragmentDrawShirtgreen.setOnClickListener(this@DrawFragment)
            fragmentDrawShirtyellow.setOnClickListener(this@DrawFragment)
            fragmentDrawBall.setOnClickListener(this@DrawFragment)
            fragmentDrawLittleHat.setOnClickListener(this@DrawFragment)
            fragmentDrawCircle.setOnClickListener(this@DrawFragment)
            fragmentDrawGoal.setOnClickListener(this@DrawFragment)
            fragmentDrawFlag.setOnClickListener(this@DrawFragment)
        }

        //standard-settings for painting
        paint = Paint()
        paint.apply {
            isAntiAlias = true
            isDither = true
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 15f
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
        soccerElement = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }

    //reset/destroy surface when went back or forward
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        super.onDestroyView()
    }

    //draw line into path - connect the touched points and draw line
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        if (event != null) {
            val x: Float = event.x
            val y: Float = event.y

            //get coordinates for soccerelement
            //drawLine set to false if a soccerelement is choosen in palette
            if (drawLine == false) {
                drawElement(x, y)
            } else {

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
        }
        surfaceView.performClick()
        return true
    }

    //get path with coordinates and load into canvas (then to surfaceView/Holder)
    private suspend fun writePathToCanvas(path: Path) {
        withContext(Dispatchers.IO) {
            var canvas = Canvas(drawBitmap)

            canvas.drawPath(path, paint)

            //add/save canvas to list
            //listCanvas.add(canvas)

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
                paint.strokeWidth = 15f
                drawLine = true
            }

            //set strength of rubber and color
            R.id.fragment_draw_rubber -> {
                paint.strokeWidth = 40f
                paint.color = Color.parseColor("#01a243")
                drawLine = true
            }

            //get color for color fragments
            R.id.fragment_draw_black -> {
                paint.strokeWidth = 15f
                paint.color = Color.BLACK
                drawLine = true
            }

            R.id.fragment_draw_white -> {
                paint.strokeWidth = 15f
                paint.color = Color.WHITE
                drawLine = true
            }

            R.id.fragment_draw_red -> {
                paint.strokeWidth = 15f
                paint.color = Color.RED
                drawLine = true
            }

            R.id.fragment_draw_blue -> {
                paint.strokeWidth = 15f
                paint.color = Color.BLUE
                drawLine = true
            }

            R.id.fragment_draw_yellow -> {
                paint.strokeWidth = 15f
                paint.color = Color.YELLOW
                drawLine = true
            }

            R.id.fragment_draw_orange -> {
                paint.strokeWidth = 15f
                //used RGB-Code for color "orange"
                paint.setColor(Color.rgb(255, 165, 0))
                drawLine = true
            }

            //settings shirt
            R.id.fragment_draw_shirtblue -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtblue)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 250, 250, false)
                drawLine = false
                centerX = 125
                centerY = 125
            }
            R.id.fragment_draw_shirtgreen -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtgreen)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 250, 250, false)
                drawLine = false
                centerX = 125
                centerY = 125
            }
            R.id.fragment_draw_shirtred -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtred)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 250, 250, false)
                drawLine = false
                centerX = 125
                centerY = 125
            }
            R.id.fragment_draw_shirtyellow -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtyellow)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 250, 250, false)
                drawLine = false
                centerX = 125
                centerY = 125
            }
            R.id.fragment_draw_shirtviolet -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtviolet)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 250, 250, false)
                drawLine = false
                centerX = 125
                centerY = 125
            }
            R.id.fragment_draw_shirtorange -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtorange)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 250, 250, false)
                drawLine = false
                centerX = 125
                centerY = 125
            }

            // settings ladder
            R.id.fragment_draw_ladder -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.ladder)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 150, 150, false)
                drawLine = false
                drawLine = false
                centerX = 80
                centerY = 80
            }

            //settings ball
            R.id.fragment_draw_ball -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.ball)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                drawLine = false
                centerX = 50
                centerY = 50
            }

            //settings littlehat
            R.id.fragment_draw_littleHat -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.littlehat)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 120, 120, false)
                drawLine = false
                centerX = 60
                centerY = 60
            }

            //settings circle
            R.id.fragment_draw_circle -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.circle)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 120, 120, false)
                drawLine = false
                centerX = 60
                centerY = 65

            }

            //settings goal
            R.id.fragment_draw_goal -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.goal)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 250, 250, false)
                drawLine = false
                centerX = 120
                centerY = 120
            }

            //settings flag
            R.id.fragment_draw_flag -> {
                val myDrawable = requireActivity().getDrawable(R.drawable.flag)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 200, 200, false)
                drawLine = false
                centerX = 95
                centerY = 95
            }

            R.id.fragment_draw_text -> {
                print("test")
            }

        }
        if (this::path.isInitialized) {
            path.reset()
        }
    }


    //draw element into bitmap with x-coordinates and y-coordinates from onTouch()
    private fun drawElement(x: Float, y: Float) {
        var canvas = Canvas()
        canvas = surfaceHolder.lockCanvas(null)
        //canvas.drawColor(Color.TRANSPARENT)

        //centerX & centerY for set element in middle of touch
        canvas.drawBitmap(soccerElement, x-centerX, y-centerY, null)
        //listCanvas.add(canvas)
        surfaceHolder.unlockCanvasAndPost(canvas)
        path.reset()
    }


    //clear canvas and load into surfaceview/holder -> by deleting
    private fun clearCanvas() {
        drawBitmap = Bitmap.createBitmap(drawBitmap.width, drawBitmap.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(drawBitmap)
        //canvas.drawColor(Color.TRANSPARENT)

        canvas = surfaceHolder.lockCanvas(null)
        canvas.drawBitmap(drawBitmap, 0f, 0f, null)
        //canvas.setBitmap(drawBitmap)
        canvas.drawColor(0, PorterDuff.Mode.CLEAR)
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