package com.nammaplatform

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Utility to programmatically build the coach layout strip.
 * Each coach is a colored box with a Kannada label.
 */
object CoachViewHelper {

    data class CoachColors(
        val background: Int,
        val text: Int = Color.WHITE
    )

    fun getCoachColors(type: String): CoachColors {
        return when (type) {
            "ENGINE" -> CoachColors(Color.parseColor("#B71C1C"))
            "GENERAL" -> CoachColors(Color.parseColor("#1B5E20"))
            "LADIES" -> CoachColors(Color.parseColor("#880E4F"))
            "SLEEPER" -> CoachColors(Color.parseColor("#0D47A1"))
            "AC" -> CoachColors(Color.parseColor("#006064"))
            "GUARD" -> CoachColors(Color.parseColor("#4E342E"))
            else -> CoachColors(Color.GRAY)
        }
    }

    /**
     * Creates a single coach view (box + label).
     * @param compact If true, renders smaller boxes for the list-item preview.
     */
    fun createCoachView(context: Context, coach: Coach, compact: Boolean = false): LinearLayout {
        val dp = context.resources.displayMetrics.density

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            val margin = (4 * dp).toInt()
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(margin, 0, margin, 0)
            layoutParams = params
        }

        val colors = getCoachColors(coach.type)
        val boxWidth = if (compact) (44 * dp).toInt() else (56 * dp).toInt()
        val boxHeight = if (compact) (36 * dp).toInt() else (52 * dp).toInt()

        // Coach box
        val box = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(colors.background)
            layoutParams = LinearLayout.LayoutParams(boxWidth, boxHeight)
        }

        // Kannada label inside box
        val labelView = TextView(context).apply {
            text = coach.labelKn
            setTextColor(colors.text)
            textSize = if (compact) 9f else 11f
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }
        box.addView(labelView)

        // English label below box
        val subLabel = TextView(context).apply {
            text = coach.label
            setTextColor(Color.parseColor("#666666"))
            textSize = 9f
            gravity = Gravity.CENTER
            val p = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            p.topMargin = (2 * dp).toInt()
            layoutParams = p
        }

        container.addView(box)
        if (!compact) container.addView(subLabel)

        return container
    }

    /**
     * Adds all coach views to the given LinearLayout container.
     */
    fun populateCoachLayout(
        context: Context,
        container: LinearLayout,
        coaches: List<Coach>,
        compact: Boolean = false
    ) {
        container.removeAllViews()
        coaches.forEach { coach ->
            container.addView(createCoachView(context, coach, compact))
        }
    }
}
