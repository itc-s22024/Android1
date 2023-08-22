package jp.ac.it_college.std.s22024.jatabasesample

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.it_college.std.s22024.jatabasesample.databinding.RowBinding

class CocktailAdapter(
    private val data: List<String>,
    private val callback: (Int,String) -> Unit
    ) :
        RecyclerView.Adapter<CocktailAdapter.ViewHolder>() {
    class ViewHolder(internal val binding: RowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(RowBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCocktailName.text = data[position]
        holder.binding.root.setOnClickListener {
            callback(position, data[position])
        }
    }

    override fun getItemCount() = data.size
}

