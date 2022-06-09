package com.ubaya.advweek4.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.advweek4.R
import com.ubaya.advweek4.databinding.FragmentStudentDetailBinding
import com.ubaya.advweek4.model.Student
import com.ubaya.advweek4.util.loadImage
import com.ubaya.advweek4.viewmodel.DetailViewModel
import com.ubaya.advweek4.viewmodel.ListViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_student_detail.*
import kotlinx.android.synthetic.main.fragment_student_list.*
import kotlinx.android.synthetic.main.student_list_item.view.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 * Use the [StudentDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentDetailFragment : Fragment(), ButtonUpdateClickListener, ButtonNotificationClickListener {
    private lateinit var viewModel: DetailViewModel
    private lateinit var dataBinding:FragmentStudentDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_detail, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments != null) {
            val studentID = StudentDetailFragmentArgs.fromBundle(requireArguments()).studentID
            viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
            viewModel.fetch(studentID)

            dataBinding.updateListener = this
            dataBinding.notificationListener = this

            observeViewModel()
        }
    }

    private fun observeViewModel() {
        viewModel.studentsLiveData.observe(viewLifecycleOwner) {
            val student = it
            Log.d("hasil", student.toString())
            student?.let {
                editID.setText(it.id)
                editDOB.setText(it.dob)
                editName.setText(it.name)
                editPhone.setText(it.phone)
                imageStudentPhotoDetail.loadImage(it.photoURL, progressLoadingStudentPhotoDetail)
                buttonNotif.setOnClickListener {
                    Observable.timer(5, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            Log.d("mynotif", "Notification delayed after 5 seconds")
                            Log.d("student name", student.name.toString())
                            student.name?.let { studentName ->
                                MainActivity.showNotification(
                                    studentName,
                                    "A new notification created", R.drawable.ic_baseline_person_24)
                            }
                        }
                }
            }
        }
    }

    override fun onButtonUpdateClick(v: View) {
        Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show()
    }

    override fun onButtonNotificationClick(v: View, obj: Student) {
        Observable.timer(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("mynotif", "Notification delayed after 5 seconds")
                Log.d("student name", obj.name.toString())
                obj.name?.let { studentName ->
                    MainActivity.showNotification(
                        studentName,
                        "A new notification created", R.drawable.ic_baseline_person_24)
                }
            }
    }
}