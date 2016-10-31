package com.robot.eecs541.eecs541robot;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<String> mArrayAdapter;
    private Set<BluetoothDevice> pd;
    private ListView mListView;
    public static final int REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mListView = (ListView)findViewById( R.id.listview1 );
        // Create the bluetooth adapter.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if bluetooth is enabled. If not, then enable it.
        if ( !mBluetoothAdapter.isEnabled() )
        {
            Intent enableBtIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
            startActivityForResult( enableBtIntent, REQUEST_ENABLE_BT );
        }
        else
        {
            //mBluetoothAdapter.startDiscovery();
        }

        pd = mBluetoothAdapter.getBondedDevices();
        ArrayList<String> idk = new ArrayList<String>();
        for( BluetoothDevice dev : pd )
        {
            idk.add( dev.getName() + "\n" + dev.getAddress() );
        }
        mArrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, idk );
        mListView.setAdapter( mArrayAdapter );

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( final AdapterView<?> parent, final View view, final int position, long id )
            {
                new AlertDialog.Builder( getApplicationContext() ).setTitle( "Confirm" ).setMessage("Use this device?")
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener()
                        {

                            public void onClick(DialogInterface dialog, int button )
                            {
                                String[] str = parent.getItemAtPosition( position ).toString().split("\n");
                                String s = str[1];
                                Intent i = new Intent( getApplicationContext(), MainActivity.class );
                                i.putExtra("addr", s);
                                setResult( 1, i );
                                finish();
                            }
                        })
                .setNegativeButton("no", null).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_ENABLE_BT)
        {
            if(resultCode == RESULT_OK)
            {
                //mBluetoothAdapter.startDiscovery();
            }
        }
    }
}
