package com.alphago.extension

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.widget.TextViewCompat
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

/**
 * @Author:Wzb
 * @Date:2017-03-03 18:19
 * @Desc:
 */
fun Toast.show(message: String) {
    setText(message)
    duration = Toast.LENGTH_SHORT
    show()
}

fun Toast.show(message: Int) {
    show(view.context.getString(message))
}

@Suppress("UNCHECKED_CAST")
fun <S, R> S.support(versionNO: Int, support: S.(S) -> R, nonsupport: S.(S) -> R = { Unit as R }): R {
    return if (Build.VERSION.SDK_INT >= versionNO) this.support(this) else this.nonsupport(this)
}

@Suppress("UNCHECKED_CAST")
fun <S, R> S.support(versionNO: Int, support: S.(S) -> R): R {
    return support(versionNO, support, { Unit as R })
}

/** show/hide status bar must call before setContentView,otherwise it's not working
 * */
fun Activity.fullscreen(fullscreen: Boolean = true, showStatusBar: Boolean = false) {
    if (fullscreen) {
        //全屏显示
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (showStatusBar) {
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}

fun Fragment.fullscreen(fullscreen: Boolean = true, showStatusBar: Boolean = false) {
    activity.fullscreen(fullscreen, showStatusBar)
}

/**
 * id:资源id，args:字符串资源需要进行格式化时传入
 *@author Chris
 *created at 2017/2/9 009
 */
fun TextView.text(id: Int = -1, vararg args: Any) {
    if (id > 0) {
        if (args.isNotEmpty()) {
            text = context.getString(id, *args)
        } else {
            text = context.getString(id)
        }
    }
}

fun EditText.text(id: Int = -1, vararg args: Any) {
    if (id > 0) {
        if (args.isNotEmpty()) {
            setText(context.getString(id, *args))
        } else {
            setText(context.getString(id))
        }
    }
}

/**
 * 代码设置textView的textAppearance
 *@author Chris
 *created at 2017/2/9 009
 */
fun TextView.textAppearance(resId: Int = -1) {
    if (resId != -1) TextViewCompat.setTextAppearance(this, resId)
}

/**
 * 设置view可见，如果设置返回true，不需要设置返回false-->本身就是可见的
 *@author Chris
 *created at 2016/10/24 024
 */
fun View.visible(): Boolean {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
        return true
    } else {
        return false
    }
}

/**
 * 设置view不可见，如果设置返回true，不需要设置返回false-->本身就是不可见的
 *@author Chris
 *created at 2016/10/24 024
 */
fun View.invisible(): Boolean {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
        return true
    } else {
        return false
    }
}

/**
 * 设置view不可见，如果设置返回true，不需要设置返回false-->本身就是不可见的
 *@author Chris
 *created at 2016/10/24 024
 */
fun View.gone(): Boolean {
    if (visibility != View.GONE) {
        visibility = View.GONE
        return true
    } else {
        return false
    }
}

fun Context.checkNetwork(): Boolean {
    val conn: ConnectivityManager
            = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = conn.activeNetworkInfo
    if (info?.isAvailable != true) return false
    else {
        when (info.type) {
            ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE ->
                return info.isConnected
            else -> return false
        }
    }
}

fun Context.keyboardToggle(view: View, show: Boolean = false) {
    val inputManager
            = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (show) {
        view.requestFocus()
        inputManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    } else {
        view.clearFocus()
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun View.keyboardToggle(show: Boolean = false, view: View? = null) {
    context.keyboardToggle(view ?: this, show)
}

fun View.index(): Int {
    val parentView = parent
    if (parentView is ViewGroup) {
        return parentView.indexOfChild(this)
    } else {
        return 0
    }
}