package com.shaw.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {
    val dbName="MyNotes"
    val dbTable="Notes"
    val colID="ID"
    val colTitle="Title"
    val colDesc="Description"
    val dbVersion=1
    val sqlCreateTable="CREATE TABLE IF NOT EXISTS "+dbTable+"( "+colID+" INTEGER PRIMARY KEY," +
            colTitle+" TEXT, "+colDesc+" TEXT);"
    var sqlDb:SQLiteDatabase?=null

    constructor(context: Context){
        var dB=DatabaseHelperNotes(context)
        sqlDb=dB.writableDatabase
    }
    inner class DatabaseHelperNotes:SQLiteOpenHelper{
        var context:Context?=null
        constructor(context: Context):super(context,dbName,null,dbVersion){
            this.context=context

        }
        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"Database is created",Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("DROP TABLE IF EXISTS "+dbTable)
        }
    }
    fun Insert(values:ContentValues):Long{
        val ID=sqlDb!!.insert(dbTable,"",values)
        return ID

    }
    fun query(projection:Array<String>,selection:String,selectionArgs:Array<String>,sortOrder:String):Cursor{
        val qb=SQLiteQueryBuilder()
        qb.tables=dbTable
        val cursor=qb.query(sqlDb,projection,selection,selectionArgs,null,null,sortOrder)
        return cursor
    }
    fun delete(selection:String,selectionArgs:Array<String>):Int{
        val count=sqlDb!!.delete(dbTable,selection,selectionArgs)
        return count
    }
    fun update(values: ContentValues,selection:String,selectionArgs:Array<String>):Int{
        val count=sqlDb!!.update(dbTable,values,selection,selectionArgs)
        return count

    }




}
