package tj.winditask.data.remote.request

import com.google.gson.annotations.SerializedName

data class PhoneRequest(
    @SerializedName("phone")
    val phone: String
)

data class PhoneWithCodeRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("code")
    val code: String
)

data class RegisterRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String
)