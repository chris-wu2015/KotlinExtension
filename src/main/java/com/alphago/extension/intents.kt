package com.alphago.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import java.io.Serializable
import java.lang.IllegalArgumentException

/**
 * @Author:Wzb
 * @Date:2017-03-13 16:23
 * @Desc:
 */
inline fun <reified T : Activity> Context.startActivity(vararg pair: Pair<String, Any>,
                                                        finish: Boolean = false,
                                                        anim: Boolean = true,
                                                        enterAnim: Int = 0,
                                                        exitAnim: Int = 0,
                                                        bundle: Bundle? = null,
                                                        needResult: Boolean = false,
                                                        reqCode: Int = 0) {
    val intent = Intent(this, T::class.java)
    putDataToIntent(intent, pair = *pair, bundle = bundle)
    if (this is Activity) {
        if (needResult) {
            startActivityForResult(intent, reqCode)
        } else {
            startActivity(intent)
        }
        if (finish && isFinishing.not()) onBackPressed()
    } else {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    if (this is Activity && anim && enterAnim != 0 && exitAnim != 0) {
        overridePendingTransition(enterAnim, exitAnim)
    }
}

inline fun <reified T : Activity> Activity.startActivityAnim(vararg pair: Pair<String, Any>,
                                                             enterAnimRes: Int = 0,
                                                             exitAnimRes: Int = 0,
                                                             finish: Boolean = false,
                                                             bundle: Bundle? = null,
                                                             needResult: Boolean = false,
                                                             reqCode: Int = 0) {
    val intent = Intent(this, T::class.java)
    putDataToIntent(intent, pair = *pair, bundle = bundle)
    val optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this,
            enterAnimRes, exitAnimRes).toBundle()

    if (needResult) {
        ActivityCompat.startActivityForResult(this, intent, reqCode, optionsCompat)
    } else {
        ActivityCompat.startActivity(this, intent, optionsCompat)
    }
    if (finish && isFinishing.not()) onBackPressed()
}

inline fun <reified T : Activity> Fragment.startActivity(vararg pair: Pair<String, Any>,
                                                         finish: Boolean = false,
                                                         anim: Boolean = true,
                                                         bundle: Bundle? = null) {
    activity.startActivity<T>(finish = finish, pair = *pair, anim = anim, bundle = bundle)
}

inline fun <reified T : Activity> Activity.startActivityAndFinish(vararg pair: Pair<String, Any>) {
    startActivity<T>(pair = *pair, finish = true)
}

inline fun <reified T : Activity> Fragment.startActivityAndFinish(vararg pair: Pair<String, Any>) {
    startActivity<T>(pair = *pair, finish = true)
}

inline fun <reified T : Activity> Activity.startActivityForResult(reqCode: Int,
                                                                  vararg pair: Pair<String, Any>) {
    val intent = Intent(this, T::class.java)
    putDataToIntent(intent, pair = *pair)
    startActivityForResult(intent, reqCode)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(reqCode: Int = 0,
                                                                  vararg pair: Pair<String, Any>) {
    val intent = Intent(this.activity, T::class.java)
    putDataToIntent(intent, pair = *pair)
    startActivityForResult(intent, reqCode)
}

fun putDataToIntent(intent: Intent, vararg pair: Pair<String, Any>, bundle: Bundle? = null) {
    pair.forEach {
        val value = it.second
        when (value) {
            is Short -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Serializable -> intent.putExtra(it.first, value)
            else ->
                throw IllegalArgumentException("the second value of pair must be one of eight " +
                        "base data type or parcelable or serializable")
        }
    }
    if (bundle != null) {
        intent.putExtras(bundle)
    }
}

/**
 * 跳转拨号界面并指定号码
 *@author Chris
 *created at 2016/12/20 020
 */
fun Context.dial(phoneNumber: String) {
    val phoneIntent = Intent(Intent.ACTION_DIAL,
            Uri.parse("tel:$phoneNumber"))
    if (this !is Activity) {
        phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(phoneIntent)
}

fun Context.dial(resId: Int) {
    if (resId > 0) {
        dial(getString(resId))
    }
}

/**
 * 跳转应用市场详情页
 */
fun Context.openMarket(): Boolean {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val handler = packageManager.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS)
    if (handler?.size ?: 0 > 0) {
        startActivity(intent)
        return true
    } else return false
}