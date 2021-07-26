package com.esoft.targetappfinal.activity

import android.Manifest
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.esoft.targetappfinal.R
import com.esoft.targetappfinal.adapter.BtListAdapter
import kotlinx.android.synthetic.main.activity_bt_connect.*
import java.util.*

class BtConnectActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var btAdapter: BluetoothAdapter
    private lateinit var devices: ArrayList<BluetoothDevice>
    private lateinit var listAdapter: BtListAdapter

    private val REQ_ENABLE_BT = 10
    private val BT_BOUNDED = 21
    private val BT_SEARCH = 22
    private val REQUEST_CODE_LOC = 1

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bt_connect)

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_onback);
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.title = "Подключение"

        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        switch_enable_bt.setOnCheckedChangeListener(this)

        devices = ArrayList()

        btAdapter = BluetoothAdapter.getDefaultAdapter()

        if(btAdapter.isEnabled) {
            showConnectToArd()
            switch_enable_bt.isChecked = true
            setListAdapter(BT_BOUNDED)
        }

        btn_enable_search.setOnClickListener {
            enableSearch()
        }

    }

    private fun enableBt(flag: Boolean) {
        if (flag) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, REQ_ENABLE_BT)
        } else {
            btAdapter.disable()
        }
    }

    private fun showConnectToArd() {
        frame_noConnect.visibility = View.GONE
        frame_getDevices.visibility = View.VISIBLE
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if(buttonView!! == switch_enable_bt) {
            enableBt(isChecked)
            if (!isChecked) {
                showMessage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_ENABLE_BT) {
            if (resultCode == RESULT_OK && btAdapter.isEnabled) {
                showConnectToArd()
                setListAdapter(BT_BOUNDED)
            } else if (resultCode == RESULT_CANCELED) {
                enableBt(true)
            }
        }
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    btn_enable_search.setText(R.string.stop_search)
                    pb_progress.visibility = View.VISIBLE
                    setListAdapter(BT_SEARCH)
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    btn_enable_search.setText(R.string.enable_search)
                    pb_progress.visibility = View.GONE
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null && !devices.contains(device)) {
                        devices.add(device)
                        listAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun getBoundedDevice(): ArrayList<BluetoothDevice> {
        val deviceSet: Set<BluetoothDevice> = btAdapter.bondedDevices
        val tmpArrayList = ArrayList<BluetoothDevice>()
        if (deviceSet.isNotEmpty()) {
            for (device in deviceSet) {
                tmpArrayList.add(device)
            }
        }
        return tmpArrayList
    }

    private fun setListAdapter(type: Int) {
        devices.clear()
        when (type) {
            BT_BOUNDED -> {
                devices = getBoundedDevice()
            }
        }
        listAdapter = BtListAdapter(this, devices)
        tv_bt_device.adapter = listAdapter
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun enableSearch() {
        if (btAdapter.isDiscovering) {
            btAdapter.cancelDiscovery()
        } else {
            accessLocationPermission()
            btAdapter.startDiscovery()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun accessLocationPermission() {
        val accessCL = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        val accessFL = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val accessBL = checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        val listRequestPermission: MutableList<String> = ArrayList()
        if (accessCL != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if(accessBL != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        if (accessFL != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (listRequestPermission.isNotEmpty()) {
            val strRP = listRequestPermission.toTypedArray()
            requestPermissions(strRP, REQUEST_CODE_LOC)
        }
    }

    private fun showMessage() {
        frame_noConnect.visibility = View.VISIBLE
        frame_getDevices.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bt_connect_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_bt_connect -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        btAdapter.cancelDiscovery()
    }
}