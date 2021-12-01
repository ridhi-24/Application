package com.example.contactutils

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.recyclerview.widget.RecyclerView
import java.lang.StringBuilder
import android.R.id
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat.requestPermissions

import android.content.DialogInterface

import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {
    lateinit var list: RecyclerView
    private var contactlist: ArrayList<String>? = null
    var contactAdapter :ContactAdapter?=null

    lateinit var receiver: PhoneStateReceiver


    companion object{
        val PERMISSION_REQUEST_CONTACT =1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list = findViewById(R.id.list)
        receiver = PhoneStateReceiver()


        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) !=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE),1)

            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE),1)
            }
        }

        IntentFilter(Intent.ACTION_CALL).also {
            // registering the receiver
            // it parameter which is passed in  registerReceiver() function
            // is the intent filter that we have just created
            registerReceiver(receiver, it)
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list!!.setLayoutManager(layoutManager)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)

                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question

                    builder.setOnDismissListener {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_CONTACTS
                            ), PERMISSION_REQUEST_CONTACT
                        )
                    }

                    builder.show();

                } else {

                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_CONTACTS), 1)

                }
            }else{
            contactlist= loadcontactlist()
            }
        }
        else{
            contactlist=  loadcontactlist()
        }
        contactAdapter = ContactAdapter( contactlist!!,this)
        list!!.setAdapter(contactAdapter)
        contactAdapter!!.notifyDataSetChanged()

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.READ_CONTACTS) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            2->{
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.READ_PHONE_STATE) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    @SuppressLint("Range")
    fun loadcontactlist(): ArrayList<String>? {
        val nameList: ArrayList<String> = ArrayList()

        val cr: ContentResolver = getContentResolver()
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if (cur!!.getCount() > 0) {
            while (cur.moveToNext()) {
                val id = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts._ID
                    )
                )

                val name = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                nameList.add(name)

                if (Integer.parseInt(
                        cur.getString(
                            cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                            )
                        )
                    ) > 0
                ) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {

                        val phoneNumber = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        nameList.add(phoneNumber)

                    }
                    pCur.close();
                }
            }
        }
        return nameList


    }
}