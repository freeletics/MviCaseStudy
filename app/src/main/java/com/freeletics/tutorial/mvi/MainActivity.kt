package com.freeletics.tutorial.mvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: ItemAdapter

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
    }

    /**
     * RecyclerView reached the end of the list. User scrolled to the end of the list.
     */
    private fun onEndOfRecyclerViewReached() {
    }
}
