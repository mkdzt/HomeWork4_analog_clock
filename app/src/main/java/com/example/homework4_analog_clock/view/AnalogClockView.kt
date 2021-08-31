package com.example.homework4_analog_clock.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.homework4_analog_clock.R
import java.util.*

class AnalogClockView(context: Context, attrs: AttributeSet?) :
    @JvmOverloads View(context, attrs) {

    private var centerClockX = 0f
    private var centerClockY = 0f
    private var radiusClock = 0f

    private var hourHandColor:Int
    private var minuteHandColor:Int
    private var secondHandColor:Int

    private var hourHandLength:Float
    private var minuteHandLength:Float
    private var secondHandLength:Float

    private var paint = Paint()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.AnalogClockView).apply {
            hourHandColor = getInt(R.styleable.AnalogClockView_hourHandColor, R.color.black)
            minuteHandColor = getInt(R.styleable.AnalogClockView_minuteHandColor, R.color.black)
            secondHandColor = getInt(R.styleable.AnalogClockView_secondHandColor, R.color.black)

            hourHandLength = getFloat(R.styleable.AnalogClockView_hourHandLength, 200f)
            minuteHandLength = getFloat(R.styleable.AnalogClockView_minuteHandLength, 350f)
            secondHandLength = getFloat(R.styleable.AnalogClockView_secondHandLength, 350f)

            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        centerClockX = (measuredWidth / 2).toFloat()
        centerClockY = (measuredHeight / 2).toFloat()
        radiusClock = minOf(centerClockX, centerClockY) - 50
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawCenterPoint(canvas)
            drawCircle(canvas)
            drawBlockLines(canvas)
            drawHands(canvas)
        }
        postInvalidateDelayed(500)
    }

    private fun drawCircle(canvas: Canvas?){
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE,8f)
        canvas?.drawCircle(centerClockX, centerClockY, radiusClock, paint)
    }

    private fun drawHands(canvas: Canvas?){
        val calendar = Calendar.getInstance()

        val second = calendar.get(Calendar.SECOND).toFloat()
        val minute = calendar.get(Calendar.MINUTE).toFloat()
        val hour = calendar.get(Calendar.HOUR) + minute / 60.0f
        setPaintAttributes(hourHandColor, Paint.Style.FILL,20f)
        drawHand(canvas,hour, hourHandLength)

        setPaintAttributes(minuteHandColor, Paint.Style.FILL,15f)
        drawHand(canvas, minute, minuteHandLength)

        setPaintAttributes(secondHandColor, Paint.Style.FILL,10f)
        drawHand(canvas, second, secondHandLength)
    }

    private fun drawHand(canvas: Canvas?, time:Float, handLength:Float){
        val angle = Math.PI * time / 30 - Math.PI / 2
        canvas?.drawLine(
            centerClockX,
            centerClockY,
            (centerClockX + kotlin.math.cos(angle) * handLength).toFloat(),
            (centerClockY + kotlin.math.sin(angle) * handLength).toFloat(),
            paint
        )
    }

    private fun setPaintAttributes(color: Int, style:Paint.Style,strokeWidth:Float){
        paint.reset()
        paint.apply {
            isAntiAlias = true
            this.color = color
            this.strokeWidth = strokeWidth
            this.style = style
        }
    }

    private fun drawCenterPoint(canvas: Canvas?) {
        canvas?.apply {
            paint.color = Color.BLACK
            paint.style = Paint.Style.FILL_AND_STROKE
            drawCircle( centerClockX, centerClockY, 8f, paint)
        }
    }

    private fun drawBlockLines(canvas: Canvas?){
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 20f)
        for (i in 1..12){
            val angle = Math.PI * (i) / 6
            canvas?.drawLine(
                (centerClockX + kotlin.math.cos(angle) * radiusClock).toFloat(),
                (centerClockY + kotlin.math.sin(angle) * radiusClock).toFloat(),
                (centerClockX + kotlin.math.cos(angle) * (radiusClock - radiusClock / 8)).toFloat(),
                (centerClockY + kotlin.math.sin(angle) * (radiusClock - radiusClock / 8)).toFloat(),
                paint )
        }
    }
}