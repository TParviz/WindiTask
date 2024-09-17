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
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import dagger.hilt.android.AndroidEntryPoint
import tj.winditask.R
import tj.winditask.databinding.FragmentSmsCodeBinding
import tj.winditask.domain.model.State
import tj.winditask.extensions.hideKeyboard
import tj.winditask.extensions.showToast
import tj.winditask.presentation.viewModel.AuthViewModel

@AndroidEntryPoint
class SmsCodeFragment : Fragment() {

    private var _binding: FragmentSmsCodeBinding? = null
    private val binding get() = checkNotNull(_binding)

    private var phone: String = ""

    private val viewModel: AuthViewModel by viewModels()
    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phone = it.getString(ARG_PHONE, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSmsCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
    }

    private fun setupView() = with(binding) {
        smsCodeView.onChangeListener = SmsConfirmationView.OnChangeListener { code, isComplete ->
            if (isComplete) {
                viewModel.checkAuthCode(phone = phone, code = code)
            }
        }
    }

    private fun setupObservers() = with(viewModel) {
        checkAuthCodeState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    showLoadingDialog()
                }

                is State.Error -> {
                    dismissLoadingDialog()
                    requireContext().showToast(it.message)
                }

                is State.Success -> {
                    it.data?.let { aUserToken ->
                        requireActivity().hideKeyboard()
                        if (aUserToken.isUserExists) {
                            Handler().postDelayed({
                                dismissLoadingDialog()
                                findNavController().popBackStack(
                                    R.id.SmsCodeFragment,
                                    true
                                )
                                findNavController().navigate(R.id.ProfileFragment)
                            }, 1000)
                        } else {
                            Handler().postDelayed({
                                dismissLoadingDialog()
                                findNavController().navigate(
                                    R.id.action_SmsCodeFragment_to_RegisterFragment,
                                    bundleOf(RegisterFragment.ARG_PHONE to phone)
                                )
                            }, 1000)
                        }
                    }
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
        _binding = null
    }

    companion object {
        const val ARG_PHONE = "arg_phone"
    }
}