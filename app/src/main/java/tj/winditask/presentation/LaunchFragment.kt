package tj.winditask.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tj.winditask.R
import tj.winditask.databinding.FragmentLaunchBinding
import tj.winditask.presentation.base.BaseFragment
import tj.winditask.presentation.viewModel.UserViewModel


@AndroidEntryPoint
class LaunchFragment : BaseFragment<FragmentLaunchBinding>() {

    private val viewModel: UserViewModel by viewModels()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLaunchBinding {
        return FragmentLaunchBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUserToken()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userTokenState.observe(viewLifecycleOwner) {
            it.userToken?.let { aToken ->
                if (aToken.accessToken.isNotBlank()) {
                    findNavController().popBackStack(
                        findNavController().graph.startDestinationId,
                        true
                    )
                    findNavController().navigate(R.id.ProfileFragment)
                } else {
                    findNavController().navigate(R.id.action_LaunchFragment_to_PhoneFragment)
                }
            }
        }
    }

}