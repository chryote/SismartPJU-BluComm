package com.rifqipadisiliwangi.sismartpju.view.penanganan.pekerjaan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.ThumbnailUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.rifqipadisiliwangi.sismartpju.R
import com.rifqipadisiliwangi.sismartpju.databinding.ActivityTambahPekerjaanBinding
import com.rifqipadisiliwangi.sismartpju.view.detail.DetailPekerjaanActivity
import com.rifqipadisiliwangi.sismartpju.view.home.DashboardActivity
import com.rifqipadisiliwangi.sismartpju.view.penanganan.spesifikasi.SpesifikasiActivity
import java.io.ByteArrayOutputStream

class TambahPekerjaanActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTambahPekerjaanBinding

    var jkList = arrayOf("primer","sekunder", "tersier")
    var tkList = arrayOf("ringan","sedang", "rusak berat")

    private var imageView: ImageView? = null

    private val RESULT_LOAD_IMAGE = 123
    private val REQUEST_CODE_GALLERY = 999

    private var imagePostPekerjaan: Bitmap? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPekerjaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestCamera()
        loadSpinerJenis()
        loadSpinerHasil()

        binding.btnSpesifikasi.setOnClickListener {
            startActivity(Intent(this, SpesifikasiActivity::class.java))
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
            finishAndRemoveTask()
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
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

    override fun onBackPressed() {
        super.onBackPressed()
    }
}