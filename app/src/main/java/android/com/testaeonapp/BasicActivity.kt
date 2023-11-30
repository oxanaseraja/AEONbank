package android.com.testaeonapp

import android.com.testaeonapp.databinding.ContentBasicBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class BasicActivity : AppCompatActivity() {
    private lateinit var binding: ContentBasicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentBasicBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}