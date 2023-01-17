package com.elliottsoftware.calftracker.util

import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.domain.models.fireBase.calfListToDataPoint
import com.elliottsoftware.calftracker.domain.models.fireBase.groupedCalfListToDataPointList
import com.elliottsoftware.calftracker.presentation.components.util.DataPoint
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.util.*

class DataPointMapperTest {


    @Test
    fun firebaseCalfList_to_DatapointList() {
        /**GIVEN**/
        val date = Date(2018,3,4)
        val date1 = Date(2019,3,4)
        val calf = FireBaseCalf("1",
            "22d2",
            "22d2ddrew4r","Bull","STUFF AND THINGS", date,"222"
        )
        val calf1 = FireBaseCalf("2",
            "22d2",
            "22d2ddrew4r","Bull","STUFF AND THINGS", date1,"222"
        )
        val calfList = listOf(calf,calf,calf1)

        /**WHEN**/
       val sorted =  groupedCalfListToDataPointList(calfList)

        /**THEN**/
        Assert.assertEquals(2, sorted[0].calves.size)

    }


}

