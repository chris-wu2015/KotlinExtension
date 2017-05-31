package com.alphago.extension

import android.app.Activity
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.KeyEvent
import android.view.View
import android.widget.ListAdapter

/**
 * @author Chris
 * @Desc AlertDialogCompat
 * @Date 2017/1/10 010
 */
open class DialogCompatBuilder(val ctx: Activity) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
    protected var dialog: AlertDialog? = null

    fun dismiss() {
        dialog?.dismiss()
    }

    fun show(): AlertDialog {
        val alertDialog = builder.create()
        alertDialog.show()
        dialog = alertDialog
        return alertDialog
    }

    fun title(title: CharSequence): DialogCompatBuilder {
        builder.setTitle(title)
        return this
    }

    fun title(resource: Int): DialogCompatBuilder {
        builder.setTitle(resource)
        return this
    }

    fun message(title: CharSequence): DialogCompatBuilder {
        builder.setMessage(title)
        return this
    }

    fun message(resource: Int): DialogCompatBuilder {
        builder.setMessage(resource)
        return this
    }

    fun icon(icon: Int): DialogCompatBuilder {
        builder.setIcon(icon)
        return this
    }

    fun icon(icon: Drawable): DialogCompatBuilder {
        builder.setIcon(icon)
        return this
    }

    fun customTitle(title: View): DialogCompatBuilder {
        builder.setCustomTitle(title)
        return this
    }

    fun customView(view: View, initEvent: (View.(dialog: DialogInterface) -> Unit)? = null)
            : DialogCompatBuilder {
        builder.setView(view)
        initEvent?.invoke(view, dialog!!)
        return this
    }

    fun cancellable(value: Boolean = true): DialogCompatBuilder {
        builder.setCancelable(value)
        return this
    }

    fun onCancel(f: () -> Unit): DialogCompatBuilder {
        builder.setOnCancelListener { f() }
        return this
    }

    fun onKey(f: (keyCode: Int, e: KeyEvent) -> Boolean): DialogCompatBuilder {
        builder.setOnKeyListener({ dialog, keyCode, event -> f(keyCode, event) })
        return this
    }

    fun neutralButton(textResource: Int = android.R.string.ok,
                      f: DialogInterface.() -> Unit = { dismiss() }): DialogCompatBuilder {
        neutralButton(ctx.getString(textResource), f)
        return this
    }

    fun neutralButton(title: String, f: DialogInterface.() -> Unit = { dismiss() })
            : DialogCompatBuilder {
        builder.setNeutralButton(title, { dialog, which -> dialog.f() })
        return this
    }

    fun positiveButton(textResource: Int = android.R.string.ok, f: DialogInterface.() -> Unit)
            : DialogCompatBuilder {
        positiveButton(ctx.getString(textResource), f)
        return this
    }

    fun positiveButton(title: String, f: DialogInterface.() -> Unit): DialogCompatBuilder {
        builder.setPositiveButton(title, { dialog, which -> dialog.f() })
        return this
    }

    fun negativeButton(textResource: Int = android.R.string.cancel,
                       f: DialogInterface.() -> Unit = { dismiss() }): DialogCompatBuilder {
        negativeButton(ctx.getString(textResource), f)
        return this
    }

    fun negativeButton(title: String, f: DialogInterface.() -> Unit = { dismiss() })
            : DialogCompatBuilder {
        builder.setNegativeButton(title, { dialog, which -> dialog.f() })
        return this
    }

    fun items(itemsId: Int, f: (which: Int) -> Unit): DialogCompatBuilder {
        items(ctx.resources!!.getTextArray(itemsId), f)
        return this
    }

    fun items(items: List<CharSequence>, f: (which: Int) -> Unit): DialogCompatBuilder {
        items(items.toTypedArray(), f)
        return this
    }

    fun items(items: Array<CharSequence>, f: (which: Int) -> Unit): DialogCompatBuilder {
        builder.setItems(items, { dialog, which -> f(which) })
        return this
    }

    fun adapter(adapter: ListAdapter, f: (which: Int) -> Unit): DialogCompatBuilder {
        builder.setAdapter(adapter, { dialog, which -> f(which) })
        return this
    }

    fun adapter(cursor: Cursor, labelColumn: String, f: (which: Int) -> Unit)
            : DialogCompatBuilder {
        builder.setCursor(cursor, { dialog, which -> f(which) }, labelColumn)
        return this
    }
}

fun Fragment.dialog(): DialogCompatBuilder = activity.dialog()


fun Fragment.dialog(init: DialogCompatBuilder.() -> DialogCompatBuilder):
        DialogCompatBuilder {
    return activity.dialog(init)
}

fun android.app.Fragment.dialog() = activity.dialog()

fun android.app.Fragment.dialog(init: DialogCompatBuilder.() -> DialogCompatBuilder):
        DialogCompatBuilder {
    return activity.dialog(init)
}

fun Activity.dialog(): DialogCompatBuilder = DialogCompatBuilder(this)

fun Activity.dialog(init: DialogCompatBuilder.() -> DialogCompatBuilder):
        DialogCompatBuilder {
    return DialogCompatBuilder(this).init()
}
