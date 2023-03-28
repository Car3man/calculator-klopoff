package com.klopoffaa.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.klopoffaa.calculator.core.Calculator
import com.klopoffaa.calculator.core.Operation
import com.klopoffaa.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val calculator: Calculator = Calculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonClear.setOnClickListener { calculator.clear(); }
        binding.buttonChangeSign.setOnClickListener { calculator.sign(); }
        binding.buttonPercentage.setOnClickListener { calculator.percent(); }
        binding.buttonDivide.setOnClickListener { calculator.operation(Operation.DIVIDE); }
        binding.buttonMultiply.setOnClickListener { calculator.operation(Operation.MULTIPLY); }
        binding.buttonSubtract.setOnClickListener { calculator.operation(Operation.SUBTRACT); }
        binding.buttonAdd.setOnClickListener { calculator.operation(Operation.ADD); }
        binding.buttonDelimiter.setOnClickListener { calculator.delimiter(); }
        binding.buttonCalculate.setOnClickListener { calculator.calculate(); }

        binding.button0.setOnClickListener { calculator.number(0); }
        binding.button1.setOnClickListener { calculator.number(1); }
        binding.button2.setOnClickListener { calculator.number(2); }
        binding.button3.setOnClickListener { calculator.number(3); }
        binding.button4.setOnClickListener { calculator.number(4); }
        binding.button5.setOnClickListener { calculator.number(5); }
        binding.button6.setOnClickListener { calculator.number(6); }
        binding.button7.setOnClickListener { calculator.number(7); }
        binding.button8.setOnClickListener { calculator.number(8); }
        binding.button9.setOnClickListener { calculator.number(9); }

        calculator.setStateUpdateListener { onCalculatorStateUpdate() }
        onCalculatorStateUpdate()
    }

    private fun onCalculatorStateUpdate() {
        updateClearButton()
        updateResultText()
    }

    private fun updateClearButton() {
        binding.buttonClear.text = if (!calculator.isCurrNumberIsFirstOperand() || calculator.getCurrNumber() != 0.0) {
            resources.getString(R.string.button_clear)
        } else {
            resources.getString(R.string.button_all_clear)
        }
    }

    private fun updateResultText() {
        binding.calculationResultText.text = formatNumberToDisplay(calculator.getCurrNumber())
    }

    private fun formatNumberToDisplay(result: Double): String {
        if (calculator.hasCurrNumberDelimiter()) {
            return result.toString()
        }
        return result.toString().replace(".0", "")
    }
}