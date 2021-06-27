package com.esoft.targetappfinal.adapter;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.esoft.targetappfinal.R;
import com.esoft.targetappfinal.bluetooth.BtConstKt;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BtListAdapter extends BaseAdapter {

    private static final int RESOURCE_LAYOUT = R.layout.list_item;

    private ArrayList<BluetoothDevice> bluetoothDevices;
    private LayoutInflater layoutInflater;
    private SharedPreferences pref;

    private List<ViewHolder> lisViewHolders;

    public BtListAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDevices) {
        this.bluetoothDevices = bluetoothDevices;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            viewHolder.btName = convertView.findViewById(R.id.tv_name);
            viewHolder.btAdress = convertView.findViewById(R.id.tv_address);
            viewHolder.chBtSelected = convertView.findViewById(R.id.chConnection);
            //viewHolder.bt_position = convertView.findViewById(R.id.tv_position);
            convertView.setTag(viewHolder);
            lisViewHolders.add(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(device != null){
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
            System.out.println(bluetoothDevices.get(i).getAddress());
        });

        if(pref.getString(BtConstKt.MAC_KEY, "no bt selected").equals(bluetoothDevices.get(i).getAddress())){
            viewHolder.chBtSelected.setChecked(true);
        }

       /* @SuppressLint("ViewHolder") View view = layoutInflater.inflate(RESOURCE_LAYOUT, viewGroup, false);
        BluetoothDevice device = bluetoothDevices.get(i);
        if(device != null){
            if(device.getName() == null) {
                ((TextView) view.findViewById(R.id.tv_name)).setText("Неизвестное устройство");
            }else {
                ((TextView) view.findViewById(R.id.tv_name)).setText(device.getName());
            }
            ((TextView) view.findViewById(R.id.tv_address)).setText(device.getAddress());
            lisViewHolders.add()
        }*/
        return convertView;
    }

    private void savePref(int position) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(BtConstKt.MAC_KEY, bluetoothDevices.get(position).getAddress());
        editor.apply();
    }

    static class ViewHolder {
        TextView bt_position;
        TextView btName;
        TextView btAdress;
        CheckBox chBtSelected;
    }

}
