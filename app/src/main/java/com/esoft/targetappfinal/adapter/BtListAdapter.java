package com.esoft.targetappfinal.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.esoft.targetappfinal.R;
import com.esoft.targetappfinal.bluetooth.BtConstKt;

import java.util.ArrayList;
import java.util.List;

public class BtListAdapter extends BaseAdapter {

    private static final int RESOURCE_LAYOUT = R.layout.list_item;

    private ArrayList<BluetoothDevice> bluetoothDevices;
    private SharedPreferences pref;

    private List<ViewHolder> lisViewHolders;

    public BtListAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDevices) {
        this.bluetoothDevices = bluetoothDevices;
        lisViewHolders = new ArrayList<>();
        pref = context.getSharedPreferences(BtConstKt.MY_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return bluetoothDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        BluetoothDevice device = bluetoothDevices.get(i);

        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(RESOURCE_LAYOUT, null, false);
            convertView.setTag(viewHolder);
            lisViewHolders.add(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btName = convertView.findViewById(R.id.tv_name);
        viewHolder.btAdress = convertView.findViewById(R.id.tv_address);
        viewHolder.chBtSelected = convertView.findViewById(R.id.chConnection);

        if(bluetoothDevices != null){
            if(device.getName() == null) {
                viewHolder.btName.setText("Неизвестное устройство!");
            }else {
                viewHolder.btName.setText(device.getName());
            }
            viewHolder.btAdress.setText(device.getAddress());
        }

        viewHolder.chBtSelected.setOnClickListener(v -> {
            for(ViewHolder holder: lisViewHolders) {
                holder.chBtSelected.setChecked(false);
            }
            viewHolder.chBtSelected.setChecked(true);
            savePref(i);
            System.out.println(device.getAddress());
        });

        if(pref.getString(BtConstKt.MAC_KEY, "no bt selected").equals(device.getAddress())){
            viewHolder.chBtSelected.setChecked(true);
        }

        return convertView;
    }

    private void savePref(int position) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BtConstKt.MAC_KEY, bluetoothDevices.get(position).getAddress());
        editor.putString(BtConstKt.DEVICE_NAME_KEY, bluetoothDevices.get(position).getName());
        editor.apply();
    }


    static class ViewHolder {
        TextView bt_position;
        TextView btName;
        TextView btAdress;
        CheckBox chBtSelected;
    }

}
