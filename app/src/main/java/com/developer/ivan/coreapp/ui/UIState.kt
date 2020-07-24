package com.developer.ivan.coreapp.ui

import com.developer.ivan.domain.Failure

sealed class UIState<out State> {

    class Empty<State> : UIState<State>()
    class Loading<State> : UIState<State>()
    class Error<State>(val failure: Failure) : UIState<State>()
    class Render<State>(val myState: State) : UIState<State>()
}