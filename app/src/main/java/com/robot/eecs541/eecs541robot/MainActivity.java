package com.robot.eecs541.eecs541robot;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.provider.Settings.NameValueTable.NAME;

public class MainActivity extends Activity {

    protected static final int GET_BT_CONFIG = 1;
    protected static final int CONNECTION_STARTED = 789;
    protected BluetoothDevice mDevice;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private ConnectThread mConnectThread;
//    private AcceptThread mAcceptThread;
    private ConnectedThread mConnectedThread;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mRobot;
  //  int MESSAGE_READ = 987;
    boolean mConnected = false;
    private ImageView mLeftControl;
    boolean mMoving = false;


    private Handler mAcceptHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent bt = new Intent( getApplicationContext(), BluetoothActivity.class );
        startActivityForResult( bt, GET_BT_CONFIG );
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        TextView tv = (TextView)findViewById( R.id.connectLabel );
        tv.setText( "NOT CONNECTED :(" );

        Button up = (Button)findViewById( R.id.up_button );
        Button left = (Button)findViewById( R.id.left_button );
        Button right = (Button)findViewById( R.id.right_button );
        Button down = (Button)findViewById( R.id.down_button );
        Button stop = (Button)findViewById( R.id.stop_button );

        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mConnected && !mMoving) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                       // mMoving = true;
                        mConnectedThread.write(getWheelBytes(1));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mMoving = false;
                        mConnectedThread.write(getWheelBytes(0));
                    }
                    return true;
                }
                return false;
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mConnected && !mMoving) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //mMoving = true;
                        mConnectedThread.write(getWheelBytes(2));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mMoving = false;
                        mConnectedThread.write(getWheelBytes(0));
                    }
                    return true;
                }
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mConnected && !mMoving) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //mMoving = true;
                        mConnectedThread.write(getWheelBytes(3));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mMoving = false;
                        mConnectedThread.write(getWheelBytes(0));
                    }
                    return true;
                }
                return false;
            }
        });

        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mConnected && !mMoving) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //mMoving = true;
                        mConnectedThread.write(getWheelBytes(4));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mMoving = false;
                        mConnectedThread.write(getWheelBytes(0));
                    }
                    return true;
                }
                return false;
            }
        });

        stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mConnected && !mMoving) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //mMoving = true;
                        mConnectedThread.write(getWheelBytes(0));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mMoving = false;
                        mConnectedThread.write(getWheelBytes(0));
                    }
                    return true;
                }
                return false;
            }
        });

        /*mAcceptHandler = new Handler()
        {
            public void handleMessage( Message msg )
            {
                if( msg.what == MESSAGE_READ)
                {
                    TextView readData = (TextView)findViewById( R.id.read );
                    readData.setText( msg.toString() );
                }
            }
        };*/

        //mAcceptThread = new AcceptThread();
        //mAcceptThread.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if( requestCode == GET_BT_CONFIG )
        {
            if(resultCode == RESULT_OK)
            {
                mRobot = mBluetoothAdapter.getRemoteDevice( data.getStringExtra("addr") );
                mConnectThread = new ConnectThread( mRobot );
                mConnectThread.start();
                //mAcceptThread = new AcceptThread();
                //mAcceptThread.start();
            }
        }
    }

    private byte[] getWheelBytes( int aInt )
    {
        return new byte[] {
                (byte)( aInt >>> 24 ),
                (byte)( aInt >>> 16 ),
                (byte)( aInt >>> 8 ),
                (byte)aInt };
    }

    /*private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    mConnectedThread = new ConnectedThread( socket );
                    mConnectedThread.start();
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) { }
                    break;
                }
            }
        }


        // Will cancel the listening socket, and cause the thread to finish
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }*/

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
//            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            mConnectedThread = new ConnectedThread( mmSocket );
            mHandler.sendEmptyMessage( CONNECTION_STARTED );
            mConnectedThread.start();
        }

        // Will cancel an in-progress connection, and close the socket
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }

        private final Handler mHandler = new Handler()
        {
            public void handleMessage( Message msg )
            {
                if( msg.what == CONNECTION_STARTED )
                {
                    TextView devName = (TextView)findViewById( R.id.deviceName );
                    TextView devAddr = (TextView)findViewById( R.id.deviceAddress );
                    TextView connected = (TextView)findViewById( R.id.connectLabel );
                    devName.setText( mRobot.getName() );
                    devAddr.setText( mRobot.getAddress() );
                    connected.setText("Connected:");

                    mConnected = true;
                }
            }
        };
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
               /* while (true) {
                    try {
                        // Read from the InputStream
                        bytes = mmInStream.read(buffer);
                        // Send the obtained bytes to the UI activity
                        mAcceptHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget();
                    } catch (IOException e) {
                        break;
                    }
                }*/
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {}
        }


    }
}
