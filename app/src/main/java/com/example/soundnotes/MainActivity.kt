package com.example.soundnotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.soundnotes.Adapter.NAdapter
import com.example.soundnotes.Database.DB
import com.example.soundnotes.Models.NViewmodel
import com.example.soundnotes.Models.Note
import com.example.soundnotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DB
    lateinit var viewModel: NViewmodel
    lateinit var adapter: NAdapter
    lateinit var selected: Note

    private val  updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->

        if(result.resultCode == Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note")as? Note
            if (note!=null){
                viewModel.updateN(note)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        UI()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NViewmodel::class.java)
        viewModel.allnotes.observe(this, { list ->
            list?.let {
                adapter.updateL(list)
            }
        })

        database = DB.getDatabase(this)
    }

    private fun UI() {

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                if (result.resultCode == Activity.RESULT_OK) {
                    val note = result.data?.getSerializableExtra("note") as? Note
                    if (note != null) {
                        viewModel.insertN(note)
                    }
                }

        }

        binding.add.setOnClickListener {
            val  intent = Intent(this, Add::class.java)
            getContent.launch(intent)
        }

        binding.search.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText != null){
                    adapter.filterL(newText)
                }
                return true
            }

        })


    }

    override fun onItemCliclked(note: Note) {
        val intent = Intent(this, Add::class.java)
        intent.putExtra("cur_note", note)
        updateNote.launch(intent)
    }

    override fun onItemLongClicked(note: Note, cardView: CardView){
        selected = note

        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.inflate(R.menu.delete_item)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.delete_item){
            viewModel.deleteN(selected)
            return true
        }
        return false
    }
}