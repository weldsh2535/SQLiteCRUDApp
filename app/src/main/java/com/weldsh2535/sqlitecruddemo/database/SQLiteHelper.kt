package com.weldsh2535.sqlitecruddemo.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.weldsh2535.sqlitecruddemo.Model.Student

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "student.db"
        private const val DATABASE_VERSION = 1
        private const val TBL_STUDENT = "tbl_student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
       val createStudent =  "CREATE TABLE " + TBL_STUDENT + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT," + EMAIL + " TEXT)"
        db?.execSQL(createStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT")
        onCreate(db)
    }
    fun insertStudent(stud:Student):Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,stud.id)
        contentValues.put(NAME,stud.name)
        contentValues.put(EMAIL,stud.email)

        val success = db.insert(TBL_STUDENT,null,contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllStudents(): ArrayList<Student> {
        val stdList: ArrayList<Student> = ArrayList()
        val selectedQuery = "SELECT * FROM $TBL_STUDENT"
        val db = this.readableDatabase
        var cursor:Cursor? = null

        try {
            cursor = db.rawQuery(selectedQuery,null)
        } catch (e:java.lang.Exception){
            e.printStackTrace()
            db.execSQL(selectedQuery)
        }
        var id:Int
        var name:String
        var email:String

        if (cursor!!.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))

                val std = Student(id =id, name =name, email =email)
                stdList.add(std)
            } while (cursor.moveToNext())
        }
        return stdList
    }

    fun updateStudent(stud:Student):Int {
        val db = this.writableDatabase

        val contentValues  = ContentValues()
        contentValues.put(ID,stud.id)
        contentValues.put(NAME,stud.name)
        contentValues.put(EMAIL,stud.email)

        val success = db.update(TBL_STUDENT,contentValues,"id=" +stud.id,null)
        db.close()
        return success
    }
    fun deleteStudent(id:Int):Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,id)

        val success = db.delete(TBL_STUDENT,"id=$id",null)
        db.close()
        return success
    }
}