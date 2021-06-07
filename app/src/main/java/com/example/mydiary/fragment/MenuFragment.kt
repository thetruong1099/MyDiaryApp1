package com.example.mydiary.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mydiary.R
import com.example.mydiary.model.Diary
import com.example.mydiary.util.CustomBackStackFragment
import com.example.mydiary.viewmodel.DiaryViewModel
import com.example.mydiary.viewmodel.FileViewModel
import com.example.mydiary.viewmodel.SharedPreferenceViewModel
import kotlinx.android.synthetic.main.fragment_menu.*
import java.io.File


class MenuFragment : CustomBackStackFragment() {

    private val sharePreferenceViewModel by lazy {
        ViewModelProvider(
            this,
            SharedPreferenceViewModel.SharePreferenceViewModelFactory(requireContext())
        )[SharedPreferenceViewModel::class.java]
    }

    private val diaryViewModel: DiaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiaryViewModel.DiaryViewModelFactory(requireActivity().application)
        )[DiaryViewModel::class.java]
    }

    private val fileViewModel by lazy {
        ViewModelProvider(
            this,
            FileViewModel.FileViewModelFactory(requireContext())
        )[FileViewModel::class.java]
    }

    private var list = listOf<String>()

    private val requestPermissionResultWriteFile = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) exportCSV()
    }

    private val requestPermissionResultReadFile = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) importCSV()
    }

    private val resultLauncherWrite = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        diaryViewModel.getAllDiary().observe(this, Observer {
            if (it.size == 0) {
                showDialogCheckPass("Không có dữ liệu để backup")
            } else if (result?.resultCode == Activity.RESULT_OK && result.data!!.data != null) {
                fileViewModel.writeDataToFile(result.data!!.data!!, it)
            }
        })
    }

    private val resultLauncherRead = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result?.resultCode == Activity.RESULT_OK) {
            list = fileViewModel.readDataFromFile(result.data!!.data!!)
            importDatabase(list)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onStart() {
        super.onStart()

        val navController = findNavController()
        btn_back.setOnClickListener { navController.popBackStack() }

        layout_day_start.setOnClickListener {
            val dayStartOfWeekFragment = DayStartOfWeekFragment()
            dayStartOfWeekFragment.show(childFragmentManager, "TAG")
        }

        sharePreferenceViewModel.getDayStart().observe(this, Observer {
            if (it == "") tv_set_day_start.text = "Sunday"
            else tv_set_day_start.text = it
        })

        btn_change_password.setOnClickListener {
            val changePasswordFragment = ChangePasswordFragment()
            changePasswordFragment.show(childFragmentManager, "TAG")
        }

        btn_backup.setOnClickListener {
            exportCSV()
//            requestPermissionWriteFile()
        }

        btn_restore.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Bạn có chắc chắn muốn restore")
                .setPositiveButton("Yes") { dialogInterface, which ->
                    importCSV()
//                    requestPermissionReadFile()
                }
                .setNegativeButton("No") { dialogInterface, which ->
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun exportCSV() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/*"
            putExtra(Intent.EXTRA_TITLE, "data.csv")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        resultLauncherWrite.launch(intent)
    }

    private fun requestPermissionWriteFile() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            importCSV()
            return
        }
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (result == PackageManager.PERMISSION_GRANTED) {
            exportCSV()
        } else {
            requestPermissionResultWriteFile.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun requestPermissionReadFile() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            importCSV()
            return
        }
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (result == PackageManager.PERMISSION_GRANTED) {
            importCSV()
        } else {
            requestPermissionResultReadFile.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun importCSV() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/*"
        resultLauncherRead.launch(intent)
    }

    private fun importDatabase(listData: List<String>) {
        diaryViewModel.deleteAllDiary()
        for (i in 1 until listData.size) {
            val listString = listData[i].split(",")
            diaryViewModel.insertDiary(
                Diary(
                    listString[0].toInt(),
                    listString[1].toInt(),
                    listString[2].toInt(),
                    listString[3],
                    listString[4],
                    listString[5]
                )
            )
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