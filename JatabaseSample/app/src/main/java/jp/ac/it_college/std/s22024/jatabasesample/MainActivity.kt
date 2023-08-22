package jp.ac.it_college.std.s22024.jatabasesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.ac.it_college.std.s22024.jatabasesample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var _helper: DatabaseHelper

    private var _cocktailId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _helper = DatabaseHelper(this)

        initList(binding.lvCocktail)

        binding.btnSave.setOnClickListener(::onSaveButton)
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    private fun initList(list: RecyclerView) {
        val data = resources.getStringArray(R.array.lv_cocktail_list)
        val adapter = CocktailAdapter(data.toList()) { pos, name ->
            // 選択されたカクテル名を画面上に表示
            binding.tvCockteilName.text = name

            // 選択されたカクテルのID（番号）を MainActivityのプロパティで保持
            _cocktailId = pos.toLong()

            // データベースからメモを取ってきてセットする
            binding.etNote.setText(loadCocktailMemos())
        }
        val manager = LinearLayoutManager(this)
        list.adapter = adapter
        list.layoutManager = manager
        list.addItemDecoration(
            DividerItemDecoration(this, manager.orientation)
        )
    }

    private fun onSaveButton(view: View) {
        // 入力された感想を取得
        val note = binding.etNote.text.toString()
        // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        val db = _helper.writableDatabase

        // 既存のデータを削除してから、新たな感想を保存する方式
        // 削除用SQL
        val deleteSQL = """
            | DELETE FROM cocktail_memos
            | WHERE _id = ?
        """.trimMargin()
        // SQLを読み込ませて値をセットして実行
        db.compileStatement(deleteSQL).let { stmt ->
            // プレースホルダ(?)に変数の値(id)をバインド(割り当て)
            stmt.bindLong(1, _cocktailId)
            stmt.executeUpdateDelete()
        }

        // 保存用SQL
        val insertSQL = """
            | INSERT INTO cocktail_memos (_id, name, note)
            | VALUES (?, ?, ?)
        """.trimMargin()
        // SQLを読み込ませて〜（プリペアドステートメント）
        db.compileStatement(insertSQL).let { stmt ->
            // プレースホルダ(?)に各値をバインド(割り当て)
            stmt.bindLong(1, _cocktailId)
            stmt.bindString(2, binding.tvCockteilName.text.toString())
            stmt.bindString(3, note)
            // 組み立てたINSERT SQL 文を実行
            stmt.executeInsert()
        }
        // View とかの初期化？
        binding.etNote.setText("")
        binding.tvCockteilName.text = ""
        _cocktailId = 0
    }

    private fun loadCocktailMemos(): String {
        // 読み取り専用のデータベース接続オブジェクトを使用する
        val db = _helper.readableDatabase

        // 取得用のSQL
        val select = """
            | SELECT * FROM cocktail_memos
            | WHERE _id = $_cocktailId
        """.trimMargin()
        // INSERT SQL 文を実行してカーソルオブジェクトを取得する
        val cursor = db.rawQuery(select, arrayOf("$_cocktailId"))

        // カーソルからデータを取り出す。なければ空文字列("")を返す
        return cursor.use {
            if (cursor.moveToNext()) {
                cursor.getString(cursor.getColumnIndexOrThrow("note"))
            } else {
                ""
            }
        }
    }
}