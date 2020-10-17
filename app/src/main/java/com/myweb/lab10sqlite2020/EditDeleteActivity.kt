package com.myweb.lab10sqlite2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_edit_delete.*

class EditDeleteActivity : AppCompatActivity() {
    private lateinit var dbHandler:DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete)

        dbHandler = DatabaseHelper.getInstance(baseContext)
        val mID = intent.getStringExtra("mId")
        val mName = intent.getStringExtra("mName")
        val mAge = intent.getStringExtra("mAge")

        edt_id.setText(mID)
        edt_id.isEnabled = false
        edt_name.setText(mName)
        edt_age.setText(mAge)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateStudent(v:View){
        var id = edt_id.text.toString()
        var name = edt_name.text.toString()
        var age = edt_age.text.toString().toInt()
        var result = dbHandler.updateStudent(Student(id=id,name=name,age=age))
        if(result > -1){
            Toast.makeText(applicationContext,"The student is updated successfully", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(applicationContext,"Insert Failure", Toast.LENGTH_LONG).show()
        }
        finish()
    }

    fun deleteStudent(v:View){
        val myBuilder = AlertDialog.Builder(this)
        myBuilder.apply {
            setTitle("Warning Message")
            setMessage("Do you want to delete the student?")
            setNegativeButton("Yes") { dialog, which ->
                val result = dbHandler?.deleteStudent(edt_id.text.toString())
                if (result!! > -1) {
                    Toast.makeText(applicationContext, "The student is deleted successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Delete Failure", Toast.LENGTH_LONG).show()
                }
                finish()
            }
            setPositiveButton("No") { dialog, which ->  dialog.cancel() }
            show()
        }

    }
}