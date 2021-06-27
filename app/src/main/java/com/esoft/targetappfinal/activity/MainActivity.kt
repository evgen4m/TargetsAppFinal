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
import androidx.appcompat.app.AppCompatActivity
import com.esoft.targetappfinal.R
import com.esoft.targetappfinal.bluetooth.*
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

        runOnUiThread {
            handler = @SuppressLint("HandlerLeak")
            object : Handler() {
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
                            }
                        }
                        CONNECT_MESSAGE_COMPLITE -> connectStatus.text = "Подключено"
                        DISCONNECT_MESSAGE -> connectStatus.text = "Отключено"

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

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun init() {
        pref = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        btConnect = BtConnection(this)
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


}