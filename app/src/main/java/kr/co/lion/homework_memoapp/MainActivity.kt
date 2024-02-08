package kr.co.lion.homework_memoapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.homework_memoapp.databinding.ActivityMainBinding
import kr.co.lion.homework_memoapp.databinding.RvMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // 3 InputActivity의 런처 ActivityResultLauncher<Intent>
    lateinit var inputActivityLauncher: ActivityResultLauncher<Intent>
    // 데이터 리스트
    val memoList = mutableListOf<MemoData>()

    // 4 ShowInfoActivity의 런처 ActivityResultLauncher<Intent>
    lateinit var showMemoActivityLauncher:ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)


        //1-1 툴바 타이틀 설정
        activityMainBinding.tbMain.title = "메모 관리"
        // 툴바 메뉴 설정 - inflateMenu (메뉴 xml 파일 이름)
        activityMainBinding.tbMain.inflateMenu(R.menu.menu_tb_main)


        //1-2 RV 설정
        activityMainBinding.rvMain.adapter = MyAdapter()
        activityMainBinding.rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
        // 구분선
        val deco = MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL)
        activityMainBinding.rvMain.addItemDecoration(deco)


        // 2-1 InputActivity 런처
        // 새로운 메모 객체 수신
        val contract1 = ActivityResultContracts.StartActivityForResult()
        inputActivityLauncher = registerForActivityResult(contract1){
            // 34
            // 작업 결과가 OK 라면
            if(it.resultCode == RESULT_OK){
                // 전달된 Intent객체가 있다면

                if(it.data != null){
                    // 전달받은 Intent객체에서 메모 객체를 추출한다.
                    if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                        val newMemoData = it.data?.getParcelableExtra("newMemoData", MemoData::class.java)
                        memoList.add(newMemoData!!)
                        activityMainBinding.rvMain.adapter?.notifyDataSetChanged()
                    } else {
                        val newMemoData = it.data?.getParcelableExtra<MemoData>("newMemoData")
                        memoList.add(newMemoData!!)
                        activityMainBinding.rvMain.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }

        // 3-1 ShowMemoActivity 런처
        // 삭제될 메모 객체 정보 수신
        val contract2 = ActivityResultContracts.StartActivityForResult()
        showMemoActivityLauncher = registerForActivityResult(contract2){
            // 34
            // 작업 결과가 OK 라면
            if(it.resultCode == RESULT_OK){
                // 전달된 Intent객체가 있다면
                if(it.data != null){

                        val idxStrToBeDeleted = it.data?.getStringExtra("idxStrToBeDeleted")
                        val itemsToBeDeleted : List<MemoData> = memoList.filter { it.date == idxStrToBeDeleted}
                        val indexToBeDeleted = memoList.indexOf(itemsToBeDeleted[0])
                        memoList.removeAt(indexToBeDeleted)
                        activityMainBinding.rvMain.adapter?.notifyDataSetChanged()

                }
            }
        }

        // 2-2 툴바에 Input 론처 리스너 설정
        activityMainBinding.tbMain.setOnMenuItemClickListener {
            // 메뉴의 id로 분기한다
            when(it.itemId){
                // 추가 메뉴
                R.id.menu_tb_main_add -> {
                    // InputActivity를 실행한다.
                    val inputIntent = Intent(this@MainActivity, InputActivity::class.java)
                    inputActivityLauncher.launch(inputIntent)
                }
                else -> {

                }
            }
            true
        }
    }


    // 1-3 RecyclerView의 어뎁터
    inner class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
        // ViewHolder
        inner class MyViewHolder(rvMainBinding: RvMainBinding) : RecyclerView.ViewHolder(rvMainBinding.root){
            val rvMainBinding:RvMainBinding

            init{
                this.rvMainBinding = rvMainBinding
                // 항목 클릭시 전체가 클릭이 될 수 있도록
                // 가로 세로 길이를 설정해준다.
                this.rvMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                // 9 항목을 눌렀을 때의 리스너
                this.rvMainBinding.root.setOnClickListener {
                    // ShowMemoActivity를 실행한다.
                    val showMemoIntent = Intent(this@MainActivity, ShowMemoActivity::class.java)

                    //라운드 3
                    // 선택한 항목 번째의 메모 객체를 Intent 에 담는다.

                    showMemoIntent.putExtra("memoData", memoList[adapterPosition])
                    /////////// 삭제 등을 위해 adapterPosition 도 담아 보낼 수는 없나??
                    //showMemoIntent.putExtra("index", 2)
                    /////////// 아니면 데이터 리스트를 ShowActivity에서도 접근하게 만들 수 있나??

                    showMemoActivityLauncher.launch(showMemoIntent)

                }

            }
        }

        //1-3
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val rvMainBinding = RvMainBinding.inflate(layoutInflater)
            val myViewHolder = MyViewHolder(rvMainBinding)

            return myViewHolder
        }

        override fun getItemCount(): Int {
            //1-3
            //return 100
            // 32
            return memoList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            //1-3
//            holder.rvMainBinding.tvRvMainTitle.text = "제목 $position"
//            holder.rvMainBinding.tvRvMainDate.text = "$position 일"
            //2-
            holder.rvMainBinding.tvRvMainTitle.text = "${memoList[position].title}"
            holder.rvMainBinding.tvRvMainDate.text = "${memoList[position].date}"
        }
    }

}