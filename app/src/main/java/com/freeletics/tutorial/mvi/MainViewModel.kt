package com.freeletics.tutorial.mvi

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.freeletics.tutorial.mvi.model.Item
import com.freeletics.tutorial.mvi.model.ItemDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalStateException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val internalState: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = internalState

    private val dataSource: ItemDataSource = getApplication<MviApplication>().dataSource

    // TODO Nice to have it immutable
    private var pageCount = 0
//    private val allItems = mutableListOf<Item>()

    fun loadInitial() {

        updateState(Change.Loading)

        val d = dataSource.getPageRx(pageCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                pageCount++
                updateState(Change.NewItems(items))
            }, { throwable ->
                updateState(Change.Error(throwable))
            })

    }


    fun loadNext() {

        updateState(Change.Loading)

        val d = dataSource.getPageRx(pageCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                pageCount++
                updateState(Change.NewItems(items))

            }, { throwable ->
                updateState(Change.Error(throwable))
            })

    }

    private fun updateState(change: Change) {

        val state = internalState.value ?: State.Loading

        val newState: State = change.computeNextState(state)

        Log.d("MVI", "Change: ${change}, state: ${state}")

        internalState.value = newState
    }

}

sealed class State {
    object Loading : State()
    data class LoadingNext(val items: List<Item>) : State()
    data class Content(val items: List<Item>) : State()
    data class InitialError(val message: String) : State()
    data class IncrementError(val items: List<Item>, val message: String) : State()
}

sealed class Change {
    data class NewItems(val items: List<Item>) : Change() {
        override fun computeNextState(state: State): State {
            return when (state) {
                is State.Loading -> State.Content(items)
                is State.LoadingNext -> State.Content(state.items + items)
                else -> state
            }
        }
    }

    data class Error(val error: Throwable) : Change() {
        override fun computeNextState(state: State): State {
            return when (state) {
                is State.Loading -> State.InitialError(error.message!!)
                is State.LoadingNext -> State.IncrementError(state.items, error.message!!)
                else -> state
            }
        }
    }

    object Loading : Change() {
        override fun computeNextState(state: State): State {
            return when (state) {
                is State.InitialError -> State.Loading
                is State.IncrementError -> State.LoadingNext(state.items)
                is State.Content -> State.LoadingNext(state.items)
                else -> state
            }
        }
    }

    abstract fun computeNextState(state: State) : State
}
