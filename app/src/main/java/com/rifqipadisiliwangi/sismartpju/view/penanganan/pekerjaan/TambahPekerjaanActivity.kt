package com.rifqipadisiliwangi.sismartpju.view.penanganan.pekerjaan

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.os.Environment
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
import com.rifqipadisiliwangi.sismartpju.data.model.pekerjaan.pjuperbaikan.ResponsePicturePerbaikan
import com.rifqipadisiliwangi.sismartpju.data.network.ApiClient
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityTambahPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity
import com.rifqipadisiliwangi.sismartpju.view.penanganan.spesifikasi.SpesifikasiActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "TambahPekerjaanActivity"
class TambahPekerjaanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTambahPekerjaanBinding
    private var imagePost: Bitmap? = null
    private var imagePostAfter: Bitmap? = null
    private lateinit var progressDialog : ProgressDialog

    var tkList = arrayOf("Selesai","Belum Selesai")
    var jkList = arrayOf("Perbaikan kabel jaringan kongslet","Perbaikan lampu mati", "Perbaikan installasi box", "Perbaikan MCB Tiang")

    private var titleDate = ""
    private var titleCurrentDate = ""
    private var titleAdrress = ""
    private var kondisi = ""
    private var idne = ""
    private var idPju = ""
    private var idPelanggan = ""
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
        postPerbaikan()
        binding.btnSpesifikasi.setOnClickListener {
            toSpesifikasi()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }
    private fun postPerbaikan(){

        binding.btnAdd.setOnClickListener {

            val currentDate = getCurrentDate("yyyy-MM-dd HH:mm:ss")
            titleCurrentDate  = currentDate
            titleDate = currentDate

            val requestBody: MutableMap<String, RequestBody> = mutableMapOf(
                "idne" to createPartFromString(idne),
                "tgl" to createPartFromString(titleDate),
                "link" to createPartFromString(""),
                "usernya" to createPartFromString(""),
                "idpju" to createPartFromString(idPju),
                "idpelanggan" to createPartFromString(idPelanggan),
                "tanggalperbaikan" to createPartFromString(titleCurrentDate),
                "hasilperbaikan" to createPartFromString(binding.spHasil.selectedItem.toString()),
                "keteranganlainnya" to createPartFromString( binding.etNote.text.toString().trim()),
                "jenisperbaikan" to createPartFromString( binding.spJenis.selectedItem.toString()),
            )


            val requestImage: RequestBody?
            val imagePart: MultipartBody.Part?

            if (imagePost != null) {
                // The user has selected an image
                val imageFile = convertTempFile(imagePost)
//            requestImage = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile!!)
                Log.d(TAG, "image-file : $imageFile")
                val mimeType = "image/${imageFile!!.extension}"
                requestImage = RequestBody.create(mimeType.toMediaTypeOrNull(), imageFile)
                imagePart = MultipartBody.Part.createFormData("foto1", imageFile.name, requestImage)
            } else {
                // The user has not selected an image
                requestImage = null
                imagePart = null
            }

            Log.d(TAG, binding.imgUploadPekerjaan.toString())
            Log.d(TAG, "imagePart $imagePart")

            val requestImageAfter: RequestBody?
            val imagePartAfter: MultipartBody.Part?

            if (imagePostAfter != null) {
                // The user has selected an image
                val imageFile = convertTempFile(imagePostAfter)
                Log.d(TAG, "image-file : $imageFile")
                val mimeType = "image/${imageFile!!.extension}"
                requestImageAfter = RequestBody.create(mimeType.toMediaTypeOrNull(), imageFile)
                imagePartAfter = MultipartBody.Part.createFormData("foto2", imageFile.name, requestImageAfter)
            } else {
                // The user has not selected an image
                requestImageAfter = null
                imagePartAfter = null
            }

            Log.d(TAG, binding.imgUploadPekerjaanSesudah.toString())
            Log.d(TAG, "imagePart $imagePartAfter")


            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Loading...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            ApiClient.instance.addPhoto(requestBody, imagePart,imagePartAfter)
                .enqueue(object : Callback<ResponsePicturePerbaikan> {
                    override fun onResponse(
                        call: Call<ResponsePicturePerbaikan>,
                        response: Response<ResponsePicturePerbaikan>
                    ) {
                        progressDialog.dismiss()
                        if (response.isSuccessful) {
                            val addPjuResponse = response.body()
                            Log.d(TAG,"Response : $addPjuResponse" )
                            addPjuResponse?.let {
                                val userData = it.msg
                                Toast.makeText(
                                    this@TambahPekerjaanActivity,
                                    "Berhasil menambahkan laporan perbaikan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBackPressed()
                            }
                            startActivity(Intent(this@TambahPekerjaanActivity, DashboardActivity::class.java))
                            finish()
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@TambahPekerjaanActivity,
                                    "Gagal menambahkan laporan perbaikan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponsePicturePerbaikan>, t: Throwable) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@TambahPekerjaanActivity,
                            "Gagal menambahkan laporan irigasi",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("UpdateProfileError", t.toString())
                    }
                })
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            imagePost = data!!.extras!!["data"] as Bitmap?
            val dimension = imagePost!!.width.coerceAtMost(imagePost!!.height)
            imagePost = ThumbnailUtils.extractThumbnail(imagePost, dimension, dimension)
            binding.imgUploadPekerjaan.setImageBitmap(imagePost)
            Log.d(TAG, "imagePart ${binding.imgUploadPekerjaan}")
            binding.imgUploadPekerjaan.isGone = false
            binding.ivTrashImg.isVisible = true
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            imagePostAfter = data!!.extras!!["data"] as Bitmap?
            val dimension = imagePostAfter!!.width.coerceAtMost(imagePostAfter!!.height)
            imagePostAfter = ThumbnailUtils.extractThumbnail(imagePostAfter, dimension, dimension)
            binding.imgUploadPekerjaanSesudah.setImageBitmap(imagePostAfter)
            binding.imgUploadPekerjaanSesudah.isGone = false
            binding.ivTrashImgSesudah.isVisible = true
        }
        super.onActivityResult(requestCode, resultCode, data)
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
    private fun convertTempFile(bitmap: Bitmap?): File? {
        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_image.png"
        )
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val bitmapData = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
    private fun getBundle(){
        idne = intent.extras?.getString("id") ?: "Tidak Terdeteksi"
        idPelanggan = intent.extras?.getString("idpekerjaan") ?: "Tidak Terdeteksi"
        idPju = intent.extras?.getString("idpju") ?: "Tidak Terdeteksi"
        titleDate = intent.extras?.getString("tgl") ?: "Tidak Terdeteksi"
        titleAdrress = intent.extras?.getString("alamat") ?: "Tidak Terdeteksi"
        kondisi = intent.extras?.getString("kondisi") ?: "Tidak Terdeteksi"
        lat = intent.extras?.getString("lat") ?: "Tidak Terdeteksi"
        lot = intent.extras?.getString("lot") ?: "Tidak Terdeteksi"
    }

    private fun toSpesifikasi(){
        val intent = Intent(this, SpesifikasiActivity::class.java)
        intent.putExtra("idpekerjaan",idPelanggan)
        intent.putExtra("idpju",idPju)
        intent.putExtra("tgl", titleDate)
        intent.putExtra("alamat",titleAdrress)
        intent.putExtra("kondisi",kondisi)
        intent.putExtra("lat", lat)
        intent.putExtra("lot",lot)
        this.startActivity(intent)
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(
            MultipartBody.FORM, descriptionString
        )
    }

    private fun getCurrentDate(format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }
}