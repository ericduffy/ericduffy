package com.ericduffy.raybanautomation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log

class CallManager(private val context: Context) {

    companion object {
        private const val TAG = "CallManager"
        private const val WHATSAPP_VIDEOCALL_MIME = "vnd.android.cursor.item/vnd.com.whatsapp.video.call"
        private const val WHATSAPP_VOIP_CALL_MIME = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call"
        // TODO: User needs to update this number
        private const val GATE_NUMBER = "0000000000" 
        private const val WIFE_NAME = "Jashneet Duffy"
    }

    fun callGate() {
        Log.d(TAG, "Initiating call to Gate")
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$GATE_NUMBER")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied for calling phone", e)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to call Gate", e)
        }
    }

    fun callWifeWhatsApp() {
        Log.d(TAG, "Initiating WhatsApp call to $WIFE_NAME")
        val whatsappId = getWhatsAppContactId(WIFE_NAME)
        
        if (whatsappId != null) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(
                    Uri.parse("content://com.android.contacts/data/$whatsappId"),
                    WHATSAPP_VOIP_CALL_MIME
                )
                setPackage("com.whatsapp")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start WhatsApp call", e)
            }
        } else {
            Log.e(TAG, "Could not find WhatsApp contact: $WIFE_NAME")
            // Fallback or Toast could take place here but avoiding UI in logic class for now
        }
    }

    private fun getWhatsAppContactId(displayName: String): Long? {
        val resolver = context.contentResolver
        val cursor = resolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.Data._ID),
            "${ContactsContract.Data.DISPLAY_NAME} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(displayName, WHATSAPP_VOIP_CALL_MIME),
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex(ContactsContract.Data._ID)
                if (idIndex != -1) {
                    return it.getLong(idIndex)
                }
            }
        }
        return null
    }
}
