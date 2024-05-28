package com.example.soundnotes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.soundnotes.R
import com.example.soundnotes.Models.Note


class NAdapter(private val context : Context, val listener: NotesClickListener) : RecyclerView.Adapter<NAdapter.NoteViewHolder>(){

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val curNote = NotesList[position]
        holder.title.text = curNote.title
        holder.title.isSelected = true
        holder.NoteVh.text = curNote.note
        holder.date.text = curNote.date
        holder.date.isSelected =  true
        holder.notesLayout.setOnClickListener {
            listener.onItemCliclked(NotesList[holder.adapterPosition])
        }

       holder.notesLayout.setOnLongClickListener {
           listener.onItemLongClicked(NotesList[holder.adapterPosition], holder.notesLayout)
           true
       }
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun updateL(newList :List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterL(search:String){
        NotesList.clear()

        for (item in fullList){
            if (item.title?.contains(search.lowercase())==true ||
                item.note?.lowercase()?.contains(search.lowercase())==true){
                NotesList.add(item)
            }
            notifyDataSetChanged()

        }
    }



    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val notesLayout = itemView.findViewById<CardView>(R.id.card)
        val title = itemView.findViewById<TextView>(R.id.title)
        val NoteVh = itemView.findViewById<TextView>(R.id.note)
        val date = itemView.findViewById<TextView>(R.id.date)
    }

    interface NotesClickListener{
        fun onItemCliclked(note: Note)
        fun onItemLongClicked(note: Note, cardView: CardView)
    }
}