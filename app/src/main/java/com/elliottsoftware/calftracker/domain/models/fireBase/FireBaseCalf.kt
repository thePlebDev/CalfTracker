package com.elliottsoftware.calftracker.domain.models.fireBase

import java.util.*

//TODO THE BETTERS AND SETTERS USED BY FIREBASE ARE CASE SENSITIVE
data class FireBaseCalf(val calfTag: String? = null,
                        val cowTag:String? = null,
                        val cciaNumber: String? = null,
                        val sex:String? = null,
                        val details:String?=null,
                        val date: Date? = null,
                        val birthWeight:String? = null,
                        var id: String? = null,

                        ) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
