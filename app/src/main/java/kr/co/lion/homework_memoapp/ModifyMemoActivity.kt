package kr.co.lion.homework_memoapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kr.co.lion.homework_memoapp.databinding.ActivityModifyMemoBinding

class ModifyMemoActivity : AppCompatActivity() {

    lateinit var activityModifyMemoBinding: ActivityModifyMemoBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityModifyMemoBinding = ActivityModifyMemoBinding.inflate(layoutInflater)
        setContentView(activityModifyMemoBinding.root)

        // 툴바 설정
        // 툴바 타이틀
        activityModifyMemoBinding.tbModifyMemo.title = "메모 수정"
        //툴바 뒤로가기 버튼
        activityModifyMemoBinding.tbModifyMemo.setNavigationIcon(R.drawable.arrow_back_24px)
        //툴바 리스너
        activityModifyMemoBinding.tbModifyMemo.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        //툴바 메뉴 버튼
        activityModifyMemoBinding.tbModifyMemo.inflateMenu(R.menu.menu_tb_modifymemo)

        // Intent로 부터 메모 정보 객체를 추출한다.
        val memoData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra("memoData", MemoData::class.java)
        } else {
            intent.getParcelableExtra<MemoData>("memoData")
        }


        // 수정화면 툴바의 수정 완료 아이콘 클릭 리스너 설정
        activityModifyMemoBinding.tbModifyMemo.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menu_tb_modify_done -> {
                    val dateStr = memoData?.date
                    processModifyDone(dateStr!!)
                }
            }
            true
        }


        // 뷰 설정
        activityModifyMemoBinding.tfModifyTitle.setText(memoData?.title)
        //activityModifyMemoBinding.tfInputDate.setText(memoData?.date)
        activityModifyMemoBinding.tfModifyBody.setText(memoData?.body)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun processModifyDone(dateStr:String) {
        //Toast.makeText(this@InputActivity, "눌러졌습니다", Toast.LENGTH_SHORT).show()

        activityModifyMemoBinding.apply {
            // 사용자가 입력한 내용을 string으로 가져온다
            val title = tfModifyTitle.text.toString()
            val body = tfModifyBody.text.toString()

            // 입력 검사
            if (title.isEmpty()) {
                showDialog("제목 입력 오류", "제목을 입력해주세요", tfModifyTitle)
                return
            }
            if (body.isEmpty()) {
                showDialog("내용 입력 오류", "내용을 입력해주세요", tfModifyBody)
                return
            }


            // 33
            // 입력받은 정보를 객체에 담는다.
            val modifiedMemoData = MemoData(title, body, dateStr)

            //Snackbar.make(activityModifyMemoBinding.root, "등록이 완료되었습니다", Snackbar.LENGTH_SHORT).show()
            // 이전으로 돌아간다.
            val resultIntent = Intent()
            resultIntent.putExtra("modifiedMemoData", modifiedMemoData)

            setResult(RESULT_OK, resultIntent)
            finish()

        }
    }

    // 다이얼로그를 보여주는 메서드
    fun showDialog(title:String, message:String, focusView: TextInputEditText){
        // 다이얼로그를 보여준다.
        val builder = MaterialAlertDialogBuilder(this@ModifyMemoActivity).apply {
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