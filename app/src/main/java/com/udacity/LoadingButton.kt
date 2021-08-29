package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonBackgroundColor = 0
    private var buttonDownloadingBackgroundColor = 0
    private var circleColor = 0
    private var progress = 0
    private var displayedText = resources.getString(R.string.download);
    private var downloadDisplayText = resources.getString(R.string.download);
    private var loadingDisplayedText = resources.getString(R.string.button_loading);

    private var buttonTextColor = Color.WHITE

    private var progressArc = RectF()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var valueAnimator = ValueAnimator()


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                valueAnimator = ValueAnimator.ofInt(0, 1000).apply {
                    addUpdateListener {
                        progress = animatedValue as Int
                        invalidate()
                    }
                    duration = 1000
                    doOnStart {
                        displayedText = loadingDisplayedText
                        isEnabled = false
                    }
                    doOnEnd {
                        progress = 0
                        isEnabled = true
                        displayedText = downloadDisplayText
                    }
                    start()
                }

            }
            ButtonState.Clicked -> {
                buttonState = ButtonState.Loading
                isEnabled = false
            }
            ButtonState.Completed -> {
                isEnabled = true
                displayedText = downloadDisplayText
            }
        }
        invalidate()
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonBackgroundColor = getColor(
                R.styleable.LoadingButton_backgroundColor,
                Color.parseColor("#FF07C2AA")
            )
            buttonDownloadingBackgroundColor = getColor(
                R.styleable.LoadingButton_downloadingBackgroundColor,
                Color.parseColor("#FF004349")
            )
            circleColor =
                getColor(R.styleable.LoadingButton_circleColor, Color.parseColor("#FFF9A825"))
            buttonTextColor =
                getColor(R.styleable.LoadingButton_textColor, Color.parseColor("#FFFFFFFF"))

        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rectArea = Rect()
        paint.color = buttonBackgroundColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
        // draw download progress
        if (buttonState == ButtonState.Loading) {
            paint.color = buttonDownloadingBackgroundColor
            val progressRect = progress / 1000f * widthSize
            canvas.drawRect(0f, 0f, progressRect, heightSize.toFloat(), paint)
            val sweepAngle = progress / 1000f * 360f
            paint.color = circleColor
            canvas.drawArc(progressArc, 0f, sweepAngle, true, paint)
        }
        paint.color = buttonTextColor
        paint.getTextBounds(displayedText, 0, displayedText.length, rectArea)
        val centerButton = measuredHeight.toFloat() / 2 - rectArea.centerY()
        canvas.drawText(displayedText, widthSize / 2f, centerButton, paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
        progressArc = RectF(
            widthSize - 250f,
            heightSize / 2 - 25f,
            widthSize.toFloat() - 200f,
            heightSize / 2 + 25f
        )
    }

    fun downloadStartLoading() {
        buttonState = ButtonState.Loading
    }
}