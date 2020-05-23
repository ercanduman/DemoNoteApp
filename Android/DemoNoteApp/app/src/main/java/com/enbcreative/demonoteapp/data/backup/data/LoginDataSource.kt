package com.enbcreative.demonoteapp.data.backup.data

import com.enbcreative.demonoteapp.DUMMY_USER_MAIL
import com.enbcreative.demonoteapp.DUMMY_USER_NAME
import com.enbcreative.demonoteapp.DUMMY_USER_PASSWORD
import com.enbcreative.demonoteapp.data.backup.data.model.LoggedInUser
import java.io.IOException
import java.security.InvalidParameterException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(userEmail: String, password: String): Result<LoggedInUser> {
        return try {
            if (userEmail == DUMMY_USER_MAIL && password == DUMMY_USER_PASSWORD) {
                val fakeUser = LoggedInUser(
                    java.util.UUID.randomUUID().toString(),
                    DUMMY_USER_NAME,
                    DUMMY_USER_MAIL,
                    DUMMY_USER_PASSWORD
                )
                Result.Success(fakeUser)
            } else {
                Result.Error(InvalidParameterException("Invalid E-mail or password"))
            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }
}

