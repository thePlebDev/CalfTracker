package com.elliottsoftware.calftracker.domain.models.fireBase

import com.elliottsoftware.calftracker.domain.models.DataPoint
import java.util.*

//TODO THE GETTERS AND SETTERS USED BY FIREBASE ARE CASE SENSITIVE
data class FireBaseCalf(var calftag: String? = null,
                        var cowtag:String? = null,
                        var ccianumber: String? = null,
                        var sex:String? = null,
                        var details:String?=null,
                        var date: Date? = null,
                        var birthweight:String? = null,
                        var id: String? = null,
                        var vaccinelist:List<String>? = null
                        ) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}


/**
 *  Function that allows the conversion from a list of FireBaseCalf to a single DataPoint
 *
 * @param calfList List of FireBaseCalf
 * @param index an int value to be given to the as the DataPoint's x coordinate
 * @return DataPoint, a individual point to be plotted on the graph
 */
fun calfListToDataPoint(calfList:List<FireBaseCalf>,index:Int): DataPoint {
    val dataPoint = DataPoint(index.toFloat(),calfList.size.toFloat(), mutableListOf())
    calfList.forEach{
        dataPoint.calves.add(it)
    }
//    calfList.subList(1,calfList.size).forEach{
//        dataPoint.calves.add(it)
//    }

    return dataPoint
}

/**
 * * Function that takes in the a random ordering of List<FireBaseCalf>. The individual implementations of
 * FireBaseCalf are then grouped by date and then converted to a List<DataPoint>, which will then be used to
 * create a line graph
 *
 * @param calfList List of FireBaseCalf
 * @return List<DataPoint> the values to be plotted on the graph
 */
fun calfListToDataPointList(calfList: List<FireBaseCalf>):List<DataPoint>{
    //a list converted to a Map and grouped by `it.date`. Also converted back to a Pair based list with `.toList()`
    val calfListSortedByDate = calfList.groupBy { it.date }.toList()

    // calfListSortedByDate converted to a List<List<FireBaseCalf>>
    val listPairToNestedList = calfListSortedByDate.map { it.second }

    //accessing the nested list through the mapIndexed{} and returning the wanted List by calling calfListToDataPoint
    val finalList = listPairToNestedList.mapIndexed{ index,list ->
        calfListToDataPoint(list,index)
    }
    return finalList
}