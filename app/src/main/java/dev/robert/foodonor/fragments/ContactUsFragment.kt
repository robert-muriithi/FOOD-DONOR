package dev.robert.foodonor.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.robert.foodonor.databinding.FragmentContactUsBinding


@AndroidEntryPoint
class ContactUsFragment : Fragment() {
    private lateinit var binding: FragmentContactUsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        val view = binding.root

        val name  = binding.nameError.editText?.text.toString()
        val email = binding.emailError.editText?.text.toString().trim()
        val description = binding.decriptionError.editText?.text.toString().trim()

        binding.submit.setOnClickListener {
           when{
               binding.nameError.editText?.text.toString().isEmpty() -> {
                   binding.nameError.error = "Name is required"
                   binding.nameError.isErrorEnabled = true
               }
               binding.emailError.editText?.text.toString().isEmpty() -> {
                   binding.emailError.error = "Email is required"
                   binding.emailError.isErrorEnabled = true
                   return@setOnClickListener
               }
               binding.decriptionError.editText?.text.toString().isEmpty() -> {
                   binding.decriptionError.error = "Required"
                   binding.decriptionError.isErrorEnabled = true
                   return@setOnClickListener
               }
               else -> {
                       binding.nameError.isErrorEnabled = false
                       binding.emailError.isErrorEnabled = false
                       binding.decriptionError.isErrorEnabled = false
                   val emailIntent = Intent(
                       Intent.ACTION_SEND
                   )
                   emailIntent.action = Intent.ACTION_SEND
                   emailIntent.type = "message/rfc822"
                   emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("leencelidonde@gmail.com"))
                   emailIntent.putExtra(Intent.EXTRA_CC, "")
                   emailIntent.putExtra(Intent.EXTRA_BCC, "")
                   emailIntent.putExtra(
                       Intent.EXTRA_SUBJECT,
                       "Feedback from Foodonor"
                   )
                   emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, \n Message: \n $description")
                   emailIntent.type = "text/html"
                   startActivity(Intent.createChooser(emailIntent,
                       "Send Email Using: "))
               }
           }

        }
        return view
    }

}