package com.example.todo


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.Pomodoro.Pomodoro
import com.example.todo.RegisterAndLogin.LoginActivity
import com.example.todo.RegisterAndLogin.Restar
import com.example.todo.Task.*
import com.example.todo.market.MarketMainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var button_login: Button
    private lateinit var button_logout: Button
    private lateinit var button_market: Button

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val controlView = findViewById<View>(R.id.control)
        val username = findViewById<TextView>(R.id.button_user)
        username.setText(getString(R.string.username1))

        val fabAdd = controlView.findViewById<FloatingActionButton>(R.id.fab_add)
        val fabDisplay = controlView.findViewById<FloatingActionButton>(R.id.fab_display)

        fabAdd.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
        fabDisplay.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)

        }
        val pomodoro = controlView.findViewById<FloatingActionButton>(R.id.pomodoro)
        pomodoro.setOnClickListener {
            val intent = Intent(this, Pomodoro::class.java)
            startActivity(intent)
        }

        val dra = findViewById<DrawerLayout>(R.id.draw)
        button_login=findViewById<Button>(R.id.button_login)
        button_logout=findViewById<Button>(R.id.button_logout)
        button_market=findViewById<Button>(R.id.button_market)
        val button_logout=findViewById<Button>(R.id.button_logout)
        //点击主页面中的按钮弹出侧滑页面
        dra.openDrawer(Gravity.LEFT)
        //点击侧滑页面中的按钮缩回侧滑页面
        dra.closeDrawer(Gravity.LEFT)
        //dra的侧拉监听事件
        dra.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(view: View, v: Float) {}
            override fun onDrawerOpened(view: View) {
                Toast.makeText(this@MainActivity, "侧拉菜单打开", Toast.LENGTH_LONG).show()
            }

            override fun onDrawerClosed(view: View) {
                Toast.makeText(this@MainActivity, "侧拉菜单关闭", Toast.LENGTH_LONG).show()
            }

            override fun onDrawerStateChanged(i: Int) {
                //Toast.makeText(this@MainActivity, "change", Toast.LENGTH_LONG).show()
            }
        })

        button_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        button_logout.setOnClickListener {
            LoginActivity.userID = "-1"
            restartApp(this,2000)
        }
        val intent = intent
        val userId = intent.getIntExtra("user_id", -1)
        val userName = intent.getStringExtra("user_name")
        if (!LoginActivity.userID.equals("-1")){
            username.setText(LoginActivity.userID)
        }
        else{
            username.setText(getString(R.string.username1))
        }
        if (!username.text.equals(getString(R.string.username1))) {
            loginS()
        }

    }
    override fun onResume() {
        super.onResume()
        updateTaskList()
    }

    fun updateTaskList() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val tasks = getTasksFromDatabase()
        val adapter = TaskAdapter(this, tasks as MutableList<Task>)
        recyclerView.adapter = adapter
    }


    private fun getTasksFromDatabase(): List<Task> {
        // 获取数据库实例
        val dbHelper = TaskDbHelper(this)
        val db = dbHelper.readableDatabase

        // 定义要查询的列
        val projection = arrayOf(
            TaskContract.TaskEntry._ID,
            TaskContract.TaskEntry.COLUMN_TITLE,
            TaskContract.TaskEntry.COLUMN_DESCRIPTION,
            TaskContract.TaskEntry.COLUMN_DUE_DATE,
            TaskContract.TaskEntry.COLUMN_PRIORITY,
            TaskContract.TaskEntry.COLUMN_CATEGORY
        )

        // 查询数据库
        val cursor = db.query(
            TaskContract.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            "${TaskContract.TaskEntry.COLUMN_PRIORITY} DESC"
        )

        // 将查询结果转换为List<Task>
        val tasks = mutableListOf<Task>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(TaskContract.TaskEntry._ID))
                val title =
                    getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_TITLE))
                val description =
                    getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DESCRIPTION))
                val dueDate =
                    getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_DUE_DATE))
                val priority =
                    getInt(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_PRIORITY))
                val category =
                    getString(getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_CATEGORY))
                tasks.add(Task(id, title, description, dueDate, priority, category))
            }
        }

        // 关闭查询
        cursor.close()


        tasks.sortWith(compareBy<Task> { it.dueDate }.thenByDescending { it.priority })

        return tasks
    }

    /** 重启应用 */
    @JvmOverloads
    fun restartApp(context: Context, delayed: Long = 2000L) {
        // 创建Intent
        val intent = Intent(context, Restar::class.java)
        // 传入包名和延时时间
        intent.putExtra("PackageName", context.packageName)
        intent.putExtra("Delayed", delayed)

        // 启动服务
        context.startService(intent)

        // 杀死整个进程
        //android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }
    fun loginS() {
        if (!LoginActivity.userID.equals("-1")) {
            button_login.visibility = View.INVISIBLE // 登录后隐藏登录键
            button_logout.visibility = View.VISIBLE
            button_market.visibility = View.VISIBLE
        }
    }

    fun goToMarket(view: View?){
        val intent = Intent(this, MarketMainActivity::class.java)
        startActivity(intent)
    }

}