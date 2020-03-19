package com.tutosoftware.mano;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ManoControl extends AppCompatActivity {

    Button uno,dos,tres,cuatro,cinco,seis,abrir,cerrar,paz,ok,saludo,desconectar;
    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mano_control);


        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS);
        //receive the address of the bluetooth device

        uno = (Button) findViewById(R.id.uno);
        dos = (Button) findViewById(R.id.dos);
        tres = (Button) findViewById(R.id.tres);
        cuatro = (Button) findViewById(R.id.cuatro);
        cinco = (Button) findViewById(R.id.cinco);
        abrir = (Button) findViewById(R.id.abrir);
        cerrar = (Button) findViewById(R.id.cerrar);
        paz = (Button) findViewById(R.id.paz);
        ok = (Button) findViewById(R.id.ok);
        saludo = (Button) findViewById(R.id.saludo);
        desconectar = (Button) findViewById(R.id.des);


        new ConnectBT().execute();

        //commands to be sent to bluetooth
        uno.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("1");      //method to turn on
            }
        });
        dos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("2");      //method to turn on
            }
        });
        tres.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("3");      //method to turn on
            }
        });
        cuatro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("4");      //method to turn on
            }
        });
        cinco.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("5");      //method to turn on
            }
        });
        abrir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("a");      //method to turn on
            }
        });
        cerrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("b");      //method to turn on
            }
        });
        paz.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("c");      //method to turn on
            }
        });
        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("d");      //method to turn on
            }
        });
        saludo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mandarComando("e");      //method to turn on
            }
        });
        desconectar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });



    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void mandarComando(String comando)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(comando.getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }



    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ManoControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }



}
