package com.example.idea6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.idea6.databinding.FragmentCustomDictBinding
import com.example.idea6.adapter.WordAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [CustomDictFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomDictFragment : Fragment() {
    private var _binding: FragmentCustomDictBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
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
        val test_words = listOf("Test1", "Test2", "Test3", "Test4", "Test1", "Test2", "Test3", "Test4")
        recyclerView.adapter = WordAdapter(test_words)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}