package com.freeletics.tutorial.mvi

import io.kotlintest.specs.AbstractBehaviorSpec

/**
 * Just a kotlintest alias for `when`
 */
internal suspend fun AbstractBehaviorSpec.GivenContext.on(
    name: String,
    test: suspend AbstractBehaviorSpec.WhenContext.() -> Unit
) = `when`(name, test)

/**
 * Just a kotlintest alias for `when`
 */
internal suspend fun AbstractBehaviorSpec.GivenAndContext.on(
    name: String,
    test: suspend AbstractBehaviorSpec.WhenContext.() -> Unit
) = `when`(name, test)
