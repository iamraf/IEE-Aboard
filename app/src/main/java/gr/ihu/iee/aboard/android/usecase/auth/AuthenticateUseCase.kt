package gr.ihu.iee.aboard.android.usecase.auth

import gr.ihu.iee.aboard.android.domain.auth.AuthDataSource
import gr.ihu.iee.aboard.android.domain.auth.entity.Auth
import javax.inject.Inject

class AuthenticateUseCase @Inject constructor(
    private val dataSource: AuthDataSource
) {

    suspend operator fun invoke(code: String): Auth {
        return dataSource.authenticate(code)
    }
}