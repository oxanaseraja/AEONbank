package android.com.testaeonapp.retrofit

data class TransactionResponse (
    var success  : String? = null,
    var response : ArrayList<Transactions> = arrayListOf()
)

data class Transactions (
    var id : String? = null,
    var title : String? = null,
    var amount : String? = null,
    var created : String? = null

)
/*
{
    "success": "true",
    "response": [
        {
            "id": 1,
            "title": "Test Payment 1",
            "amount": 101,
            "created": 1683904809
        },
    ]
}
 */