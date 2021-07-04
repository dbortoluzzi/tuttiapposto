package it.dbortoluzzi.tuttiapposto.ui.util

object Constants {
    val PROD_BASE_URL = "https://mysterious-plateau-02939.herokuapp.com/"
//    val DEV_BASE_URL = "http://192.168.1.60:9091/"
    val DEV_BASE_URL = PROD_BASE_URL
    val DEFAULT_DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss"

    val READ_SECONDS_TIMEOUT_RETROFIT = 10L
    val WRITE_SECONDS_TIMEOUT_RETROFIT = 10L
}