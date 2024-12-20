package com.example.projetjeux

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetjeux.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var actifs = 100
    private val images = arrayOf(
        R.drawable.banane,
        R.drawable.charbon,
        R.drawable.diamant,
        R.drawable.emeraude,
        R.drawable.fsa,
        R.drawable.img7,
        R.drawable.piece,
        R.drawable.gerald_godin,
        R.drawable.sacargent,
        R.drawable.tresor,

    )
    private val random = Random

    private lateinit var binding: ActivityMainBinding // View Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState != null) {
            actifs = savedInstanceState.getInt("actifs", 100)
        }

        binding.button.setOnClickListener { jouer() }
        binding.editTextText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ajouterArgent()
                true
            } else {
                false
            }
        }

        mettreAJourAffichage()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("actifs", actifs)
    }

    private fun jouer() {
        val mise = getMise()
        if (mise <= actifs) {
            actifs -= mise


            val img1 = images[random.nextInt(images.size)]


            val isTroisImagesIdentiques = random.nextBoolean()


            val img2 = if (isTroisImagesIdentiques) img1 else images[random.nextInt(images.size)]
            val img3 = if (isTroisImagesIdentiques) img1 else images[random.nextInt(images.size)]


            binding.imageView1.setImageResource(img1)
            binding.imageView2.setImageResource(img2)
            binding.imageView3.setImageResource(img3)


            calculerGains(img1, img2, img3, mise)
        } else {
            Toast.makeText(this, "Fonds insuffisants!", Toast.LENGTH_SHORT).show()
        }
        mettreAJourAffichage()
    }

    private fun ajouterArgent() {
        val code = binding.editTextText.text.toString()
        if (code == "votre_code_secret") {
            actifs += 100
            binding.editTextText.setText("")
            mettreAJourAffichage()
        }
    }

    private fun mettreAJourAffichage() {
        binding.textView.text = getString(R.string.actifs) + actifs
        binding.button.isEnabled = actifs > 0
        binding.radioButton1.isEnabled = actifs >= 1
        binding.radioButton2.isEnabled = actifs >= 2
        binding.radioButton3.isEnabled = actifs >= 5
    }

    private fun calculerGains(image1: Int, image2: Int, image3: Int, mise: Int) {
        var gain = 0


        if (binding.checkBox.isChecked) {
            if (image1 == R.drawable.fsa && image2 == R.drawable.fsa && image3 == R.drawable.fsa) {
                gain = 100 * mise
                Toast.makeText(this, "Gagné (Casse-Cou) ! Montant : $gain", Toast.LENGTH_SHORT).show()
            } else if ((image1 == R.drawable.gerald_godin && image2 == R.drawable.gerald_godin) ||
                (image2 == R.drawable.gerald_godin && image3 == R.drawable.gerald_godin) ||
                (image1 == R.drawable.gerald_godin && image3 == R.drawable.gerald_godin)) {
                gain = 10 * mise
                Toast.makeText(this, "Gagné (Casse-Cou) ! Montant : $gain", Toast.LENGTH_SHORT).show()
            }
        } else {

            if (image1 == image2 && image2 == image3) {
                gain = 25 * mise
                Toast.makeText(this, "Gagné ! Montant : $gain", Toast.LENGTH_SHORT).show()
            } else if (image1 == image2 || image2 == image3 || image1 == image3) {
                gain = mise
                Toast.makeText(this, "Gagné ! Montant : $gain", Toast.LENGTH_SHORT).show()
            }
        }


        actifs += gain
    }

    private fun getMise(): Int {
        return when {
            binding.radioButton1.isChecked -> 1
            binding.radioButton2.isChecked -> 2
            binding.radioButton3.isChecked -> 5
            else -> 0
        }
    }
}
