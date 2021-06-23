package com.esoft.targetappfinal.bluetooth

import android.app.AlertDialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import android.widget.Toast
import java.io.IOException
import java.lang.Exception

class BtConnection
{
    private var context: Context
    private var pref: SharedPreferences
    private var btAdapter: BluetoothAdapter
    private lateinit var btDevice: BluetoothDevice
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null



    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        btAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun connect(pbProgress: ProgressDialog, context: Context) {
        val macAdress = pref.getString(MAC_KEY, "")
        if(!btAdapter.isEnabled || macAdress!!.isEmpty()) return
        btDevice = btAdapter.getRemoteDevice(macAdress)
        if (btDevice == null) return

        connectThread = ConnectThread(context, btAdapter, btDevice, pbProgress)
        connectThread!!.start()
    }

    fun sendMsg(msg: String, context: Context) {
        try {
            connectedThread = ConnectedThread(connectThread!!.btSocket)
            connectedThread!!.write(msg)
        }catch (e: Exception) {
            Log.d(BT_CON_TAG, e.printStackTrace().toString())
            Toast.makeText(context, "Устройство не подключено!", Toast.LENGTH_SHORT).show()
        }

    }

}