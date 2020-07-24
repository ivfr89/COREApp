package com.developer.ivan.coreapp.ui.custom

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView

class MaterialHTMLTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : MaterialTextView(context, attrs, defStyle) {


    var htmlText: String? = ""
        set(value) = value?.let { valueNoNullable ->
            text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(valueNoNullable, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(valueNoNullable)
            }
        } ?: kotlin.run { }


}