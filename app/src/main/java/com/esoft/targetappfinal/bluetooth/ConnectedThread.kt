package com.esoft.targetappfinal.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import java.io.*
import java.util.*


class ConnectedThread(socket: BluetoothSocket,handler: Handler) : Thread() {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    var isConnected = false
    var handler: Handler

    init {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            inputStream = socket.inputStream
            outputStream = socket.outputStream
        } catch (e: IOException) {
            e.printStackTrace()
        }

        this.inputStream = inputStream
        this.outputStream = outputStream
        isConnected = true
        this.handler = handler
    }

    fun write(command: String) {
        val bytes = command.toByteArray()
        if (outputStream != null) {
            try {
                outputStream!!.write(bytes)
                outputStream!!.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun run() {
        Log.d("MYLOG", "Запуск потока приема данных")
        val buffer = ByteArray(1024) // buffer store for the stream

        var bytes: Int // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = inputStream!!.read(buffer) // Получаем кол-во байт и само собщение в байтовый массив "buffer"
                handler.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget() // Отправляем в очередь сообщений Handler
            } catch (e: IOException) {
                break
            }
        }


    }


}