package edu.puo.foodonus.repository

import edu.puo.foodonus.model.User
import edu.puo.foodonus.utils.Resource

interface AuthRepository {
    //suspend fun saveUserDetails(user: User, result: (Resource<User>) -> Unit)
    suspend fun login(email: String, password: String, result: (Resource<String>) -> Unit)
    suspend fun register(email: String, password: String, user: User,result: (Resource<String>) -> Unit)
    suspend fun logout(result: () -> Unit)
}