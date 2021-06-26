package it.dbortoluzzi.tuttiapposto.ui.activities

import android.os.Bundle
import android.text.TextUtils
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.ActivityOptionsBinding
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpActivity
import it.dbortoluzzi.tuttiapposto.ui.presenters.OptionsPresenter
import javax.inject.Inject

@AndroidEntryPoint
class OptionsActivity : BaseMvpActivity<OptionsActivity, OptionsPresenter>(), OptionsPresenter.View {

    @Inject
    override lateinit var mPresenter: OptionsPresenter

    private lateinit var binding: ActivityOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveBtn.setOnClickListener {
            val company = binding.company.text.toString().trim()

            if (TextUtils.isEmpty(company)) {
                binding.company.error = getString(R.string.mandatory_error)
                return@setOnClickListener
            }

            mPresenter.doSaveOptions()
        }

        binding.exitBtn.setOnClickListener {
            finish()
        }

    }

    companion object {
        private val TAG = "OptionsActivity"
    }

    override fun getCompany(): String {
        return binding.company.text.toString()
    }

    override fun onSuccessSave() {
        finish()
    }

}
