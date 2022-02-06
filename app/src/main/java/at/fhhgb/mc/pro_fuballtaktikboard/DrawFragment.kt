package at.fhhgb.mc.pro_fuballtaktikboard

import android.content.ContentValues
import android.content.DialogInterface
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import at.fhhgb.mc.pro_fuballtaktikboard.databinding.FragmentDrawBinding
import at.fhhgb.mc.pro_fuballtaktikboard.db.ProjectViewModel
import at.fhhgb.mc.pro_fuballtaktikboard.models.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import android.graphics.Bitmap





class DrawFragment(projectViewModel: ProjectViewModel?, project: Project?, page: Int?, background: Bitmap?) : Fragment(), SurfaceHolder.Callback, View.OnTouchListener, View.OnClickListener,
    DialogInterface.OnClickListener {

    private lateinit var binding: FragmentDrawBinding
    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var drawBitmap: Bitmap
    private lateinit var soccerElement: Bitmap
    private var project = project
    private var page = page
    private var projectViewModel = projectViewModel
    private var background = background

    //for centering the elements by input-touching
    var centerX: Int = 50
    var centerY: Int = 50

    var drawLine: Boolean = true


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


        //set fragment clickable
        with(binding) {
            fragmentDrawPencil.setOnClickListener(this@DrawFragment)
            fragmentDrawDelete.setOnClickListener(this@DrawFragment)
            fragmentDrawRubber.setOnClickListener(this@DrawFragment)
            fragmentDrawSaveToGallery.setOnClickListener(this@DrawFragment)

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
        binding.fragmentDrawBlack.setBackgroundColor(Color.DKGRAY)
        binding.fragmentDrawPencil.setBackgroundColor(Color.DKGRAY)
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

        val myDrawable = when(page) {
            FIRST_FIELD -> {
                requireActivity().getDrawable(R.drawable.fullfield)
            }
            SECOND_FIELD -> {
                requireActivity().getDrawable(R.drawable.goalarea)
            }
            else -> {
                requireActivity().getDrawable(R.drawable.freearea)
            }
        }

        var back = (myDrawable as BitmapDrawable).bitmap

        background?.let {
            back = it
        }

        val mutableBitmap: Bitmap = back.copy(Bitmap.Config.ARGB_8888, true)

        drawBitmap = Bitmap.createScaledBitmap(mutableBitmap, width, height, false)

        //load converted bitmap into canvas as background
        var canvas = Canvas(drawBitmap)
        canvas = surfaceHolder.lockCanvas()
        canvas.drawBitmap(drawBitmap, 0f, 0f, null)
        surfaceHolder.unlockCanvasAndPost(canvas)

        //bitmap for soccerelement
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
                if (event.actionMasked == MotionEvent.ACTION_UP) {
                    drawElement(x, y)
                }
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

                //write path to bitmap, then save to canvas (from there into surfaceView)
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

            canvas = surfaceHolder.lockCanvas()
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
                resetColorFragment()
                binding.fragmentDrawDelete.setBackgroundColor(Color.DKGRAY)
                clearCanvas()

                //wait 300 milisec to change color of fragment
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    resetColorFragment()
                }, 300)
            }

            //set strength of pencil
            R.id.fragment_draw_pencil -> {
                resetColorFragment()
                binding.fragmentDrawPencil.setBackgroundColor(Color.DKGRAY)
                paint.strokeWidth = 15f
                drawLine = true
            }

            //set strength of rubber and color
            R.id.fragment_draw_rubber -> {
                resetColorFragment()
                binding.fragmentDrawRubber.setBackgroundColor(Color.DKGRAY)
                paint.strokeWidth = 40f
                paint.color = Color.parseColor("#01a243")
                drawLine = true
            }

            //get color for color fragments
            R.id.fragment_draw_black -> {
                resetColorFragment()
                binding.fragmentDrawBlack.setBackgroundColor(Color.DKGRAY)
                binding.fragmentDrawPencil.setBackgroundColor(Color.DKGRAY)
                paint.strokeWidth = 15f
                paint.color = Color.BLACK
                drawLine = true
            }

            R.id.fragment_draw_white -> {
                resetColorFragment()
                binding.fragmentDrawWhite.setBackgroundColor(Color.DKGRAY)
                binding.fragmentDrawPencil.setBackgroundColor(Color.DKGRAY)
                paint.strokeWidth = 15f
                paint.color = Color.WHITE
                drawLine = true
            }

            R.id.fragment_draw_red -> {
                resetColorFragment()
                binding.fragmentDrawRed.setBackgroundColor(Color.DKGRAY)
                binding.fragmentDrawPencil.setBackgroundColor(Color.DKGRAY)
                paint.strokeWidth = 15f
                paint.color = Color.RED
                drawLine = true
            }

            R.id.fragment_draw_blue -> {
                resetColorFragment()
                binding.fragmentDrawBlue.setBackgroundColor(Color.DKGRAY)
                binding.fragmentDrawPencil.setBackgroundColor(Color.DKGRAY)
                paint.strokeWidth = 15f
                paint.color = Color.BLUE
                drawLine = true
            }

            R.id.fragment_draw_yellow -> {
                resetColorFragment()
                binding.fragmentDrawYellow.setBackgroundColor(Color.DKGRAY)
                binding.fragmentDrawPencil.setBackgroundColor(Color.DKGRAY)
                paint.strokeWidth = 15f
                paint.color = Color.YELLOW
                drawLine = true
            }

            R.id.fragment_draw_orange -> {
                resetColorFragment()
                binding.fragmentDrawOrange.setBackgroundColor(Color.DKGRAY)
                binding.fragmentDrawPencil.setBackgroundColor(Color.DKGRAY)
                paint.strokeWidth = 15f
                //used hex-Code for color "orange"
                paint.setColor(Color.parseColor("#FFA500"))
                drawLine = true
            }

            //settings shirt
            R.id.fragment_draw_shirtblue -> {
                resetColorFragment()
                binding.fragmentDrawShirtblue.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtblue)
                soccerElement = (myDrawable as BitmapDrawable).bitmap

                soccerElement = Bitmap.createScaledBitmap(soccerElement, 220, 220, false)
                centerX = 110
                centerY = 110

                project?.let {
                    if (it.hasEdited) {
                        soccerElement = Bitmap.createScaledBitmap(soccerElement, 115, 115, false)
                        centerX = 78
                        centerY = 78
                    }
                }

                drawLine = false
            }
            R.id.fragment_draw_shirtgreen -> {
                resetColorFragment()
                binding.fragmentDrawShirtgreen.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtgreen)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 220, 220, false)
                drawLine = false
                centerX = 110
                centerY = 110
            }
            R.id.fragment_draw_shirtred -> {
                resetColorFragment()
                binding.fragmentDrawShirtred.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtred)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 220, 220, false)
                drawLine = false
                centerX = 110
                centerY = 110
            }
            R.id.fragment_draw_shirtyellow -> {
                resetColorFragment()
                binding.fragmentDrawShirtyellow.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtyellow)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 220, 220, false)
                drawLine = false
                centerX = 110
                centerY = 110
            }
            R.id.fragment_draw_shirtviolet -> {
                resetColorFragment()
                binding.fragmentDrawShirtviolet.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtviolet)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 220, 220, false)
                drawLine = false
                centerX = 110
                centerY = 110
            }
            R.id.fragment_draw_shirtorange -> {
                resetColorFragment()
                binding.fragmentDrawShirtorange.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.shirtorange)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 220, 220, false)
                drawLine = false
                centerX = 110
                centerY = 110
            }

            // settings ladder
            R.id.fragment_draw_ladder -> {
                resetColorFragment()
                binding.fragmentDrawLadder.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.ladder)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 130,130, false)
                drawLine = false
                drawLine = false
                centerX = 70
                centerY = 73
            }

            //settings ball
            R.id.fragment_draw_ball -> {
                resetColorFragment()
                binding.fragmentDrawBall.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.ball)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 70,70, false)
                drawLine = false
                centerX = 35
                centerY = 35
            }

            //settings littlehat
            R.id.fragment_draw_littleHat -> {
                resetColorFragment()
                binding.fragmentDrawLittleHat.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.littlehat)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 80, 80, false)
                drawLine = false
                centerX = 35
                centerY = 40
            }

            //settings circle
            R.id.fragment_draw_circle -> {
                resetColorFragment()
                binding.fragmentDrawCircle.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.circle)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 100, 100, false)
                drawLine = false
                centerX = 50
                centerY = 50

            }

            //settings goal
            R.id.fragment_draw_goal -> {
                resetColorFragment()
                binding.fragmentDrawGoal.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.goal)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 220, 220, false)
                drawLine = false
                centerX = 110
                centerY = 110
            }

            //settings flag
            R.id.fragment_draw_flag -> {
                resetColorFragment()
                binding.fragmentDrawFlag.setBackgroundColor(Color.DKGRAY)
                val myDrawable = requireActivity().getDrawable(R.drawable.flag)
                soccerElement = (myDrawable as BitmapDrawable).bitmap
                soccerElement = Bitmap.createScaledBitmap(soccerElement, 130, 130, false)
                drawLine = false
                centerX = 65
                centerY = 65
            }

            R.id.fragment_draw_save_to_gallery -> {
                resetColorFragment()
                binding.fragmentDrawSaveToGallery.setBackgroundColor(Color.DKGRAY)
                saveMediaToStorage(drawBitmap)

                //wait 300 milisec to change color of fragment
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    resetColorFragment()
                }, 300)
            }

        }
        if (this::path.isInitialized) {
            path.reset()
        }
    }


    //reset backgroundcolors of fragments
    private fun resetColorFragment() {
        binding.fragmentDrawPencil.setBackgroundColor(Color.parseColor("#91908E"))
        binding.fragmentDrawDelete.setBackgroundColor(Color.parseColor("#91908E"))
        binding.fragmentDrawRubber.setBackgroundColor(Color.parseColor("#91908E"))
        binding.fragmentDrawSaveToGallery.setBackgroundColor(Color.parseColor("#91908E"))
        binding.fragmentDrawBlack.setBackgroundColor(Color.parseColor("#91908E"))
        binding.fragmentDrawWhite.setBackgroundColor(Color.parseColor("#91908E"))
        binding.fragmentDrawRed.setBackgroundColor(Color.parseColor("#91908E"))
        binding.fragmentDrawBlue.setBackgroundColor(Color.parseColor("#91908E"))
        binding.fragmentDrawYellow.setBackgroundColor(Color.parseColor("#91908E"))
        binding.fragmentDrawOrange.setBackgroundColor(Color.parseColor("#91908E"))

        binding.fragmentDrawLadder.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawBall.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawLittleHat.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawCircle.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawGoal.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawFlag.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawShirtblue.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawShirtgreen.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawShirtred.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawShirtyellow.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawShirtorange.setBackgroundColor(Color.parseColor("#6A6A6A"))
        binding.fragmentDrawShirtviolet.setBackgroundColor(Color.parseColor("#6A6A6A"))
    }


    //draw element into bitmap with x-coordinates and y-coordinates from onTouch()
    private fun drawElement(x: Float, y: Float) {
        var canvas = Canvas(drawBitmap)

        //centerX & centerY for centering the element on coodinations from onTouch
        canvas.drawBitmap(soccerElement, x - centerX, y-centerY, null)

        canvas = surfaceHolder.lockCanvas()
        canvas.drawBitmap(drawBitmap, 0f, 0f, null)
        surfaceHolder.unlockCanvasAndPost(canvas)
    }


    //clear canvas and load into surfaceview/holder -> by deleting
    private fun clearCanvas() {

        val myDrawable = when(page) {
            FIRST_FIELD -> {
                requireActivity().getDrawable(R.drawable.fullfield)
            }
            SECOND_FIELD -> {
                requireActivity().getDrawable(R.drawable.goalarea)
            }
            else -> {
                requireActivity().getDrawable(R.drawable.freearea)
            }
        }

        val back = (myDrawable as BitmapDrawable).bitmap

        val mutableBitmap: Bitmap = back.copy(Bitmap.Config.ARGB_8888, true)

        drawBitmap = Bitmap.createScaledBitmap(mutableBitmap, drawBitmap.width, drawBitmap.height, false)

        var canvas = Canvas(drawBitmap)
        canvas = surfaceHolder.lockCanvas()
        canvas.drawBitmap(drawBitmap, 0f, 0f, null)
        surfaceHolder.unlockCanvasAndPost(canvas)

        soccerElement = Bitmap.createBitmap(drawBitmap.width, drawBitmap.height, Bitmap.Config.ARGB_8888)

        project?.hasEdited = false
    }

    //convert bitmap to picture and store into gallery
    private fun saveMediaToStorage(bitmap: Bitmap) {
        //Generating a file name
        val filename = "${project?.projectName + "_" + page}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        //getting the contentResolver
        context?.contentResolver?.also { resolver ->

        //Content resolver will process the contentvalues
        val contentValues = ContentValues().apply {

            //putting file information in content values
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        //Inserting the contentValues to contentResolver and getting the Uri
        val imageUri: Uri? =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        project?.let {
            it.hasEdited = true

            when(page) {
                FIRST_FIELD -> {
                    it.pathFirstField = imageUri.toString()
                }
                SECOND_FIELD -> {
                    it.pathPenaltyArea = imageUri.toString()
                }
                THIRD_FIELD -> {
                    it.pathFreeArea = imageUri.toString()
                }
            }

            GlobalScope.launch {
                projectViewModel?.update(it)
            }
        }

            //Opening an outputstream with the Uri that we got
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(requireContext(), "Picture saved to Gallery", Toast.LENGTH_LONG).show()

        }
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