package com.abadzheva.readcontacts

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {
            v,
            insets,
            ->
            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat
                        .Type
                        .systemBars(),
                )
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom,
            )
            insets
        }

        // -----------------------------------------------
        val permissionGranted =
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_CONTACTS,
            ) == PackageManager.PERMISSION_GRANTED
        if (permissionGranted) {
            requestContacts()
        } else {
            Log.d("Contacts", "Permission denied")
        }
    }

    private fun requestContacts() {
        thread {
            val cursor =
                contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null,
                )
            while (cursor?.moveToNext() == true) {
                val id =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            Contacts._ID,
                        ),
                    )
                val name =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            Contacts.DISPLAY_NAME,
                        ),
                    )
                val contact = Contact(id, name)
                Log.d("Contacts", contact.toString())
            }
            cursor?.close()
        }
    }
}
