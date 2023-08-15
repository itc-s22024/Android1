package jp.ac.it_college.std.s22024.fragmantsample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.it_college.std.s22024.fragmantsample.databinding.FragmentMenuListBinding

/**
 * メニュー一覧表を表示するためのフラグメント
 */

internal const val REQUEST_SELECTED_MENU = "selectedMenu"
internal const val RESULT_NAME = "menuName"
internal const val RESULT_PRICE = "menuPrice"

class MenuListFragment : Fragment() {
    // Bindingクラスのインスタンスを入れておくプロパティ(Nullable)
    private var _bindig: FragmentMenuListBinding? = null
    // Activityのときと同じようにbindingを使うための工夫
    private val binding get() = _bindig!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindig = FragmentMenuListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.menuList.apply {
            adapter = MenuAdapter(teisyokuList) {
                parentFragmentManager.setFragmentResult(REQUEST_SELECTED_MENU, bundleOf(
                    RESULT_NAME to it.name,
                    RESULT_PRICE to it.price
                ))
            }
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            addItemDecoration(DividerItemDecoration(context, manager.orientation))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindig = null
    }

}