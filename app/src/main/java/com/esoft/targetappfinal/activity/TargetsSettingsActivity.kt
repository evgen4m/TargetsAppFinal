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

        btn_complite.setOnClickListener {
            savePref("M1%" + parA1.text + "$" + parN1.text + "M2%" + parA2.text + "$" + parN2.text
            + "M3%" + parA3.text + "$" + parN3.text +"M4%" + parA4.text + "$" + parN4.text
            + "M5%" + parA5.text + "$" + parN5.text)
            println("M1%" + parA1.text + "$" + parN1.text + "M2%" + parA2.text + "$" + parN2.text
                    + "M3%" + parA3.text + "$" + parN3.text +"M4%" + parA4.text + "$" + parN4.text
                    + "M5%" + parA5.text + "$" + parN5.text)
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


    private fun savePref(settings: String) {
        val editor = pref.edit()
        editor.putString(SETTINGS_KEY, settings)
        editor.apply()
    }
}