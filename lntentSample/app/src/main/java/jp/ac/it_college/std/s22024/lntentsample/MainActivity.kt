package jp.ac.it_college.std.s22024.lntentsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.it_college.std.s22024.lntentsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lvMenu.apply {
            adapter = MenuListAdapter(menuList)

            LinearLayoutManager(this@MainActivity).let {
                layoutManager = it

                addItemDecoration(DividerItemDecoration(this@MainActivity, it.orientation))
            }

        }
    }
}