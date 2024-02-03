package com.rifqipadisiliwangi.sismartpju.view.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.rifqipadisiliwangi.sismartpju.databinding.FragmentPekerjaanBinding

import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.core.content.ContextCompat
import android.widget.Button
import com.rifqipadisiliwangi.sismartpju.R
import android.util.TypedValue
import androidx.cardview.widget.CardView

class PekerjaanFragment : Fragment() {

    private lateinit var binding: FragmentPekerjaanBinding
    // Assuming you have the JSON data as a list of integers, e.g., statusLampu.
    val statusLampu = listOf(1, 0, 0, 0)
    val streetNames = listOf("Jl. HM Subchan ZE", "Jl. Magelang Km. 10", "Jl. Parasamya", "Jl. Sidomoyo")
    val pekerjaanSelesai = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("PekerjaanFragment.kt", "Log message from LogHandler")
        binding = FragmentPekerjaanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvBelumSelesai = binding.tvBelumSelesai
        val tvSelesai = binding.tvSelesai

        tvBelumSelesai.setOnClickListener {
            generateCards()
        }

        tvSelesai.setOnClickListener {
            showGeneratedCards()
        }

        // Initial card generation based on statusLampu.
        // generateCards()
    }

    private fun generateCards() {
        val linearLayout = binding.linearLayout

        // Clear existing cards.
        linearLayout.removeAllViews()

        // Generate cards based on statusLampu.
        statusLampu.forEachIndexed { index, value ->
            if (value == 0) {
                val cardView = createCardView(index, streetNames[index])
                linearLayout.addView(cardView)
            }
        }
    }

    private fun createCardView(elementIndex: Int, streetNames: String): CardView {
        val context = requireContext()

        val cardView = CardView(context)
        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardParams.setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen._10sdp))
        cardView.layoutParams = cardParams

        // Set the card background color to a specific color, e.g., white.
        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))

        val cardContent = LinearLayout(context)
        cardContent.orientation = LinearLayout.VERTICAL
        cardView.addView(cardContent)

        val description = "Lampu $elementIndex on $streetNames"
        val descriptionTextView = TextView(context)
        descriptionTextView.text = description
        descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        descriptionTextView.setTextColor(ContextCompat.getColor(context, R.color.dark_blue)) // Set text color here
        val widthInPixels = resources.getDimensionPixelSize(R.dimen._60sdp)

        // Inflate the "Pekerjaan" layout from XML
        val arusView = LayoutInflater.from(context).inflate(R.layout.item_pekerjaan, null)
        // Find the TextView by its ID
        val textViewBaterai = arusView.findViewById<TextView>(R.id.textViewBaterai)

        // Set the visibility to GONE to hide the TextView
        textViewBaterai.visibility = View.GONE

        // Create a separator (View element)
        val separator = View(context)
        val separatorHeightInPixels = resources.getDimensionPixelSize(R.dimen._12ssp) // Adjust the separator height
        val separatorLayoutParams = ViewGroup.LayoutParams(widthInPixels, separatorHeightInPixels)
        separator.layoutParams = separatorLayoutParams


        // Create a vertical linear layout to contain both rows of information
        val textContainer = LinearLayout(context)
        textContainer.orientation = LinearLayout.VERTICAL

        textContainer.addView(descriptionTextView)
        textContainer.addView(arusView)
        textContainer.addView(separator) // Add the separator after customTextView

        cardContent.addView(textContainer)

        val dismissButton = Button(context)
        dismissButton.text = "Selesai"
        dismissButton.setOnClickListener {
            pekerjaanSelesai.add(description) // Save description to pekerjaanSelesai
            cardView.visibility = View.GONE // Hide the card
        }
        dismissButton.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_blue))
        cardContent.addView(dismissButton)

        return cardView
    }

    private fun showGeneratedCards() {
        val linearLayout = binding.linearLayout

        // Clear existing cards.
        linearLayout.removeAllViews()

        // Generate new cards based on pekerjaanSelesai.
        pekerjaanSelesai.forEach { description ->
            val cardView = createCardViewFromDescription(description)

            linearLayout.addView(cardView)
        }
    }

    private fun createCardViewFromDescription(description: String): CardView {
        val context = requireContext()

        val cardView = CardView(context)
        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardParams.setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen._10sdp))
        cardView.layoutParams = cardParams

        val cardContent = LinearLayout(context)
        cardContent.orientation = LinearLayout.VERTICAL
        cardView.addView(cardContent)

        val descriptionTextView = TextView(context)
        descriptionTextView.text = description
        descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        descriptionTextView.setTextColor(ContextCompat.getColor(context, R.color.dark_blue)) // Set text color here
        cardContent.addView(descriptionTextView)

        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))

        // Inflate the "pekerjaan" layout from XML
        val arusView = LayoutInflater.from(context).inflate(R.layout.item_pekerjaan, null)
        // Find the TextView by its ID
        val textViewBaterai = arusView.findViewById<TextView>(R.id.textViewBaterai)

        // Set the visibility to GONE to hide the TextView
        textViewBaterai.visibility = View.GONE

        cardContent.addView(arusView)

        return cardView
    }

}
