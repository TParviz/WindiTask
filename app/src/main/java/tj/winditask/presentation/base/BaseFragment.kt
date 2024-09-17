package tj.winditask.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import tj.winditask.R

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    protected val binding: VB
        get() = checkNotNull(_binding)

    private var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showLoadingDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(R.layout.dialog_loading)
        alertDialogBuilder.setCancelable(false)
        loadingDialog = alertDialogBuilder.create()
        loadingDialog?.show()
    }

    protected fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}