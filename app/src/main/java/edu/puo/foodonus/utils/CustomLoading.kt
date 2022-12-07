package edu.puo.foodonus.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import edu.puo.foodonus.R

object CustomLoading  {
    fun showLoading(activity: Activity) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
    fun stopDialog(activity: Activity?) {
        val dialog = Dialog(activity!!)
        dialog.dismiss()
    }
}