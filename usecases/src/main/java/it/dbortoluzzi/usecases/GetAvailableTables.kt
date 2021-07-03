package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.TablePersistenceSource
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult

class GetAvailableTables(private val tablePersistenceSource: TablePersistenceSource) {

    suspend operator fun invoke(): List<Table> {
        return when (val serviceResult = tablePersistenceSource.getAvailableTables()) {
            is ServiceResult.Success -> serviceResult.data
            is ServiceResult.Error -> arrayListOf()
            else -> throw NotImplementedError()
        }
    }

}
