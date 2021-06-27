package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.util.ServiceResult

class CompaniesRepository(
    private val companyPersistenceSource: CompanyPersistenceSource
) {

    suspend fun getActiveCompanies(): ServiceResult<List<Company>> = companyPersistenceSource.getActiveCompanies()

}

interface CompanyPersistenceSource {

    suspend fun getActiveCompanies(): ServiceResult<List<Company>>

}
