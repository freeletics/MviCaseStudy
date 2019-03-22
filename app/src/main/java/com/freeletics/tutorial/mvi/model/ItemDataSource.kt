package com.freeletics.tutorial.mvi.model

import io.reactivex.Single
import kotlinx.coroutines.delay
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicInteger

class ItemDataSource(
    /**
     * A delay that will be added in milliseconds
     */
    private val delayMs: Long = 1500,
    /**
     * Every X request an error should be thrown
     */
    private val errorEveryXRequests: Int = 3,

    /**
     * How many items should be displayed on the page
     */
    private val itemsPerPage: Int = 20
) {

    private var requestCounter = AtomicInteger()

    fun getPageSync(page: Int): List<Item> {
        if (requestCounter.getAndIncrement() % errorEveryXRequests == 0) {
            throw RuntimeException("Fake Exception")
        }

        return (page * itemsPerPage..page * itemsPerPage + 19)
            .map { Item(id = it, name = "Item $it") }
    }

    /**
     * Get the page in the Rx way.
     */
    fun getPageRx(page: Int): Single<List<Item>> = Single.fromCallable {
        Thread.sleep(delayMs)
        getPageSync(page)
    }

    /**
     * get the page in the coroutine style
     */
    suspend fun getPageCo(page: Int): List<Item> {
        delay(delayMs)
        return getPageSync(page)
    }

    /**
     * Get the page by passing the callback lambdas throught it
     */
    fun getPageAsync(
        page: Int,
        successCallback: (List<Item>) -> Unit,
        errorCallback: (Throwable) -> Unit
    ) {
        getPageRx(page).subscribe({
            successCallback(it)
        },
            {
                errorCallback(it)
            })
    }
}