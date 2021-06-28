package com.esoft.targetappfinal.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esoft.targetappfinal.R
import kotlinx.android.synthetic.main.activity_bt_connect.*
import kotlinx.android.synthetic.main.activity_targets_settings.*
import kotlinx.android.synthetic.main.activity_targets_settings.toolbar

class TargetsSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_targets_settings)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_onback);
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        btn_complite.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("parM1", "M1%" + parA1.text + "$" + parN1.text)
            intent.putExtra("parM2", "M2%" + parA2.text + "$" + parN2.text)
            intent.putExtra("parM3", "M3%" + parA3.text + "$" + parN3.text)
            intent.putExtra("parM4", "M4%" + parA4.text + "$" + parN4.text)
            intent.putExtra("parM5", "M5%" + parA5.text + "$" + parN5.text)
            startActivity(intent)
            finish()
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
}