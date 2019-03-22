package com.freeletics.tutorial.mvi

import com.freeletics.tutorial.mvi.model.Item
import com.freeletics.tutorial.mvi.model.ItemDataSource
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec
import kotlinx.coroutines.runBlocking
import java.lang.RuntimeException

class ItemDataSourceSpec : BehaviorSpec({

    given("A ItemDataSource") {
        and("using Rx") {
            val dataSource = ItemDataSource(
                delayMs = 1,
                errorEveryXRequests = 3,
                itemsPerPage = 20
            )

            on("loading page 0") {
                val exception = shouldThrow<RuntimeException> {
                    dataSource.getPageRx(0).blockingGet()
                }

                then("error is thrown ") {
                    exception.message shouldBe "Fake Exception"
                }
            }

            on("loading page 0 again") {

                val items = dataSource.getPageRx(0).blockingGet()
                val expected = (0..19).map { Item(id = it, name = "Item $it") }

                then("items from 0 to 19 ") {
                    items shouldBe expected
                }

                then("items size is 20") {
                    items.size shouldBe 20
                }
            }

            on("loading page 1 again") {

                val items = dataSource.getPageRx(1).blockingGet()
                val expected = (20..39).map { Item(id = it, name = "Item $it") }

                then("items from 20 to 39 ") {
                    items shouldBe expected
                }

                then("items size is 20") {
                    items.size shouldBe 20
                }
            }

            on("loading page 2 again") {

                val exception = shouldThrow<RuntimeException> {
                    dataSource.getPageRx(2).blockingGet()
                }

                then("error is thrown ") {
                    exception.message shouldBe "Fake Exception"
                }
            }
        }

        and("using Coroutines") {
            val dataSource = ItemDataSource(
                delayMs = 1,
                errorEveryXRequests = 3,
                itemsPerPage = 20
            )

            on("loading page 0") {
                val exception = shouldThrow<RuntimeException> {
                    runBlocking {
                        dataSource.getPageCo(0)
                    }
                }

                then("error is thrown ") {
                    exception.message shouldBe "Fake Exception"
                }
            }

            on("loading page 0 again") {

                val items: List<Item> =
                    runBlocking {
                        dataSource.getPageCo(0)
                    }
                val expected = (0..19).map { Item(id = it, name = "Item $it") }

                then("items from 0 to 19 ") {
                    items shouldBe expected
                }

                then("items size is 20") {
                    items.size shouldBe 20
                }
            }

            on("loading page 1 again") {

                val items: List<Item> =
                    runBlocking {
                        dataSource.getPageCo(1)
                    }
                val expected = (20..39).map { Item(id = it, name = "Item $it") }

                then("items from 20 to 39 ") {
                    items shouldBe expected
                }

                then("items size is 20") {
                    items.size shouldBe 20
                }
            }

            on("loading page 2 again") {

                val exception = shouldThrow<RuntimeException> {
                    runBlocking {
                        dataSource.getPageCo(2)
                    }
                }

                then("error is thrown ") {
                    exception.message shouldBe "Fake Exception"
                }
            }
        }
    }
})