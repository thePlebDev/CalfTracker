package com.elliottsoftware.calftracker.domain.models.fireBase

import java.util.*

data class FireBaseCalf(val calfTag: String? = null,
                        val cowTag:String? = null,
                        val CCIANumber: String? = null,
                        val sex:String? = null,
                        val details:String?=null,
                        val date: Date? = null,
                        val birthWeight:String? = null

) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
