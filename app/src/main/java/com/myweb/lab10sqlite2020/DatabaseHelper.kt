package com.myweb.lab10sqlite2020

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper private constructor(context: Context): SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION){
    companion object{
        private val DB_NAME = "StudentDB"
        private val DB_VERSION = 1
        private val TABLE_NAME ="student"
        private val COLUMN_ID = "id"
        private val COLUMN_NAME = "name"
        private val COLUMN_AGE = "age"
        private val sqliteHelper:DatabaseHelper? = null

        @Synchronized
        fun getInstance(c:Context):DatabaseHelper{
            if(sqliteHelper==null){
                return DatabaseHelper(c.applicationContext)
            }else{
                return sqliteHelper
            }
        }
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($COLUMN_ID TXET PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_AGE INTEGER)"
        db?.execSQL(CREATE_TABLE)

        val sqlInsert ="INSERT INTO $TABLE_NAME VALUES('1','Alice', 20)"
        db?.execSQL(sqlInsert)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAllStudents():ArrayList<Student>{
        val std = ArrayList<Student>()
        val db = writableDatabase
        var cursor:Cursor? = null
        try{
            cursor = db.rawQuery("select * from $TABLE_NAME",null)
        }catch (e:SQLiteException){
            onCreate(db)
            return ArrayList()
        }
        var id :String
        var name :String
        var age :Int
        if(cursor.moveToFirst()){
            while (cursor.isAfterLast==false){
                id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE))

                std.add(Student(id,name,age))
                cursor.moveToNext()
            }
        }
        db.close()
        return std
    }

    fun insertStudent(std:Student):Long{
        val db = writableDatabase
        val value = ContentValues()
        value.put(COLUMN_ID,std.id)
        value.put(COLUMN_NAME,std.name)
        value.put(COLUMN_AGE,std.age)
        val success = db.insert(TABLE_NAME,null,value)
        db.close()
        return success
    }

    fun updateStudent(std:Student):Int{
        val db = writableDatabase
        val value = ContentValues()
        value.put(COLUMN_NAME,std.name)
        value.put(COLUMN_AGE,std.age)
        val success =db.update(TABLE_NAME,value,"$COLUMN_ID = ?", arrayOf(std.id))
        db.close()
        return success
    }

    fun deleteStudent(std_id:String):Int{
        val db = writableDatabase
        val success = db.delete(TABLE_NAME,"$COLUMN_ID = ?", arrayOf(std_id))
        db.close()
        return success
    }
}