package com.freeletics.tutorial.mvi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.freeletics.tutorial.mvi.model.Item
import com.freeletics.tutorial.mvi.model.ItemDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val internalState: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = internalState

    private val dataSource: ItemDataSource = getApplication<MviApplication>().dataSource

    // TODO Nice to have it immutable
    private var pageCount = 0
    private val allItems = mutableListOf<Item>()

    fun loadInitial() {

        if (internalState.value !is State.Content) {
            internalState.value = State.Loading

            val d = dataSource.getPageRx(pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    pageCount++
                    allItems.addAll(items)
                    internalState.value = State.Content(allItems)
                }, { throwable ->
                    internalState.value =
                        State.InitialError("Error on loading data: ${throwable.message}")

                })
        }
    }


    fun loadNext() {
        if (internalState.value != State.Loading) {
            internalState.value = State.Loading

            val d = dataSource.getPageRx(pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    pageCount++
                    allItems.addAll(items)
                    internalState.value = State.Content(allItems)

                }, { throwable ->
                    if (allItems.isEmpty()) {
                        internalState.value =
                            State.InitialError("Error on loading data: ${throwable.message}")
                    } else {
                        internalState.value = State.IncrementError(
                            allItems,
                            "Error on loading data: ${throwable.message}"
                        )
                    }
                })
        }
    }

}

sealed class State {
    object Loading : State()
    data class Content(val items: List<Item>) : State()
    data class InitialError(val message: String) : State()
    data class IncrementError(val items: List<Item>, val message: String) : State()
}

