package pl.polsl.tm

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner

@SuppressLint("AppCompatCustomView")
class BetterSpinner: Spinner {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
    }

    override fun setSelection(position: Int, animate: Boolean) {
        val sameSelect= position == selectedItemPosition
        super.setSelection(position, animate)
        if (sameSelect) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            onItemSelectedListener!!.onItemSelected(
                this,
                selectedView,
                position,
                selectedItemId
            )
        }
    }

    override fun setSelection(position: Int) {
        val sameSelect = position == selectedItemPosition
        super.setSelection(position)
        if (sameSelect) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            onItemSelectedListener!!.onItemSelected(
                this,
                selectedView,
                position,
                selectedItemId
            )
        }
    }
}