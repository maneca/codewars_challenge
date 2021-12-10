package com.example.codewarschallenge.utils

import okio.IOException


class NoConnectivityException : IOException()

class UnknownException : IOException()

class ApiNotResponding : IOException()