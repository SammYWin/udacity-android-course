package com.evantemplate.aboutme

import android.content.Context
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.evantemplate.aboutme.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val myName : MyName = MyName("Ivan Shepard")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.myName = myName

//        findViewById<Button>(R.id.btn_done).setOnClickListener {
//            addNickname(it)
//        }

        binding.btnDone.setOnClickListener {
            addNickname(it)
        }
    }

    private fun addNickname(view: View) {

        with(binding){
//            tvNickname.text = binding.etNickname.text
            myName?.nickname = etNickname.text.toString()
            tvNickname.visibility = View.VISIBLE
            etNickname.visibility = View.GONE
            btnDone.visibility = View.GONE
            invalidateAll()
        }

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }
}
