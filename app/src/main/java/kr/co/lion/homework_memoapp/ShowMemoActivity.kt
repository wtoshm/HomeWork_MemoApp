package kr.co.lion.homework_memoapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kr.co.lion.homework_memoapp.databinding.ActivityMainBinding
import kr.co.lion.homework_memoapp.databinding.ActivityShowMemoBinding

class ShowMemoActivity : AppCompatActivity() {

    lateinit var activityShowMemoBinding: ActivityShowMemoBinding

    // 10 ModifyMemoActivity의 런처 ActivityResultLauncher<Intent>
    lateinit var modifyMemoActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityShowMemoBinding = ActivityShowMemoBinding.inflate(layoutInflater)
        setContentView(activityShowMemoBinding.root)


        // 툴바 설정
        // 툴바 타이틀
        activityShowMemoBinding.tbShowMemo.title = "메모 보기"
        //툴바 뒤로가기 버튼
        activityShowMemoBinding.tbShowMemo.setNavigationIcon(R.drawable.arrow_back_24px)
        //툴바 뒤로가기 리스너
        activityShowMemoBinding.tbShowMemo.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        //툴바 메뉴 버튼
        activityShowMemoBinding.tbShowMemo.inflateMenu(R.menu.menu_tb_showmemo)


        //
        // Intent로부터 메모 객체를 추출한다.
        val memoData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra("memoData", MemoData::class.java)
        } else {
            intent.getParcelableExtra<MemoData>("memoData")
        }

        // 삭제할 데이터 identifier
        val idxStrToBeDeleted = memoData?.date

        // 뷰 설정
        activityShowMemoBinding.tfShowTitle.setText(memoData?.title)
        activityShowMemoBinding.tfShowDate.setText(memoData?.date)
        activityShowMemoBinding.tfShowBody.setText(memoData?.body)


        //10---------------------------------------------------

        // ModifyMemoActivity 런처
        val contract3 = ActivityResultContracts.StartActivityForResult()
        modifyMemoActivityLauncher = registerForActivityResult(contract3){
            // 34
            // 작업 결과가 OK 라면
            if(it.resultCode == RESULT_OK){
                // 전달된 Intent객체가 있다면

                if(it.data != null){
                    // 전달받은 메모 객체를 추출한다.
                    if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                        val modifiedMemoData = it.data?.getParcelableExtra("modifiedMemoData", MemoData::class.java)
//                        memoList.add(memoData!!)
//                        activityMainBinding.rvMain.adapter?.notifyDataSetChanged()
                        activityShowMemoBinding.tfShowTitle.isEnabled = true
                        activityShowMemoBinding.tfShowTitle.setText(modifiedMemoData?.title)
                        activityShowMemoBinding.tfShowDate.isEnabled = true
                        activityShowMemoBinding.tfShowDate.setText(modifiedMemoData?.date)
                        activityShowMemoBinding.tfShowBody.isEnabled = true
                        activityShowMemoBinding.tfShowBody.setText(modifiedMemoData?.body)
                    } else {
                        val modifiedMemoData = it.data?.getParcelableExtra<MemoData>("modifiedMemoData")
//                        memoList.add(memoData!!)
//                        activityMainBinding.rvMain.adapter?.notifyDataSetChanged()
                        activityShowMemoBinding.tfShowTitle.isEnabled = true
                        activityShowMemoBinding.tfShowTitle.setText(modifiedMemoData?.title)
                        activityShowMemoBinding.tfShowDate.isEnabled = true
                        activityShowMemoBinding.tfShowDate.setText(modifiedMemoData?.date)
                        activityShowMemoBinding.tfShowBody.isEnabled = true
                        activityShowMemoBinding.tfShowBody.setText(modifiedMemoData?.body)
                    }

                }
            }

        }

        // 2-2 툴바에 삭제 아이콘 클릭 리스너 설정
        activityShowMemoBinding.tbShowMemo.setOnMenuItemClickListener {
            // 메뉴의 id로 분기한다
            when(it.itemId){
                // 추가 메뉴
                R.id.menu_tb_showmemo_delete -> {
                    ///////// 데이터 삭제를 위해 받은 memoData 객체를 다시 보내기가 안되는데 정말 안되나??
                    val resultIntent = Intent()
                    resultIntent.putExtra("idxStrToBeDeleted", idxStrToBeDeleted)

                    setResult(RESULT_OK, resultIntent)
                    finish()

                }
                R.id.menu_tb_showmemo_modify -> {
                    val modifyIntent = Intent(this@ShowMemoActivity, ModifyMemoActivity::class.java)
                    modifyIntent.putExtra("memoData", memoData)
                    modifyMemoActivityLauncher.launch(modifyIntent)

                }
                else -> {

                }
            }
            true
        }





    }
}