package it.dbortoluzzi.tuttiapposto.framework

import it.dbortoluzzi.data.TablePersistenceSource
import it.dbortoluzzi.data.dto.TableAvailableRequestDto
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.api.ApiHelper
import it.dbortoluzzi.tuttiapposto.api.toServiceResult
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidTableSource @Inject constructor(
        var apiHelper: ApiHelper
) : TablePersistenceSource {

    override suspend fun getAvailableTables(tableAvailableRequestDto: TableAvailableRequestDto): ServiceResult<List<Table>> {
        return apiHelper
                .findAvailableTable(tableAvailableRequestDto.companyId, tableAvailableRequestDto.buildingId, tableAvailableRequestDto.roomId, tableAvailableRequestDto.startDate, tableAvailableRequestDto.endDate)
                .toServiceResult()

    }

    companion object {
        private val TAG = "TablePersistenceSource"
    }

}