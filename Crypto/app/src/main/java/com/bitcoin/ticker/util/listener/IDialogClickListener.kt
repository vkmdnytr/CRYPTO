package com.bitcoin.ticker.util.listener

import android.app.Dialog

interface OnClickOkButtonListener {
    fun onClickOkButton(dialog:Dialog)
}
interface OnClickCancelButtonListener {
    fun onClickCancelButton(dialog:Dialog)
}