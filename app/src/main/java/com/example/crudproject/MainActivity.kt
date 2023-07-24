package com.example.crudproject

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudproject.NotesDB.Companion.notesDB
import com.example.crudproject.databinding.ActivityMainBinding
import com.example.crudproject.databinding.CustomDialogLayoutBinding

class MainActivity : AppCompatActivity() {
    private val arrayList = arrayListOf<NotesEntity>()
    private lateinit var binding: ActivityMainBinding
    lateinit var notesDB: NotesDB
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var layoutManager: LinearLayoutManager
    //lateinit var clicks: Clicks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notesDB = NotesDB.getNotesDatabase(this)
        recyclerAdapter = RecyclerAdapter(arrayList, this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.RecyclerAct.layoutManager = layoutManager
        binding.RecyclerAct.adapter = recyclerAdapter

        binding.fab.setOnClickListener {
            val dialogBinding = CustomDialogLayoutBinding.inflate(layoutInflater)
            val dialog = Dialog(this)
            dialog.setContentView(dialogBinding.root)
            dialog.setCancelable(false)
            dialogBinding.btnAdd.setOnClickListener {
                class Insert : AsyncTask<Void, Void, Void>() {
                    override fun doInBackground(vararg params: Void?): Void? {
                        notesDB.notesDao().insertNoteData(
                            NotesEntity(
                                title = dialogBinding.etName.text.toString(),
                                description = dialogBinding.etRoll.text.toString()
                            )
                        )
                        return null
                    }

                    override fun onPostExecute(result: Void?) {
                        super.onPostExecute(result)
                        getNotes()
                    }
                }
                Insert().execute()
                recyclerAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            dialogBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun getNotes() {
        arrayList.clear()
        class Retrive : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                arrayList.addAll(notesDB.notesDao().getNotes())
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                recyclerAdapter.notifyDataSetChanged()
            }
        }
        Retrive().execute()
        }

    }
