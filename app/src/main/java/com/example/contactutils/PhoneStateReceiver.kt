package com.example.contactutils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class PhoneStateReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val state: String = intent!!.getStringExtra(TelephonyManager.EXTRA_STATE).toString()


        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING) || state.equals(TelephonyManager.CALL_STATE_OFFHOOK)){

            val i :Intent = Intent(context, IncomingCallActivity::class.java)
            i.putExtras(intent)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            //Wait.oneSec()
            context!!.startActivity(i)

            Toast.makeText(context,"incoming call",Toast.LENGTH_LONG).show()
        }
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "Ringing",
            Toast.LENGTH_LONG).show()
    }
}

/*
class PhoneStateReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent?) {
        try{
            Log.e("1","incoming call")

        }catch (e :Exception){

        }
        val state: String = intent!!.getStringExtra(TelephonyManager.EXTRA_STATE).toString()
        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING) || state.equals(TelephonyManager.CALL_STATE_OFFHOOK)){

            val i :Intent = Intent(p0, IncomingCallActivity::class.java)
            i.putExtras(intent)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            //Wait.oneSec()
            p0!!.startActivity(i)

            Toast.makeText(p0,"incoming call",Toast.LENGTH_LONG).show()
        }

    }
}*/
