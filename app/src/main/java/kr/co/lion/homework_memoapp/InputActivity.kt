package kr.co.lion.homework_memoapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.homework_memoapp.databinding.ActivityInputBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InputActivity : AppCompatActivity() {
    // 새로운 메모 객체 정보 입력 받아서 메인화면으로 발신
    // 론처는 필요없고 resultIntent
//    val newMemoData = MemoData(title, body, dateStr)
//    val resultIntent = Intent()
//    resultIntent.putExtra("newMemoData", newMemoData)
//    setResult(RESULT_OK, resultIntent)
//    finish()

    lateinit var activityInputBinding: ActivityInputBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityInputBinding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(activityInputBinding.root)

        //----------------------

        //activityInputBinding.tfInputTitle.visibility = View.VISIBLE

        // 툴바 설정
        // 툴바 타이틀
        activityInputBinding.tbInput.title = "메모 작성"
        //툴바 뒤로가기 버튼
        activityInputBinding.tbInput.setNavigationIcon(R.drawable.arrow_back_24px)
        //툴바 메뉴 버튼
        activityInputBinding.tbInput.inflateMenu(R.menu.menu_tb_input)

        //툴바 뒤로가기 리스너
        activityInputBinding.tbInput.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        // 툴바 메뉴 버튼 리스너 설정
        activityInputBinding.tbInput.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_tb_input_done -> {
                    processInputDone()
                }
            }
            true
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun processInputDone() {
        //Toast.makeText(this@InputActivity, "눌러졌습니다", Toast.LENGTH_SHORT).show()

        activityInputBinding.apply {

            // 사용자가 입력한 내용을 string으로 가져온다
            val title = tfInputTitle.text.toString()
            val body = tfInputBody.text.toString()

            // 현재 일시
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dateStr = current.format(formatter)

            // 입력 검사
            if (title.isEmpty()) {
                showDialog("제목 입력 오류", "제목을 입력해주세요", tfInputTitle)
                return
            }
            if (body.isEmpty()) {
                showDialog("내용 입력 오류", "내용을 입력해주세요", tfInputBody)
                return
            }

            // 33
            // 입력받은 정보를 메모 객체에 담는다.
            val newMemoData = MemoData(title, body, dateStr)

            Snackbar.make(activityInputBinding.root, "등록이 완료되었습니다", Snackbar.LENGTH_SHORT).show()

            // result intent 설정 및 새로운 메모 객체를 부가
            val resultIntent = Intent()
            resultIntent.putExtra("newMemoData", newMemoData)
            setResult(RESULT_OK, resultIntent)
            finish()

        }
    }

    // 다이얼로그를 보여주는 메서드
    fun showDialog(title:String, message:String, focusView: TextInputEditText){
        // 다이얼로그를 보여준다.
        val builder = MaterialAlertDialogBuilder(this@InputActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                focusView.setText("")
                focusView.requestFocus()
                //showSoftInput(focusView)
            }
        }
        builder.show()

    }


}