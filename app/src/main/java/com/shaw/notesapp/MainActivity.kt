package com.shaw.notesapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*

class MainActivity : AppCompatActivity() {
    var listNotes = ArrayList<Notes>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //listNotes.add(Notes(1, "christmas", "Christmas is the most important festival among Christians. It is celebrated to mark the birth of Jesus, who is the supreme lord of the Christians. It marks the beginning of the days of hope for those oppressed before the birth of Jesus. Christmas celebrates the spirit of joy and merry-making."))
       // listNotes.add(Notes(2, "bestfriend", "My best friend encourages me to improve as an individual. We plan our weeks and spend time together. My best friend is the individual who makes me upbeat and is someone I love and care for. My best friend has been my emotional support."))
        //listNotes.add(Notes(3, "school", "School is the place where we learn to read and write. It is the most crucial place for a student, and it helps us to learn new things. The teachers are always helpful and teach us important things in life. We must always be regular to school as missing classes can lead to problems during exams."))

        loadQuery("%")

    }


    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }
    fun loadQuery(title:String){
        var dbManager=DbManager(this)
        val projection= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.query(projection,"Title like ?",selectionArgs,"Title")
        if (cursor.moveToFirst()){
            do{
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val title=cursor.getString(cursor.getColumnIndex("Title"))
                val desc=cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Notes(ID,title,desc))



            }while (cursor.moveToNext())

        }
        var myadapter=MyNotesAdapter(this,listNotes)
        findViewById<ListView>(R.id.listid).adapter=myadapter


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val sv=menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Toast.makeText(applicationContext,p0,Toast.LENGTH_LONG).show()
                loadQuery("%"+p0+"%")
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item!=null) {
            when (item.itemId) {
                R.id.addid -> {
                    var intent=Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    inner class MyNotesAdapter:BaseAdapter {
        var listNotesAdapter=ArrayList<Notes>()
        var context:Context?=null
        constructor(context: Context,listNotesAdapter:ArrayList<Notes>):super(){
            this.listNotesAdapter=listNotesAdapter
            this.context=context
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val inflator=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var myView=inflator.inflate(R.layout.ticket,null)
            var myNode=listNotesAdapter[p0]
            myView.findViewById<TextView>(R.id.titleid).text=myNode.noteName
            myView.findViewById<TextView>(R.id.contentid).text=myNode.noteDesc
            myView.findViewById<ImageView>(R.id.deleteid).setOnClickListener(View.OnClickListener {
                var dbManager=DbManager(this.context!!)
                val selectionArgs= arrayOf(myNode.noteId.toString())
                dbManager.delete("ID=?",selectionArgs)
                loadQuery("%")
            })
            myView.findViewById<ImageView>(R.id.editid).setOnClickListener(View.OnClickListener {
                goToUpdates(myNode)

            })
            return myView

        }

        override fun getItem(p0: Int): Any {
            return listNotesAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }
    }
    fun goToUpdates(note:Notes){
        var intent=Intent(this,AddNotes::class.java)
        intent.putExtra("Id",note.noteId)
        intent.putExtra("Name",note.noteName)
        intent.putExtra("Desc",note.noteDesc)
        startActivity(intent)

    }


}