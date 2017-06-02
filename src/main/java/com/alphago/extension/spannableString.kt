package com.alphago.extension

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.TextView

/**
 * @Author:Wzb
 * @Date:2017-03-14 19:03
 * @Desc:
 */
fun <S> S.setForegroundColor(color: Int, start: Int = 0, end: Int = this.length,
                             flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        : S where S : Spannable
        = this.apply {
    setSpan(ForegroundColorSpan(color), start, end,
            flags)
}

fun <S : Spannable> S.setSizeSpan(size: Int, start: Int = 0, end: Int = this.length,
                                  dp: Boolean = true,
                                  flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE): S
        = this.apply {
    setSpan(AbsoluteSizeSpan(size, dp), start, end,
            flags)
}

inline fun <S : Spannable> S.setClickSpan(view: TextView, underline: Boolean = false,
                                          start: Int = 0, end: Int = this.length,
                                          crossinline click: (View) -> Unit): S {
    setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            click(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            if (underline) {
                super.updateDrawState(ds)
            } else {
                ds.isUnderlineText = false
            }
        }
    }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    view.movementMethod = LinkMovementMethod.getInstance()
    return this
}

fun <S : Spannable> S.setTextAppearanceSpan(context: Context, resId: Int,
                                        start: Int = 0, end: Int = length,
                                        flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE): S {
    setSpan(TextAppearanceSpan(context, resId), start, end, flags)
    return this
}

fun SpannableStringBuilder.appendColorSpan(append: String, color: Int)
        : SpannableStringBuilder
        = if (Build.VERSION.SDK_INT >= 21) {
    append(append, ForegroundColorSpan(color), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
} else {
    val start = length
    append(append)
            .setForegroundColor(color, start)
}

fun SpannableStringBuilder.appendColorSpanRes(context: Context, append: String, resId: Int)
        : SpannableStringBuilder
        = if (Build.VERSION.SDK_INT >= 21) {
    append(append, ForegroundColorSpan(ContextCompat.getColor(context, resId)),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
} else {
    val start = length
    append(append)
            .setForegroundColor(ContextCompat.getColor(context, resId), start)
}

fun SpannableStringBuilder.appendColorSpanRes(context: Context, append: Int, resId: Int)
        : SpannableStringBuilder
        = if (Build.VERSION.SDK_INT >= 21) {
    append(context.getString(append), ForegroundColorSpan(ContextCompat.getColor(context, resId)),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
} else {
    val start = length
    append(context.getString(append))
            .setForegroundColor(ContextCompat.getColor(context, resId), start)
}

fun SpannableStringBuilder.appendSizeSpan(append: String, size: Int, dp: Boolean = true)
        : SpannableStringBuilder
        = if (Build.VERSION.SDK_INT >= 21) {
    append(append, AbsoluteSizeSpan(size, dp), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
} else {
    val start = length
    append(append)
            .setSizeSpan(size, start, dp = dp)
}

fun SpannableStringBuilder.appendSizeSpanRes(context: Context, resStringId: Int, size: Int,
                                             dp: Boolean = true)
        : SpannableStringBuilder
        = if (Build.VERSION.SDK_INT >= 21) {
    append(context.getString(resStringId), AbsoluteSizeSpan(size, dp),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
} else {
    val start = length
    append(context.getString(resStringId))
            .setSizeSpan(size, start, dp = dp)
}

fun CharSequence.setForegroundColorSpan(color: Int, start: Int = 0, end: Int = this.length)
        = SpannableString(this)
        .setForegroundColor(color, start, end)

fun CharSequence.setForegroundColorSpanBuilder(color: Int, start: Int = 0, end: Int = this.length)
        = SpannableStringBuilder(this)
        .setForegroundColor(color, start, end)

fun CharSequence.setForegroundColorResSpan(context: Context, resId: Int,
                                           start: Int = 0, end: Int = this.length)
        = SpannableString(this)
        .setForegroundColor(ContextCompat.getColor(context, resId), start, end)

fun CharSequence.setForegroundColorResSpanBuilder(context: Context, resId: Int,
                                                  start: Int = 0, end: Int = this.length)
        = SpannableStringBuilder(this)
        .setForegroundColor(ContextCompat.getColor(context, resId), start, end)

fun CharSequence.setSizeSpan(size: Int, start: Int = 0, end: Int = this.length, dp: Boolean = true)
        = SpannableString(this)
        .setSizeSpan(size, start, end, dp)

fun CharSequence.setSizeSpanBuilder(size: Int, start: Int = 0, end: Int = this.length,
                                    dp: Boolean = true)
        = SpannableStringBuilder(this)
        .setSizeSpan(size, start, end, dp)

fun CharSequence.setStyleSpanBuilder(context: Context,resId: Int,
                                     start: Int = 0, end: Int = this.length,
                                     dp: Boolean = true)
        = SpannableStringBuilder(this)
        .setTextAppearanceSpan(context, resId, start, end)

fun CharSequence.setStyleSpan(context: Context,resId: Int,
                              start: Int = 0, end: Int = this.length,
                              dp: Boolean = true)
        = SpannableString(this)
        .setTextAppearanceSpan(context, resId, start, end)