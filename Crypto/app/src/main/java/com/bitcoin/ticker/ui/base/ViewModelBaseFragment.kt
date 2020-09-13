package com.bitcoin.ticker.ui.base


import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.bitcoin.ticker.R
import com.bitcoin.ticker.util.listener.OnClickOkButtonListener

abstract class ViewModelBaseFragment : Fragment() {

    abstract fun setObserveViewModel()
    fun showGeneralErrorDialog(text:String?=null,isCancelable:Boolean=false,onOkClickListener: OnClickOkButtonListener?=null)= MaterialDialog(requireActivity())
            .show {
                title(text = requireActivity().getString(R.string.dialog_title_mistake))
                message(text = text?:requireActivity().getString(R.string.dialog_message_mistake))
                cancelable(isCancelable)
                positiveButton(R.string.ok)
                positiveButton {
                    if(onOkClickListener==null){
                        dismiss()
                    }else  {
                        onOkClickListener.onClickOkButton(this)
                    }

                }
            }
    fun showGeneralSuccessDialog(text:String?=null,isCancelable:Boolean=false, onOkClickListener: OnClickOkButtonListener?=null) = MaterialDialog(requireActivity())
            .show {
                title(text = requireActivity().getString(R.string.dialog_title_success))
                message(text = text?:requireActivity().getString(R.string.dialog_message_success_process))
                cancelable(isCancelable)
                positiveButton(R.string.ok)
                positiveButton {
                    if(onOkClickListener==null){
                        dismiss()
                    }else  {
                        onOkClickListener.onClickOkButton(this)
                    }

                }
            }


}