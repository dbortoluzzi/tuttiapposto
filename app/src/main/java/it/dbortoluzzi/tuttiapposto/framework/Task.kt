package it.dbortoluzzi.tuttiapposto.framework

import com.google.android.gms.tasks.Task
import it.dbortoluzzi.domain.util.ServiceResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <TResult : Any> Task<TResult>.await(): ServiceResult<TResult>
{
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                ServiceResult.Error(CancellationException("Task $this was cancelled normally."))
            }
            else {
                ServiceResult.Success(result as TResult)
            }
        }
        else {
            ServiceResult.Error(e)
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null) {
                @Suppress("UNCHECKED_CAST")
                if (isCanceled) {
                    cont.cancel()
                }
                else {
                    cont.resume(ServiceResult.Success(result as TResult))
                }
            }
            else {
                cont.resumeWithException(e)
            }
        }
    }
}

suspend fun Task<Void>.awaitAny(): ServiceResult<Boolean>
{
    if (isComplete) {
        val e = exception
        return if (e == null) {
            if (isCanceled) {
                ServiceResult.Error(CancellationException("Task $this was cancelled normally."))
            }
            else {
                ServiceResult.Success(true)
            }
        }
        else {
            ServiceResult.Error(e)
        }
    }

    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            val e = exception
            if (e == null) {
                @Suppress("UNCHECKED_CAST")
                if (isCanceled) {
                    cont.cancel()
                }
                else {
                    cont.resume(ServiceResult.Success(false))
                }
            }
            else {
                cont.resumeWithException(e)
            }
        }
    }
}