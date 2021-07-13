package it.dbortoluzzi.tuttiapposto.ui.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.TextView
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.ActivitySettingsBinding
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.tuttiapposto.framework.SpinnerItem
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpActivity
import it.dbortoluzzi.tuttiapposto.ui.presenters.SettingsPresenter
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : BaseMvpActivity<SettingsActivity, SettingsPresenter>(), SettingsPresenter.View {

    @Inject
    override lateinit var mPresenter: SettingsPresenter

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveBtn.setOnClickListener {
            val companySpinnerSelected = getCompanySelected()

            if (TextUtils.isEmpty(companySpinnerSelected)) {
                val errorTextView = binding.companiesSpinner.selectedView as TextView
                errorTextView.error = getString(R.string.mandatory_error)
                return@setOnClickListener
            }

            mPresenter.doSaveOptions()
        }

        binding.exitBtn.setOnClickListener {
            finish()
        }

    }

    override fun getCompanySelected(): String {
        return (binding.companiesSpinner.selectedItem as SpinnerItem<*>).id.toString()
    }

    override fun onSuccessSave() {
        finish()
    }

    override fun renderCompanies(companies: List<Company>) {
        var spinnerCompanies: List<SpinnerItem<String>> = companies.map { SpinnerItem(it.uID, it.denomination) }
        spinnerCompanies += SpinnerItem("", getString(R.string.select_one_value))

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerCompanies)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.companiesSpinner.adapter = aa
        binding.companiesSpinner.setSelection(aa.getPosition(SpinnerItem(id = mPresenter.getPrefCompany()?:"")))
    }

    companion object {
        private val TAG = "OptionsActivity"
    }

}
