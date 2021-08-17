package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.util.ServiceResult

class CompaniesRepositoryImpl(
    private val companyPersistenceSource: CompanyPersistenceSource
): CompaniesRepository {

    override suspend fun getActiveCompanies(): ServiceResult<List<Company>> = companyPersistenceSource.getActiveCompanies()

}

interface CompanyPersistenceSource {

    suspend fun getActiveCompanies(): ServiceResult<List<Company>>

}

interface CompaniesRepository {

    suspend fun getActiveCompanies(): ServiceResult<List<Company>>

}
