package com.esoft.targetappfinal.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esoft.targetappfinal.R
import com.esoft.targetappfinal.bluetooth.MY_PREF
import com.esoft.targetappfinal.bluetooth.SETTINGS_KEY
import kotlinx.android.synthetic.main.activity_targets_settings.*
import kotlinx.android.synthetic.main.activity_targets_settings.toolbar

class TargetsSettingsActivity : AppCompatActivity() {

    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_targets_settings)
        setSupportActionBar(toolbar)

        pref = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_onback);
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.title = "Параметры"

        btn_complite.setOnClickListener {
            savePref( settings =
                            "M1%" + checkParam(parA1.text.toString()) + "$" + checkParam(parN1.text.toString()) +
                            "M2%" + checkParam(parA2.text.toString()) + "$" + checkParam(parN2.text.toString()) +
                            "M3%" + checkParam(parA3.text.toString()) + "$" + checkParam(parN3.text.toString()) +
                            "M4%" + checkParam(parA4.text.toString()) + "$" + checkParam(parN4.text.toString()) +
                            "M5%" + checkParam(parA5.text.toString()) + "$" + checkParam(parN5.text.toString())
            )
            println(
                "M1%" + checkParam(parA1.text.toString()) + "$" + checkParam(parN1.text.toString()) +
                        "M2%" + checkParam(parA2.text.toString()) + "$" + checkParam(parN2.text.toString()) +
                        "M3%" + checkParam(parA3.text.toString()) + "$" + checkParam(parN3.text.toString()) +
                        "M4%" + checkParam(parA4.text.toString()) + "$" + checkParam(parN4.text.toString()) +
                        "M5%" + checkParam(parA5.text.toString()) + "$" + checkParam(parN5.text.toString())
            )
            onBackPressed()
        }

        btn_set_default.setOnClickListener {
            parA1.setText("5")
            parA2.setText("5")
            parA3.setText("5")
            parA4.setText("5")
            parA5.setText("5")
            parN1.setText("5")
            parN2.setText("5")
            parN3.setText("5")
            parN4.setText("5")
            parN5.setText("5")
        }
    }

    private fun checkParam(string: String): String {
        var result = ""
        if (string.length == 1)
            result ="00$string"
        else if(string.length == 2)
            result = "0$string"
        else if(string.length == 3)
            result = string

        return result
    }


    private fun savePref(settings: String) {
        val editor = pref.edit()
        editor.putString(SETTINGS_KEY, settings)
        editor.apply()
    }
}