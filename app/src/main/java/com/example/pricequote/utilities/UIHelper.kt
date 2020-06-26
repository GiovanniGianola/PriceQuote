package com.example.pricequote.utilities

import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.example.pricequote.R

class UIHelper {
    companion object {
        fun customButton(activity: FragmentActivity, btnId: Int, btnText: String?, params: ViewGroup.LayoutParams): Button {
            val btn = Button(activity)
            btn.run {
                id = btnId
                text = btnText
                width = 0
                height = 150
                textSize = 15F
                layoutParams = params
                setBackgroundResource(R.drawable.btn_space_released)
                setTextColor(resources.getColor(R.color.colorPrimary, activity.theme))
            }
            return btn
        }

        fun setFocusButtonsGroup(
            activity: FragmentActivity,
            btnUnfocused: Button?,
            btnFocus: Button?
        ): Button? {

            btnUnfocused?.setTextColor(activity.baseContext.resources.getColor(R.color.colorPrimary, activity.theme))
            btnUnfocused?.setBackgroundResource(R.drawable.btn_space_released)

            btnFocus?.setTextColor(activity.baseContext.resources.getColor(R.color.white, activity.theme))
            btnFocus?.setBackgroundResource(R.drawable.btn_space_pressed)

            return btnFocus
        }
    }
}
