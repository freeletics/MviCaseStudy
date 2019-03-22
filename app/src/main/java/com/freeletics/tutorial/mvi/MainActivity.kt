package com.freeletics.tutorial.mvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.freeletics.tutorial.mvi.model.Item
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: ItemAdapter

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        adapter = ItemAdapter(layoutInflater)
        recyclerView.adapter = adapter

        val listener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val endReached = !recyclerView.canScrollVertically(1)
                Log.d("RecyclerView",  "Scrollchanged: $endReached")
                if (endReached) {
                    onEndOfRecyclerViewReached()
                }
            }
        }
        recyclerView.addOnScrollListener(listener)

        error.setOnClickListener {
            viewModel.loadInitial()
        }

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.state.observe(this, Observer<State> {
            render(it)
        })

        viewModel.loadInitial()
    }

    private fun render(it: State) {
        loading.gone()
        error.gone()

        when(it) {
            is State.Content -> {
                recyclerView.show()
                applyNewData(it.items)
            }
            is State.Loading -> loading.show()
            is State.InitialError -> {
                recyclerView.gone()
                error.show()
            }
            is State.IncrementError -> {
                applyNewData(it.items)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyNewData(items: List<Item>) {
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    /**
     * RecyclerView reached the end of the list. User scrolled to the end of the list.
     */
    private fun onEndOfRecyclerViewReached() {
        viewModel.loadNext()
    }
}
