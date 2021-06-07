package com.example.mydiary.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result?.resultCode == Activity.RESULT_OK) {
            list = fileViewModel.readDataFromFile(result.data!!.data!!)
            importCSV(list)
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

        val navControler = findNavController()
        btn_back.setOnClickListener { navControler.popBackStack() }

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

        btn_backup.setOnClickListener { exportCSV() }

        btn_restore.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Bạn có chắc chắn muốn restore")
                .setPositiveButton("Yes") { dialogInterface, which ->
                    var path = requireContext().getFilesDir().getAbsolutePath() + "/data.csv";
                    var cheackFileExist: Boolean = File(path).exists()
                    if (cheackFileExist) {
                        var listString = fileViewModel.readDataFromFileInternal()
                        importCSV(listString)
                    } else {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.type = "text/*"
                        resultLauncher.launch(intent)
                    }
                }
                .setNegativeButton("No") { dialogInterface, which ->
                }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun exportCSV() {
        diaryViewModel.getAllDiary().observe(this, Observer {
            if (it.size == 0) {
                showDialogCheckPass("Không có dữ liệu để backup")
            } else {
                fileViewModel.writeDataToFile(it)
                val context = requireContext().applicationContext
                val fileLocation = File(requireContext().filesDir, "data.csv")
                val path: Uri = FileProvider.getUriForFile(
                    context,
                    "com.example.mydiary.fileprovider",
                    fileLocation
                )

                val fileIntent = Intent(Intent.ACTION_SEND)
                fileIntent.type = "text/csv"
                fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data")
                fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                fileIntent.putExtra(Intent.EXTRA_STREAM, path)
                startActivity(Intent.createChooser(fileIntent, "Send Drive"))
            }
        })
    }

    private fun importCSV(listData: List<String>) {
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