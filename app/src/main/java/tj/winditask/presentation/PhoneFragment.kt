package tj.winditask.presentation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tj.winditask.R
import tj.winditask.databinding.FragmentPhoneBinding
import tj.winditask.domain.model.State
import tj.winditask.extensions.showToast
import tj.winditask.presentation.viewModel.AuthViewModel

@AndroidEntryPoint
class PhoneFragment : Fragment() {

    private var _binding: FragmentPhoneBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: AuthViewModel by viewModels()
    private var loadingDialog: AlertDialog? = null

    private var phoneFull: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
    }

    private fun setupView() = with(binding) {
        btnActionSubmit.setOnClickListener {
            if (edtPhone.text.toString().isNotBlank()) {
                binding.countryCodePicker.showFlag(false)
                binding.countryCodePicker.fullNumber =
                    countryCodePicker.selectedCountryCode + edtPhone.text.toString()
                phoneFull = binding.countryCodePicker.fullNumberWithPlus
                phoneFull?.let { aPhone ->
                    viewModel.sendAuthCode(aPhone)
                }
            } else {
                edtPhone.setBackgroundResource(R.drawable.bg_error_edt)
                binding.btnActionSubmit.isEnabled = false
                binding.btnActionSubmit.isClickable = false
            }
        }
        countryCodePicker.registerCarrierNumberEditText(binding.edtPhone)
        countryCodePicker.setPhoneNumberValidityChangeListener { isValidNumber ->
            if (isValidNumber) {
                // Clear error state
                edtPhone.setBackgroundResource(R.drawable.bg_edt)
                binding.btnActionSubmit.isEnabled = true
                binding.btnActionSubmit.isClickable = true
            } else {
                // Set error state and update background
                edtPhone.setBackgroundResource(R.drawable.bg_error_edt)
                binding.btnActionSubmit.isEnabled = false
                binding.btnActionSubmit.isClickable = false
            }
        }
    }

    private fun setupObservers() = with(viewModel) {
        sendAuthCodeState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    showLoadingDialog()
                }

                is State.Error -> {
                    dismissLoadingDialog()
                    requireContext().showToast(it.message)
                }

                is State.Success -> {
                    Handler().postDelayed({
                        dismissLoadingDialog()
                        findNavController().navigate(
                            R.id.action_PhoneFragment_to_SmsCodeFragment,
                            bundleOf(SmsCodeFragment.ARG_PHONE to phoneFull)
                        )
                    }, 1000)
                }
            }
        }
    }

    private fun showLoadingDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(R.layout.dialog_loading) // Replace with your custom layout for the loading dialog
        builder.setCancelable(false) // Prevent dialog dismissal on outside touch or back press

        loadingDialog = builder.create()
        loadingDialog?.show()
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.countryCodePicker.deregisterCarrierNumberEditText()
        _binding = null
    }
}