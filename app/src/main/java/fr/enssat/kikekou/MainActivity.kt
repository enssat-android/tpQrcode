package fr.enssat.kikekou

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.glxn.qrgen.android.QRCode
import java.lang.Exception
import android.provider.MediaStore
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.view.View
import android.content.Intent




class MainActivity : AppCompatActivity() {

    private val NO_COMPRESSION = 100
    private val QR_CODE_FILE_NAME = "myqrcode"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        var button =
            this.findViewById(R.id.bt_generate_qr_code) as androidx.appcompat.widget.AppCompatButton
        var qrCodeView =
            this.findViewById(R.id.qr_code_view) as androidx.appcompat.widget.AppCompatImageView
        var json = " { id: string\n" +
                " name: string\n" +
                " photo: Url?\n" +
                " contact: [ \n" +
                "     { key:  \"email\" , value : string },\n" +
                "     { key: \"tel\", value: string },\n" +
                "     { key: \"face de  book\", value: string},\n" +
                "  ]\n" +
                " week : 43" +
                "  loc: [ \n" +
                "     { day: 1, value: \"teletravail\" },\n" +
                "     { day : 3, value: \"Off\" },\n" +
                "     { day : 5: value: \"WF 036\" } \n" +
                "  ]\n" +
                "}"
        var share =
            this.findViewById(R.id.share) as com.google.android.material.floatingactionbutton.FloatingActionButton

        var uri : Uri? = null
        button.setOnClickListener { _ ->
            val bitmap: Bitmap = QRCode.from(json).bitmap()
            qrCodeView.setImageBitmap(bitmap)

            try {
                //in your android manifest
                //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
                //to access media storage for android <10

                val values = ContentValues()
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, QR_CODE_FILE_NAME)
                values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                values.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOCUMENTS.toString() + "/Kikeou/"
                )

                uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), values)
                val out = uri?.let {
                    contentResolver.openOutputStream(it)
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, NO_COMPRESSION, out)
                out?.flush()
                out?.close()
                share.visibility = View.VISIBLE

                //to get png in Android Studio : View -> Tool Windows -> Device File Explorer
                //in storage/self/primary/Documents/Kikeou
                //also in sdcard/Documents/Kikeou
                //synchronize Documents folder if needed
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        share.setOnClickListener { _ ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Share Image"))
        }
    }
}