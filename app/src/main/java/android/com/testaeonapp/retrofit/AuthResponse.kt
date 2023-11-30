package android.com.testaeonapp.retrofit

data class AuthResponse (
    var success: String,
    var response: ResponseToken?,
    var error: ResponseError?
)

data class ResponseToken (
    var token : String?
)

data class ResponseError (
    var error_code : Int?,
    var error_msg: String?
)


/*
{
    "success": "true",
    "response": {
    "token": "7b7c0a690bee2e8d90512ed1b57e19f0"

{
    "success": "false",
    "error": {
        "error_code": 1003,
        "error_msg": "User authorization failed: incorrect credentials."
    }
}


}
*/