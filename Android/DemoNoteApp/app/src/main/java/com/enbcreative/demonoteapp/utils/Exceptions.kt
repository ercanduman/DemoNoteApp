package com.enbcreative.demonoteapp.utils

import java.io.IOException

class ApiException(message: String) : IOException(message)
class NoNetworkExceptions(message: String) : IOException(message)