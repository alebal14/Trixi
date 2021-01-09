package com.example.trixi.ui.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.trixi.R
import com.example.trixi.entities.PetType
import com.example.trixi.repository.PostToDb
import com.example.trixi.repository.TrixiViewModel
import kotlinx.android.synthetic.main.fragment_pet_register.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditPetProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditPetProfile : Fragment() {

    val db = PostToDb()
    val ownerId = PostToDb.loggedInUser?.uid.toString()
    var petName = ""
    var petAge = ""
    var petBreed = ""
    var petBio = ""
    var petTypeName = ""
    var gender = ""

    var mContext: Context? = null;


    private val REQUEST_PERMISSION = 100

    var selectedFile: Uri? = null
    var filePath = ""
    var mediaPath: String? = null
    var postPath: String? = null

    var file: File? = null
    var file_validation = false
    lateinit var model: TrixiViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_pet_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProvider(this).get(TrixiViewModel::class.java)

        setUpSpinners()

    }

    private fun setUpSpinners() {

        if (register_spinner_pet_type != null) {
            model.getPetType().observe(viewLifecycleOwner, { allPetType ->
                val spinnerAdapter = ArrayAdapter<PetType>(
                    mContext!!,
                    android.R.layout.simple_spinner_item,
                    allPetType
                )
                register_spinner_pet_type.adapter = spinnerAdapter
            })

            register_spinner_pet_type.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val petType: PetType = parent.selectedItem as PetType
                    selectPetTypeData(petType)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        if (register_spinner_gender != null) {
            val spinnerAdapter = ArrayAdapter.createFromResource(
                mContext!!,
                R.array.gender,
                android.R.layout.simple_spinner_item
            )
            register_spinner_gender.adapter = spinnerAdapter

            register_spinner_gender.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    gender = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }

    private fun selectPetTypeData(petType: PetType) {
        petTypeName = petType.name
    }

}