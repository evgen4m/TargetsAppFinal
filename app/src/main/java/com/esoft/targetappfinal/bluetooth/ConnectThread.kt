package com.esoft.targetappfinal.bluetooth

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.IOException
import java.lang.reflect.Method

class ConnectThread(context: Context, btAdapter: BluetoothAdapter, btDevice: BluetoothDevice, pbProgress: ProgressDialog, handler: Handler): Thread() {

    var context: Context
    var btAdapter: BluetoothAdapter
    var btDevice: BluetoothDevice
    lateinit var btSocket: BluetoothSocket
    var progressDialog: ProgressDialog
    var succes = false
    lateinit var connectedThread: ConnectedThread
    private val handler: Handler
    val message = Message.obtain()

    init {
        this.context = context
        this.btAdapter = btAdapter
        this.btDevice = btDevice
        this.progressDialog = pbProgress
        this.handler = handler

        try {
            //btSocket = btDevice.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID))
            val method: Method = btDevice.javaClass.getMethod(
                "createRfcommSocket", *arrayOf<Class<*>?>(
                    Int::class.javaPrimitiveType
                )
            )
            btSocket = method.invoke(btDevice, 1) as BluetoothSocket
            progressDialog.show()
        }catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        btAdapter.cancelDiscovery()
        try {
            btSocket.connect()
            succes = true
            progressDialog.dismiss()
            message.what = CONNECT_MESSAGE_COMPLITE
            handler.sendMessage(message)
            Log.d("MYLOG", "CONNECTED")
        }catch (e: IOException){
            Log.d("MYLOG", "NOT CONNECTED")
            e.printStackTrace()
            closeConnection()
        }

        if (succes) {
            Log.d("MYLOG", "Соединение установлено")
            connectedThread = ConnectedThread(btSocket, handler)
            connectedThread.start()
        }
    }

    fun closeConnection() {
        try {
            btSocket.close()
            progressDialog.dismiss()
            message.what = DISCONNECT_MESSAGE
            handler.sendMessage(message)
        }catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
