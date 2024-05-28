package com.example.soundnotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.soundnotes.Models.Note
import com.example.soundnotes.databinding.ActivityAddBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Add : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding

    private lateinit var note : Note
    private lateinit var old_n :Note
    var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        try {

            old_n = intent.getSerializableExtra("cur_note") as Note
            binding.title.setText(old_n.title)
            binding.text.setText(old_n.note)
            isUpdate = true
        }catch (e : Exception){
            e.printStackTrace()
        }

        binding.record.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something")
            startActivityForResult(intent, 100)
        }

        binding.add.setOnClickListener{
            val title = binding.title.text.toString()
            val note_d = binding.text.text.toString()

            if (title.isNotEmpty() || note_d.isNotEmpty()){

                val formatter = SimpleDateFormat("EEE, d MMM yyy HH:mm")

                if(isUpdate){
                    note = Note(
                        old_n.id, title , note_d, formatter.format(Date())
                    )
                }else{
                    note = Note(
                        null, title , note_d, formatter.format(Date())
                    )
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }else{
                Toast.makeText(this@Add, "Dodaj notatkę lub nadaj jej tytuł", Toast.LENGTH_SHORT).show()

            }
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && data != null){
            val res : ArrayList<String> = data!!.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS)!!
            binding.text.setText(res[0])
        }
    }
}