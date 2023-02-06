package com.weldsh2535.sqlitecruddemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weldsh2535.sqlitecruddemo.Adapter.StudentAdapter
import com.weldsh2535.sqlitecruddemo.Model.Student
import com.weldsh2535.sqlitecruddemo.database.SQLiteHelper
import com.weldsh2535.sqlitecruddemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sqliteHelper: SQLiteHelper
    private var adapter: StudentAdapter? = null
    private var std: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        sqliteHelper = SQLiteHelper(this)

        binding.btnAdd.setOnClickListener { addStudent() }
        binding.btnView.setOnClickListener { getStudent() }
        binding.btnUpdate.setOnClickListener { updateStudent() }
        //click item listner
        adapter?.setOnClickItem {
            Toast.makeText(this@MainActivity, it.name.toString(), Toast.LENGTH_SHORT).show()
            //ok now we need to update record
            binding.edName.setText(it.name)
            binding.edEmail.setText(it.email)
            std = it
        }
        //
        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }
    }

    private fun updateStudent() {
        val name = binding.edName.text.toString()
        val email = binding.edEmail.text.toString()
        if (name == std?.name && email == std?.email) {
            Toast.makeText(this, "Record not changed...,", Toast.LENGTH_SHORT).show()
            return
        }
        if (std == null) return
        val std = Student(id = std!!.id, name = name, email = email)
        val status = sqliteHelper.updateStudent(std)
        if (status > -1) {
            clearEditText()
            getStudent()
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        binding.recyclerview.adapter = adapter
    }

    private fun getStudent() {
        val studList = sqliteHelper.getAllStudents()

        Log.i("MyTag", studList.size.toString())
        adapter?.addItems(studList)
    }

    private fun addStudent() {
        val name = binding.edName.text.toString()
        val email = binding.edEmail.text.toString()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter required filed ", Toast.LENGTH_SHORT).show()
        } else {
            val std = Student(name = name, email = email)
            val status = sqliteHelper.insertStudent(std)
            if (status > -1) {
                Toast.makeText(this, "Student Added ...", Toast.LENGTH_SHORT).show()
                clearEditText()
                getStudent()
            } else {
                Toast.makeText(this, "Record not Saved ...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteStudent(id: Int) {
        if (id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure want to delete item?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes") { dialog, _ ->
            sqliteHelper.deleteStudent(id)
            getStudent()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        binding.edName.text.clear()
        binding.edEmail.text.clear()
    }
}