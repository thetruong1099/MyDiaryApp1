package com.example.mydiary.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mydiary.R
import com.example.mydiary.viewmodel.SharedPreferenceViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_change_password.*


class ChangePasswordFragment : BottomSheetDialogFragment() {

    private val sharePreferenceViewModel by lazy {
        ViewModelProvider(
            this,
            SharedPreferenceViewModel.SharePreferenceViewModelFactory(requireContext())
        )[SharedPreferenceViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onStart() {
        super.onStart()

        btn_change_password.setOnClickListener {
            var password = edt_password.text.toString()

            if (password.length < 6) {
                showDialogCheckPass("Mật khẩu có ít nhất 6 ký tự")
            } else {
                sharePreferenceViewModel.setPassWord(password)
            }

            dismiss()
        }
    }

    private fun showDialogCheckPass(string: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(string)
            .setPositiveButton("Yes") { dialogInterface, which -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}