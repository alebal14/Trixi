package com.example.trixi.ui.post

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.trixi.R

class PostActivity : AppCompatActivity () {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_post);

        //hardcoded data for dropdown, will later be replaced with data from rest-api
        val pets = arrayOf("Stina", "Johan", "Betty")
        val categories = arrayOf("Tricks", "Cute", "Obedience")

        val petOptions : Spinner = findViewById(R.id.upload_spinner_add_pet);
        val categoryOptions : Spinner = findViewById(R.id.upload_spinner_add_category);

        categoryOptions.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        petOptions.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pets);


        categoryOptions.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val selectedCategoryItem = categoryOptions.get(position)
                val selectedPetItem = petOptions.get(position)
                Log.d(selectedCategoryItem.toString(), "the selected category")
                Log.d(selectedPetItem.toString(), "the selected pet")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }








    }
}