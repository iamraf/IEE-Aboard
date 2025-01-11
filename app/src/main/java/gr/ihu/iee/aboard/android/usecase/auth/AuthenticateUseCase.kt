/*
 * Copyright (C) 2020-2025 Raf
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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