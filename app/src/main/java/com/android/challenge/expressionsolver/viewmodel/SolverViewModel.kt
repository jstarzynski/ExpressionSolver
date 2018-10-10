package com.android.challenge.expressionsolver.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.challenge.expressionsolver.utils.ExpressionParser
import com.android.challenge.expressionsolver.utils.RpnEvaluator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SolverViewModel : ViewModel() {

    val evaluatedValue : MutableLiveData<Float> = MutableLiveData()
    val errorState : MutableLiveData<Boolean> = MutableLiveData()

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        evaluatedValue.postValue(0f)
        errorState.postValue(false)
    }

    fun evaluateExpression(expression: String) {
        executor.execute {
            try {
                evaluatedValue.postValue(RpnEvaluator.evaluate(ExpressionParser.convertInfixToRpn(ExpressionParser.splitExpression(expression))))
                errorState.postValue(false)
            } catch (e: Exception) {
                errorState.postValue(true)
            }
        }
    }

}