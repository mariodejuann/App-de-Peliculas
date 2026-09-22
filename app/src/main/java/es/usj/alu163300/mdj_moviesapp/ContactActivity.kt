package es.usj.alu163300.mdj_moviesapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button

class ContactActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val buttonEmail = findViewById<Button>(R.id.buttonEmail)
        val buttonWebsite = findViewById<Button>(R.id.buttonWebsite)
        val buttonPhone = findViewById<Button>(R.id.buttonPhone)

        buttonEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:alu.163300@usj.es")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Movies App contact")
            startActivity(intent)
        }

        buttonWebsite.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.usj.es")
            )
            startActivity(intent)
        }

        buttonPhone.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:123456789")
            )
            startActivity(intent)
        }
    }
}