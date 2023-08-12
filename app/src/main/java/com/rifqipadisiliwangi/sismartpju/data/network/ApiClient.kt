package com.rifqipadisiliwangi.sismartpju.data.network


//object  ApiClient {
//
//    private const val BASE_URL = "https://sisemarpju.smartlinks.id/"
//    private val httpClient = OkHttpClient.Builder().addInterceptor(AuthInterceptor())
//
//    private val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(httpClient.build())
//        .build()

//  val apiService: ApiService = retrofit.create(ApiService::class.java)
//
//    private val BASE_URL = "https://sisemarpju.smartlinks.id/"
//    //add BasicAuthInterceptor to OkHttp client
//    val client =  OkHttpClient.Builder()
//        .addInterceptor(BasicAuthInterceptor("demo@demo.com",     "12345678"))
//        .build()
//    val gson = GsonBuilder()
//        .setLenient()
//        .create();
//    // add OkHttp client to Retrofit instance
//    private val api = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .client(client)
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .build()
//        .create(PosApi::class.java)
//
//    fun sendAmount(amount: Double, msisdn: String): Single<LoginResponseItem> {
//        return api.sendAmount(amount, msisdn)
//
//
//
//}