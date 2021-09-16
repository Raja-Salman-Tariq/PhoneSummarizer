package com.example.a4v4.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import com.example.a4v4.database.ContactsModel
import com.example.a4v4.database.MyFiles
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.sql.Timestamp


class FileHandler(private val ctxt: Context) {

    var myFile  :   MyFiles?    =   null
    private var myTs    =   Timestamp((System.currentTimeMillis())).toString()

    init{
        try{
            fos =ctxt.openFileOutput(FILE_NAME +"_${myTs}", MODE_PRIVATE)
        }catch (e:  Exception){
            fos =   null
            e.printStackTrace()
        }
    }

    companion object{
        private const val FILE_NAME    =   "contacts.csv"

        var fos: FileOutputStream? = null

        fun closeFos(){
            if (fos != null) {
                try {
                    fos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun createCSV(contacts : List<ContactsModel>?): FileHandler {

        if (contacts == null) {
            return this
        }

        if (fos == null) {
//            Log.d("timestamp", "createC    ${Timestamp((System.currentTimeMillis()))} SV: ")
            myTs    =   Timestamp((System.currentTimeMillis())).toString()
            fos = ctxt.openFileOutput(FILE_NAME +"_${myTs}", MODE_PRIVATE)
        }

        try {

            // Successfully writting to file
            fos?.write("Index, Name, Number, Type\n".toByteArray())
            for (contact in contacts) {
                fos?.write(contact.toString().toByteArray())
                //Log.d("myfilesdirx", "successfully wrote: $contact")
            }

            myFile  =   MyFiles(FILE_NAME+"_$myTs", myTs)

            // Sharing via android share sheet
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ctxt, "com.example.a4v4..fileprovider", ctxt.getFileStreamPath(
                    FILE_NAME+"_$myTs"
                )))
                type = "text/csv"
            }
            ctxt.startActivity(Intent.createChooser(shareIntent, "Send through..."))

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return this
    }
}