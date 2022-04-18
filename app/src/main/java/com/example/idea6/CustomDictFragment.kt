package com.example.idea6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.idea6.adapter.WordAdapter
import com.example.idea6.customdict.CustomDict
import com.example.idea6.customdict.CustomDictViewModel
import com.example.idea6.databinding.FragmentCustomDictBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CustomDictFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomDictFragment : Fragment() {

    private var _binding: FragmentCustomDictBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    private lateinit var activity:MainActivity
    private lateinit var customDictViewModel: CustomDictViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        activity = getActivity() as MainActivity
        customDictViewModel = activity.getDatabase()
        customDictViewModel.delete("af")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomDictBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        //Test Words
//        val test_words = listOf("Test1", "Test2", "Test3", "Test4", "Test1", "Test2", "Test3", "Test4")
        val adapter = WordAdapter()
        recyclerView.adapter = adapter
        customDictViewModel.allWords.observe(viewLifecycleOwner) { words ->
            words.let { adapter.submitList(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}