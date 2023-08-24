package com.rifqipadisiliwangi.sismartpju.view.penanganan.pekerjaan

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuperbaikan.PerbaikanRequestItem
import com.rifqipadisiliwangi.sismartpju.data.network.ApiClient
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityTambahPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity
import com.rifqipadisiliwangi.sismartpju.view.penanganan.spesifikasi.SpesifikasiActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

private const val TAG = "TambahPekerjaanActivity"
class TambahPekerjaanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTambahPekerjaanBinding

    var tkList = arrayOf("Selesai","Belum Selesai")
    var jkList = arrayOf("Perbaikan kabel jaringan kongslet","Perbaikan lampu mati", "Perbaikan installasi box", "Perbaikan MCB Tiang")

    private var imageView: ImageView? = null
    private var title: String = ""
    private var titlePju = ""
    private var titleDate = ""
    private var titleAdrress = ""
    private var kondisi = ""
    private var id = ""
    private var lat = ""
    private var lot = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPekerjaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestCamera()
        loadSpinerJenis()
        loadSpinerHasil()
        getBundle()
        perbaikanRequest()

        binding.btnSpesifikasi.setOnClickListener {
            toSpesifikasi()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun requestCamera(){
        binding.llUploadPekerjaan.setOnClickListener{
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }

        binding.llUploadSesudah.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 2)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }

        }

        binding.ivTrashImg.setOnClickListener {
            binding.imgUploadPekerjaan.isGone = true
            binding.ivTrashImg.isVisible = false
        }

        binding.ivTrashImgSesudah.setOnClickListener {
            binding.imgUploadPekerjaanSesudah.isGone = true
            binding.ivTrashImgSesudah.isVisible = false
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun perbaikanRequest(){

        binding.btnAdd.setOnClickListener {

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Logging in...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            GlobalScope.launch(Dispatchers.Main) {
                val idne = id.toInt()
                val tgl = 24
                val link = 25
                val usernya = 26
                val idpju = 27
                val idpelanggan = 28
                val tanggalperbaikan = 29
                val foto1 = 30
                val foto2 = 31
                val hasilperbaikan = 32
                val keteranganlainnya = 33
                val jenisperbaikan = 34

                val response = ApiClient.instance.pjuPerbaikan(PerbaikanRequestItem(idne, tgl, link, usernya, idpju, idpelanggan, tanggalperbaikan, foto1, foto2, hasilperbaikan, keteranganlainnya, jenisperbaikan))
                if (response.isSuccessful) {
                    progressDialog.dismiss() // Dismiss progress dialog
                    runOnUiThread {
                        val perbaikanResponse = response.body()
                        if (perbaikanResponse == null){
                            Toast.makeText(this@TambahPekerjaanActivity, "Gagal Menambahkan Hasil Pekerjaan", Toast.LENGTH_SHORT).show()
                        }else{
                            startActivity(Intent(this@TambahPekerjaanActivity, DashboardActivity::class.java))
                            Toast.makeText(this@TambahPekerjaanActivity, "Berhasil Menambahkan Hasil Pekerjaan", Toast.LENGTH_SHORT).show()
                            finish()
                            Log.d(TAG, perbaikanResponse.toString())
                        }
                    }
                } else {
                    // Login gagal
                    Log.d(TAG, "Response: ${response.errorBody()}")
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            var image = data!!.extras!!["data"] as Bitmap?
            val dimension = image!!.width.coerceAtMost(image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            binding.imgUploadPekerjaan.setImageBitmap(image)
            binding.imgUploadPekerjaan.isGone = false
            binding.ivTrashImg.isVisible = true
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            var image = data!!.extras!!["data"] as Bitmap?
            val dimension = image!!.width.coerceAtMost(image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            binding.imgUploadPekerjaanSesudah.setImageBitmap(image)
            binding.imgUploadPekerjaanSesudah.isGone = false
            binding.ivTrashImgSesudah.isVisible = true
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun imageViewToByte(image: ImageView): ByteArray? {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    private fun loadSpinerJenis(){

        var jkItem = ArrayAdapter(this, R.layout.spinner_right_aligned, jkList)
        jkItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(binding.spJenis)
        {
            adapter = jkItem
            setSelection(0, false)
            prompt = "Hasil Perbaikan"
            gravity = Gravity.CENTER

        }

        val ll = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        ll.setMargins(10, 40, 10, 10)

        jkItem = ArrayAdapter(this, R.layout.spinner_right_aligned, jkList)
        jkItem.setDropDownViewResource(R.layout.spinner_right_aligned)
    }

    private fun loadSpinerHasil(){

        var tkItem = ArrayAdapter(this, R.layout.spinner_right_aligned, tkList)
        tkItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        with(binding.spHasil)
        {
            adapter = tkItem
            setSelection(0, false)
            prompt = "Jenis Perbaikan"
            gravity = Gravity.CENTER

        }

        val ll = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        ll.setMargins(10, 40, 10, 10)

        tkItem = ArrayAdapter(this, R.layout.spinner_right_aligned, tkList)
        tkItem.setDropDownViewResource(R.layout.spinner_right_aligned)
    }
    private fun getBundle(){
        id = intent.extras?.getString("id") ?: "Tidak Terdeteksi"
        title = intent.extras?.getString("idpekerjaan") ?: "Tidak Terdeteksi"
        titlePju = intent.extras?.getString("idpju") ?: "Tidak Terdeteksi"
        titleDate = intent.extras?.getString("tgl") ?: "Tidak Terdeteksi"
        titleAdrress = intent.extras?.getString("alamat") ?: "Tidak Terdeteksi"
        kondisi = intent.extras?.getString("kondisi") ?: "Tidak Terdeteksi"
        lat = intent.extras?.getString("lat") ?: "Tidak Terdeteksi"
        lot = intent.extras?.getString("lot") ?: "Tidak Terdeteksi"
    }

    private fun toSpesifikasi(){
        val intent = Intent(this, SpesifikasiActivity::class.java)
        intent.putExtra("idpekerjaan",title)
        intent.putExtra("idpju",titlePju)
        intent.putExtra("tgl", titleDate)
        intent.putExtra("alamat",titleAdrress)
        intent.putExtra("kondisi",kondisi)
        intent.putExtra("lat", lat)
        intent.putExtra("lot",lot)
        this.startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }
}