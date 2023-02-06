package com.weldsh2535.sqlitecruddemo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.weldsh2535.sqlitecruddemo.Model.Student
import com.weldsh2535.sqlitecruddemo.R

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    private var stdList: ArrayList<Student> = ArrayList()
    private var onClickItem:((Student)-> Unit)? = null
    private var onClickDeleteItem:((Student)-> Unit)? = null

    fun addItems(studList: ArrayList<Student>) {
        this.stdList = studList
        notifyDataSetChanged()
    }
    fun setOnClickItem(callback: (Student) -> Unit){
       this.onClickItem = callback
    }
    fun setOnClickDeleteItem(callback: (Student) -> Unit){
        this.onClickDeleteItem = callback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.stud_list_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        //Click each item events
        holder.itemView.setOnClickListener { onClickItem?.invoke(std)  }
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(std) }
    }

    override fun getItemCount(): Int {
        return stdList.size
    }


    inner class StudentViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var email = view.findViewById<TextView>(R.id.tvEmail)
         var btnDelete = view.findViewById<TextView>(R.id.btnDelete)

        fun bindView(std: Student) {
            id.text = std.id.toString()
            name.text = std.name
            email.text = std.email
        }
    }
}