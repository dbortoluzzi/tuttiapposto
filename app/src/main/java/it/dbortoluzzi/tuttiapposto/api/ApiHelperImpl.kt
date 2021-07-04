package it.dbortoluzzi.tuttiapposto.api

import it.dbortoluzzi.domain.dto.TableAvailabilityRequestDto
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException
import java.util.*
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
        private val apiService: ApiService,
) : ApiHelper {
    override suspend fun findAvailableTable(companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date): Response<List<TableAvailabilityResponseDto>> {
        return safeCall {
            apiService.findAvailableTable(TableAvailabilityRequestDto(companyId, buildingId, roomId, startDate, endDate))
        }
    }

    protected suspend fun <T> safeCall (callback : suspend (() -> Response<T>)) : Response<T> {
        try {
            return callback()
        }catch (e: IOException) {
            val errorResponseBody = (e.message ?: "network error").toResponseBody(null);
            return Response.error(500, errorResponseBody);
        }
    }
}

fun <T : Any> Response<T>.toServiceResult(): ServiceResult<T> {
    return if (isSuccessful) {
        ServiceResult.Success(body()!!)
    } else {
        ServiceResult.Error(errorBody().toString())
    }
}