package com.developer.ivan.coreapp.data.server

import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.flatMapToRight
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Response

@Suppress("BlockingMethodInNonBlockingContext")
interface NetworkManager {

    //    class JsonSyntaxFailure(val message: String?): Failure.CustomFailure()
    class ServerResponseException(val errorCode: Int, val message: String?) :
        Failure.CustomFailure()

    class UnexpectedServerError(val errorCode: Int, val message: String?) : Failure.CustomFailure()
    class EmptyBody() : Failure.CustomFailure()


    suspend fun <T, R> safeRequest(
        callRequest: Response<T>,
        functionCall: (Either.Right<T>) -> Either<Failure, R>
    ): Either<Failure, R>


    @ExperimentalCoroutinesApi
    class NetworkImplementation : NetworkManager {

        override suspend fun <T, R> safeRequest(
            callRequest: Response<T>,
            functionCall: (Either.Right<T>) -> Either<Failure, R>
        ): Either<Failure, R> {


            return ((if (callRequest.isSuccessful) {
                val body = callRequest.body()

                if (body != null)
                    Either.Right(body)
                else
                    Either.Left(EmptyBody())

            } else {
                when (callRequest.code()) {
                    in 100..300 -> Either.Left(
                        ServerResponseException(
                            callRequest.code(),
                            callRequest.errorBody()?.string()
                        )
                    )
                    else -> Either.Left(
                        UnexpectedServerError(
                            callRequest.code(),
                            callRequest.errorBody()?.string()
                        )
                    )
                }
            }).flatMapToRight { rightResult -> functionCall.invoke(Either.Right(rightResult)) })

        }

    }


}
