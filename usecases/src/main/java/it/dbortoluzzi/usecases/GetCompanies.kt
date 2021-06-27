package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.CompaniesRepository
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.util.ServiceResult

class GetCompanies(private val companiesRepository: CompaniesRepository) {

    suspend operator fun invoke(): List<Company> {
        return when (val activeCompaniesResult = companiesRepository.getActiveCompanies()) {
            is ServiceResult.Success -> activeCompaniesResult.data
            is ServiceResult.Error -> arrayListOf()
            else -> throw NotImplementedError()
        }
    }

}
