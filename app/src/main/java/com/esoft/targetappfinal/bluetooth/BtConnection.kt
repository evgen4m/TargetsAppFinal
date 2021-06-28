package com.esoft.targetappfinal.bluetooth

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
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

    fun connect(pbProgress: ProgressDialog, context: Context, handler: Handler) {
        val macAdress = pref.getString(MAC_KEY, "")
        if(!btAdapter.isEnabled || macAdress!!.isEmpty()) {
            val message = Message.obtain()
            message.what = DISCONNECT_MESSAGE
            handler.sendMessage(message)
            return
        }
        btDevice = btAdapter.getRemoteDevice(macAdress)
        if (btDevice == null) {
            val message = Message.obtain()
            message.what = DISCONNECT_MESSAGE
            handler.sendMessage(message)
            return
        }

        connectThread = ConnectThread(context, btAdapter, btDevice, pbProgress, handler)
        connectThread!!.start()
    }

    fun sendMsg(msg: String, context: Context, handler: Handler) {
        try {
            connectedThread = ConnectedThread(connectThread!!.btSocket, handler)
            connectedThread!!.write(msg)
        }catch (e: Exception) {
            Log.d(BT_CON_TAG, e.printStackTrace().toString())
            val message = Message.obtain()
            message.what = DISCONNECT_MESSAGE
            handler.sendMessage(message)
            Toast.makeText(context, "Устройство не подключено!", Toast.LENGTH_SHORT).show()
        }
    }

}