package com.example.idea6

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.idea6.customdict.CustomDict
import com.example.idea6.customdict.CustomDictViewModel
import com.example.idea6.customdict.CustomDictViewModelFactory
import com.example.idea6.databinding.FragmentWordOfTheDayBinding
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.DataFormatter
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt



class word_of_the_day : Fragment(R.layout.fragment_word_of_the_day) {

    private var _binding: FragmentWordOfTheDayBinding? = null
    private val binding get() = _binding!!
    private lateinit var customDictViewModel: CustomDictViewModel
    private lateinit var activity:MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = getActivity() as MainActivity
        customDictViewModel = activity.getDatabase()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordOfTheDayBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val custom_dict_button = binding.goToDictButton
        val settings_button = binding.goToSettings
        val reload_button = binding.reloadbutton
        val add_dict_button = binding.actdictbutton

        //Generate Random Number
        val file: File = File(context?.filesDir, "dictionary.xls")
        var fileInputStream: FileInputStream? = null
        fileInputStream = FileInputStream(file);
        val workbook = HSSFWorkbook(fileInputStream);
        val sheet = workbook.getSheetAt(0);
        val formatter = DataFormatter()
        var myRandomInt = Random.nextInt(1..13161)
        var wordName = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(0))
        var wordDef = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(1))
        //Change Def and Name
        binding.word.text = wordName
        binding.definition.text = wordDef

        //Change word
        reload_button.setOnClickListener{
            myRandomInt = Random.nextInt(1..13161)
            wordName = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(0))
            wordDef = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(1))
            binding.word.text = wordName
            binding.definition.text = wordDef
        }
        custom_dict_button.setOnClickListener{
            val action = word_of_the_dayDirections.actionWordOfTheDayToCustomDictFragment()
            view.findNavController().navigate(action)
        }
        add_dict_button.setOnClickListener{
            customDictViewModel.insert(CustomDict(wordName, wordDef))
            Toast.makeText(getActivity(), "${wordName} added to dictionary", Toast.LENGTH_SHORT).show()
        }
        settings_button.setOnClickListener{
            val action = word_of_the_dayDirections.actionWordOfTheDayToSettingsFragment()
            view.findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}