package com.example.idea6

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.idea6.database.CustomDict
import com.example.idea6.customdict.CustomDictViewModel
import com.example.idea6.databinding.FragmentWordOfTheDayBinding
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.DataFormatter
import java.io.*
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
    ): View {
        _binding = FragmentWordOfTheDayBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reload_button = binding.reloadbutton
        val add_dict_button = binding.actdictbutton

        //Open Dictionary
        val file = File(context?.filesDir, "dictionary_edit.xls")
        val fileInputStream = FileInputStream(file)
        val workbook = HSSFWorkbook(fileInputStream)
        val sheet = workbook.getSheetAt(0)
        val formatter = DataFormatter()
        //Generate Random Number and get word
        var myRandomInt: Int
        var wordName = ""
        var wordPron = ""
        var wordDef = ""

        fun updateButton(name: String) {
            val wordInDict = customDictViewModel.wordIsPresent(name)

            // Update Add to Dict button
            if (!wordInDict) {
                add_dict_button.isEnabled = true
                add_dict_button.text = getString(R.string.add_to_custom_dictionary)
            } else {
                add_dict_button.isEnabled = false
                add_dict_button.text = getString(R.string.added_to_custom_dictionary)
            }
        }

        fun updateWord(randomInt: Int) {
            wordName = formatter.formatCellValue(sheet.getRow(randomInt).getCell(0))
            wordPron = formatter.formatCellValue(sheet.getRow(randomInt).getCell(1))
            wordDef = formatter.formatCellValue(sheet.getRow(randomInt).getCell(2))

            // Update TextView
            binding.word.text = wordName
            if (wordName == wordPron) {
                binding.pronunciation.text = getString(R.string.pronunciation_unavailable)
            } else {
                binding.pronunciation.text = getString(R.string.pronunciation_format, wordPron)
            }
            binding.definition.text = wordDef
            updateButton(wordName)
        }

        val save_index = File(context?.filesDir, "lastword.txt")
        d("check1", "aaa")
        if(save_index.exists()){
            myRandomInt = read_index()
            updateWord(myRandomInt)
        }else{
            d("check2", "bbb")
            myRandomInt = Random.nextInt(1..13161)
            updateWord(myRandomInt)
            write_to_file(myRandomInt)
        }

        //Change word
        reload_button.setOnClickListener{
            myRandomInt = Random.nextInt(1..13161)
            updateWord(myRandomInt)
            //Write current word to save
            delete_index()
            write_to_file(myRandomInt)
            // Perform animation
            rotater()
            scaler()
        }
        add_dict_button.setOnClickListener{
            customDictViewModel.insert(CustomDict(wordName, wordPron, wordDef))
            Toast.makeText(getActivity(), "$wordName added to dictionary", Toast.LENGTH_SHORT).show()
            updateButton(wordName)
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
        val animator = ObjectAnimator.ofFloat(binding.refresh, View.ROTATION, 360f,0f)
        animator.duration = 400
        animator.disableViewDuringAnimation(binding.reloadbutton)
        animator.start()
    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(binding.word, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(binding.word)
        animator.start()
    }

    private fun write_to_file(word_index:Int){
        val bufferedWriter =
            BufferedWriter(FileWriter(File(context?.filesDir.toString() + File.separator + "lastword.txt")))
        bufferedWriter.write(word_index.toString())
        bufferedWriter.close()
    }

    private fun delete_index(){
        val file = File(context?.filesDir, "lastword.txt")
        file.delete()
    }

    private fun read_index():Int{
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