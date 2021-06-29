package com.esoft.targetappfinal.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esoft.targetappfinal.R
import com.esoft.targetappfinal.bluetooth.*
import com.esoft.targetappfinal.helper.StringHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    /*
    Используемые символы для работы с мишенью!
    Подключение и отправка данных происходит к главной мишени!
    Отправка настроек и принятия данных о батареи и попаданиях происходит по формату:
    "M1%21$22.. Mn%21$21"
    % - время в вертикальном положении
    $ - время в горизонтальном положении
    @ - запрос информации о попаданиях и батареи
    * - запуск мишени
    ; - остановка мишени
    # - сброс настроек

     */

    private lateinit var pref: SharedPreferences
    private lateinit var btConnect: BtConnection
    private lateinit var pbProg: ProgressDialog

    lateinit var handler: Handler
    val sb = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setSupportActionBar(toolbar)
        onClick()

        pbProg = ProgressDialog(this)
        pbProg.setCancelable(false)
        pbProg.setTitle(getString(R.string.text_connection))
        pbProg.setMessage(getString(R.string.text_please_wait))

        println(pref.getString(SETTINGS_KEY, ""))
        println(pref.getString(MAC_KEY, ""))

        runOnUiThread {
            handler = @SuppressLint("HandlerLeak")
            object : Handler() {
                @SuppressLint("SetTextI18n")
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        RECIEVE_MESSAGE -> {
                            val readBuf = msg.obj as ByteArray
                            val strIncom = String(readBuf, 0, msg.arg1)
                            sb.append(strIncom) // формируем строку
                            val endOfLineIndex = sb.indexOf("\r\n") // определяем символы конца строки
                            if (endOfLineIndex > 0) {                                            // если встречаем конец строки,
                                val sbprint = sb.substring(0, endOfLineIndex) // то извлекаем строку
                                sb.delete(0, sb.length) // и очищаем sb
                                Log.d("MYLOG", sbprint)
                                if(sbprint.isNotEmpty() && sbprint.length == 50) {
                                    runOnUiThread {
                                        hit_1.text = StringHelper().getInfoHits1(sbprint)
                                        btr_1.text = StringHelper().getInfoBtr1(sbprint)

                                        hit_2.text = StringHelper().getInfoHits2(sbprint)
                                        btr_2.text = StringHelper().getInfoBtr2(sbprint)

                                        hit_3.text = StringHelper().getInfoHits3(sbprint)
                                        btr_3.text = StringHelper().getInfoBtr3(sbprint)

                                        hit_4.text = StringHelper().getInfoHits4(sbprint)
                                        btr_4.text = StringHelper().getInfoBtr4(sbprint)

                                        hit_5.text = StringHelper().getInfoHits5(sbprint)
                                        btr_5.text = StringHelper().getInfoBtr5(sbprint)
                                    }

                                }

                            }
                        }
                        CONNECT_MESSAGE_COMPLITE -> {
                            tvStatus.visibility = View.VISIBLE
                            tvDevice.text = "${pref.getString(DEVICE_NAME_KEY, "")}"
                            tvStatus.text = "Подключено"
                            btn_connect.text = "Отключить"
                        }
                        DISCONNECT_MESSAGE -> {
                            tvStatus.visibility = View.GONE
                            tvDevice.text = getString(R.string.device_not_connected)
                            btn_connect.text = "Подключить"
                            clearView()
                        }

                    }
                }
            }
        }
    }

    private fun onClick() {

        btn_connect.setOnClickListener {
            btConnect.connect(pbProg, this, handler)
        }

        btn_hits.setOnClickListener {
            btConnect.sendMsg("@", this, handler)
        }

        btn_update.setOnClickListener {
            btConnect.sendMsg("#", this, handler)
        }

        btn_start.setOnClickListener {
            btConnect.sendMsg("*", this, handler)
        }

        btn_stop.setOnClickListener {
            btConnect.sendMsg(";", this, handler)
        }

        btn_send_settings.setOnClickListener {
            val getSettings = pref.getString(SETTINGS_KEY, "")
            println(getSettings)
            if (getSettings!!.isNotEmpty()) {
                btConnect.sendParam(getSettings, this, handler)
            }
        }

    }

    private fun init() {
        pref = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        btConnect = BtConnection(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tgSettings -> {
                val intent = Intent(this, TargetsSettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.ardConnect -> {
                val intent = Intent(this, BtConnectActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun clearView() {

        hit_1.text = ""
        btr_1.text = ""

        hit_2.text = ""
        btr_2.text = ""

        hit_3.text = ""
        btr_3.text = ""

        hit_4.text = ""
        btr_4.text = ""

        hit_5.text = ""
        btr_5.text = ""
    }


}