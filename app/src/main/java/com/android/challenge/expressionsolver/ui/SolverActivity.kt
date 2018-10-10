package com.android.challenge.expressionsolver.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.android.challenge.expressionsolver.R
import com.android.challenge.expressionsolver.viewmodel.SolverViewModel
import kotlinx.android.synthetic.main.solver_activity.*

class SolverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.solver_activity)

        val solverViewModel = ViewModelProviders.of(this).get(SolverViewModel::class.java)

        solverViewModel.evaluatedValue.observe(this, Observer {
            solution.text = it.toString()
        })

        solverViewModel.errorState.observe(this, Observer {
            error_message.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        expressionInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                solverViewModel.evaluateExpression(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

}