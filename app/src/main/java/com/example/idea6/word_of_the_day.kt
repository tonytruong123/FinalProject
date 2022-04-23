package com.example.idea6

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.idea6.customdict.CustomDict
import com.example.idea6.customdict.CustomDictViewModel
import com.example.idea6.databinding.FragmentWordOfTheDayBinding
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.DataFormatter
import java.io.*
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

        //Open Dictionary
        val file: File = File(context?.filesDir, "dictionary.xls")
        var fileInputStream: FileInputStream? = null
        fileInputStream = FileInputStream(file);
        val workbook = HSSFWorkbook(fileInputStream);
        val sheet = workbook.getSheetAt(0);
        val formatter = DataFormatter()
        //Generate Ranadom Number and get word
        var myRandomInt:Int = 0
        var wordName:String = ""
        var wordDef:String = ""

        val save_index = File(context?.filesDir, "lastword.txt")
        d("check1", "aaa")
        if(save_index.exists()){
            myRandomInt = read_index()
            wordName = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(0))
            wordDef = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(1))
            //Change Def and Name
            binding.word.text = wordName
            binding.definition.text = wordDef
        }else{
            d("check2", "bbb")
            myRandomInt = Random.nextInt(1..13161)
            wordName = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(0))
            wordDef = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(1))
            //Change Def and Name
            binding.word.text = wordName
            binding.definition.text = wordDef
            write_to_file(myRandomInt)
        }

        //Change word
        reload_button.setOnClickListener{
            myRandomInt = Random.nextInt(1..13161)
            wordName = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(0))
            wordDef = formatter.formatCellValue(sheet.getRow(myRandomInt).getCell(1))
            binding.word.text = wordName
            binding.definition.text = wordDef
            //Write current word to save
            delete_index()
            write_to_file(myRandomInt)
            rotater()
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
            val action = word_of_the_dayDirections.actionWordOfTheDayToNewSettingsFragment()
            view.findNavController().navigate(action)
        }

    }
    // disableView
    private fun ObjectAnimator.disableViewDuringAnimation(view: View){
        addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?){
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

    private fun rotater() {
        val reloadbutton:Button
        val animator = ObjectAnimator.ofFloat(binding.reloadbutton, View.ROTATION, -360f,0f)
        animator.duration = 1000
        animator.disableViewDuringAnimation(binding.reloadbutton)
        animator.start()
    }

    fun write_to_file(word_index:Int){
        val bufferedWriter =
            BufferedWriter(FileWriter(File(context?.filesDir.toString() + File.separator + "lastword.txt")))
        bufferedWriter.write(word_index.toString())
        bufferedWriter.close()
    }

    fun delete_index(){
        val file = File(context?.filesDir, "lastword.txt")
        file.delete()
    }

    fun read_index():Int{
        val bufferedReader: BufferedReader = File(context?.filesDir, "lastword.txt").bufferedReader()
        val index = bufferedReader.use { it.readText() }
        d("test", index)
        return((index).toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}