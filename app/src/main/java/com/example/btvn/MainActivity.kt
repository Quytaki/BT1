package com.example.btvn

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btvn.databinding.ActivityMainBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Sử dụng ViewBinding
    private lateinit var binding: ActivityMainBinding

    // Danh sách người dùng và Adapter
    private val userList = mutableListOf<User>()
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ánh xạ Window Insets (tuỳ chọn)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Khởi tạo RecyclerView
        setupRecyclerView()

        // Xử lý sự kiện khi nhấn nút Save
        setupSaveButton()
    }

    // Cài đặt RecyclerView
    private fun setupRecyclerView() {
        userAdapter = UserAdapter(userList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = userAdapter
    }

    // Xử lý sự kiện bấm nút lưu
    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val phone = binding.editTextPhone.text.toString().trim()

            // Kiểm tra giới tính đã được chọn
            val selectedGenderId = binding.radioGroupGender.checkedRadioButtonId
            val gender = when (selectedGenderId) {
                R.id.radioMale -> "Nam"
                R.id.radioFemale -> "Nữ"
                R.id.radioOther -> "Khác"
                else -> ""
            }

            // Kiểm tra CheckBox đồng ý điều khoản
            if (!binding.checkBoxAgreement.isChecked) {
                Toast.makeText(this, "Bạn cần đồng ý với điều khoản sử dụng", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kiểm tra các trường thông tin đã được điền đầy đủ
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tạo đối tượng User và thêm vào danh sách
            val user = User(name, email, phone, gender)
            userList.add(user)

            // Cập nhật RecyclerView
            userAdapter.notifyItemInserted(userList.size - 1)
            binding.recyclerView.scrollToPosition(userList.size - 1) // Cuộn xuống cuối danh sách

            // Làm sạch các trường nhập liệu
            clearInputFields()

            // Hiển thị thông báo
            Toast.makeText(this, "Thông tin đã được lưu", Toast.LENGTH_SHORT).show()
        }
    }

    // Hàm xóa dữ liệu trong các trường nhập liệu sau khi lưu
    private fun clearInputFields() {
        binding.editTextName.text.clear()
        binding.editTextEmail.text.clear()
        binding.editTextPhone.text.clear()
        binding.radioGroupGender.clearCheck()
        binding.checkBoxAgreement.isChecked = false
    }
}
