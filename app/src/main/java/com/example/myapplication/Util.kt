package com.example.myapplication

import org.json.JSONObject

// Returns JSONObject if valid, null otherwise
fun Any.validSocketArguments(): JSONObject? {
    if (this is Array<*> && this.isNotEmpty() && this[0] is JSONObject) {
        return this[0] as JSONObject
    }
    return null
}