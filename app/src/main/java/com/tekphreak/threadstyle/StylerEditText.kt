package com.tekphreak.threadstyle

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class StylerEditText(ctx: Context, attrs: AttributeSet?) : AppCompatEditText(ctx, attrs) {
    var savedStart = 0
    var savedEnd = 0

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        savedStart = selStart
        savedEnd = selEnd
    }
}
