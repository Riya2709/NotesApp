package com.shaw.notesapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    val dbTable="Notes"
    var id=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try {

            var bundle:Bundle= intent.extras!!
             id=bundle.getInt("Id",0)
            if(id!=0) {
                findViewById<EditText>(R.id.titleup).setText(bundle.getString("Name"))
                findViewById<EditText>(R.id.descup).setText(bundle.getString("Desc"))
            }


        }catch (ex:Exception){}

    }
    fun buAdd(view:View) {
        var dbManager = DbManager(this)
        var values = ContentValues()
        values.put("Title", findViewById<EditText>(R.id.titleup).text.toString())
        values.put("Description", findViewById<EditText>(R.id.descup).text.toString())
        if (id == 0) {
            val Id = dbManager.Insert(values)
            if (Id > 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_LONG).show()
                finish()

            } else {
                Toast.makeText(this, "Cannot add note", Toast.LENGTH_LONG).show()

            }
        }else{
            var selectionArgs= arrayOf(id.toString())
            val ID=dbManager.update(values,"ID=?",selectionArgs)
            if (ID> 0) {
                Toast.makeText(this, "Note is added", Toast.LENGTH_LONG).show()
                finish()

            } else {
                Toast.makeText(this, "Cannot add note", Toast.LENGTH_LONG).show()

            }
        }
    }

}