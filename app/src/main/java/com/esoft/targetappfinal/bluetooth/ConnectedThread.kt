package com.esoft.targetappfinal.bluetooth

import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import android.os.Handler


class ConnectedThread(socket: BluetoothSocket): Thread() {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

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
        val buffer = ByteArray(256) // buffer store for the stream
        var bytes: Int // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = inputStream!!.read(buffer)
                val string = String(buffer)// Получаем кол-во байт и само собщение в байтовый массив "buffer"
                print(string)
               // handler.obtainMessage(rm, bytes, -1, buffer).sendToTarget() // Отправляем в очередь сообщений Handler
            } catch (e: IOException) {
                break
            }
        }
    }


}