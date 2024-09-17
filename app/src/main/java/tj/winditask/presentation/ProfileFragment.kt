package tj.winditask.presentation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import tj.winditask.R
import tj.winditask.data.model.AvatarParam
import tj.winditask.data.model.Profile
import tj.winditask.data.model.UserParam
import tj.winditask.databinding.FragmentProfileBinding
import tj.winditask.di.BASE_URL
import tj.winditask.domain.model.State
import tj.winditask.extensions.*
import tj.winditask.presentation.base.BaseFragment
import tj.winditask.presentation.viewModel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private var userId: Int = 0
    private var filename: String? = null
    private var base64Image: String? = null

    private val viewModel: UserViewModel by viewModels()

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()

        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedImageUri: Uri? = data?.data
                    uploadImage(selectedImageUri)
                }
            }
    }

    private fun uploadImage(imageUri: Uri?) {
        imageUri?.let { aUri ->
            val inputStream = requireContext().contentResolver.openInputStream(aUri)
            val imageBytes = inputStream?.readBytes()
            inputStream?.close()

            imageBytes?.let { byteArray ->
                base64Image = byteArray.encodeToBase64()
                filename =
                    requireContext().contentResolver.getFileName(imageUri) ?: "unknown_filename"
                Log.d("FILE_NAME000: ", "===" + filename)
            }

            binding.ivAvatar.setImageURI(aUri)
        }
    }


    private fun setupView() = with(binding) {
        textInputEdtPhone.makeEnabled()
        textInputEdtUsername.makeEnabled()
        textInputEdtBirthday.makeEnabledButClickable()
        textInputEdtBirthday.setOnClickListener {
            showBirthdayDialog()
        }
        ivAvatar.setOnClickListener {
            openImagePicker()
        }
        btnActionLogout.setOnClickListener {
            viewModel.clearTokens()
        }

        btnActionSave.setOnClickListener {
            val phone = textInputEdtPhone.text.toString()
            val username = textInputEdtUsername.text.toString()
            val name = textInputEdtName.text.toString()
            val city = textInputEdtCity.text.toString()
            val birthday = textInputEdtBirthday.text.toString()
            val vk = textInputEdtVk.text.toString()
            val instagram = textInputEdtInstagram.text.toString()

            when {
                filename.isNullOrBlank() && base64Image.isNullOrBlank() -> {
                    requireContext().showToast(resources.getString(R.string.error_avatar))
                }

                name.isBlank() -> {
                    requireContext().showToast(resources.getString(R.string.error_name))
                }

                else -> {
                    requireActivity().hideKeyboard()
                    viewModel.userParam = UserParam(
                        name = name,
                        username = username,
                        birthday = birthday.ifBlank { null },
                        city = city.ifBlank { null },
                        vk = vk.ifBlank { null },
                        instagram = instagram.ifBlank { null },
                        phone = phone,
                        avatars = AvatarParam(
                            filename = filename,
                            base64 = base64Image
                        ),
                        avatar = filename,
                        userId = userId
                    )
                    viewModel.updateUser()
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun showBirthdayDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Handle the selected date
                val formattedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                binding.textInputEdtBirthday.setText(formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return dateFormat.format(calendar.time)
    }

    private fun setupObservers() = with(viewModel) {
        profileState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    showLoadingDialog()
                }

                is State.Error -> {
                    dismissLoadingDialog()
                    requireContext().showToast(it.message)
                }

                is State.Success -> {
                    dismissLoadingDialog()
                    prepareProfile(it.data)
                }
            }
        }

        updateProfileState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    showLoadingDialog()
                }

                is State.Error -> {
                    dismissLoadingDialog()
                    requireContext().showToast(it.message)
                }

                is State.Success -> {
                    dismissLoadingDialog()
                    requireContext().showToast(resources.getString(R.string.updated_successfully))
                    viewModel.fetchUser()
                }
            }
        }

        clearTokenState.observe(viewLifecycleOwner) {
            showLoadingDialog()
            if (it.isCleared) {
                Handler().postDelayed({
                    dismissLoadingDialog()
                    findNavController().popBackStack(
                        R.id.ProfileFragment,
                        true
                    )
                    findNavController().navigate(R.id.PhoneFragment)
                }, 1500)
            } else {
                dismissLoadingDialog()
                requireContext().showToast(it.errorMessage)
            }
        }
    }

    private fun prepareProfile(profile: Profile?) {
        profile?.let {
            userId = profile.id
            binding.textInputEdtPhone.setText(it.phone)
            binding.textInputEdtUsername.setText(it.username)
            binding.textInputEdtName.setText(it.name)
            binding.textInputEdtCity.setText(it.city)
            binding.textInputEdtBirthday.setText(it.birthday)
            binding.textInputEdtVk.setText(it.vk)
            binding.textInputEdtInstagram.setText(it.instagram)
            it.avatars?.avatar.let { avatar ->
                Glide.with(this)
                    .load(BASE_URL + avatar)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.ivAvatar)
            }

        }
    }
}