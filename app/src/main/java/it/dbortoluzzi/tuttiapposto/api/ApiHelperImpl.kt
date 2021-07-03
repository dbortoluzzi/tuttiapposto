package it.dbortoluzzi.tuttiapposto.api

import it.dbortoluzzi.data.dto.TableAvailableRequestDto
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
        private val apiService: ApiService,
) : ApiHelper {
    override suspend fun findAvailableTable(companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date): Response<List<Table>> {
        return apiService.findAvailableTable(TableAvailableRequestDto(companyId, buildingId, roomId, startDate, endDate))
    }

}

fun <T : Any> Response<T>.toServiceResult(): ServiceResult<T> {
    return if (isSuccessful) {
        ServiceResult.Success(body()!!)
    } else {
        ServiceResult.Error(errorBody().toString())
    }
}