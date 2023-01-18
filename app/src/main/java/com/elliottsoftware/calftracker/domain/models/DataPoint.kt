package com.elliottsoftware.calftracker.domain.models

import com.elliottsoftware.calftracker.domain.models.fireBase.FireBaseCalf
import com.elliottsoftware.calftracker.presentation.components.util.LineGraph

/**
 * Denotes a point in the [LineGraph].
 *
 * @param x the x coordinate or the number in the x axis
 * @param y the y coordinate or the number in the y axis
 * @param calves list of [FireBaseCalf] assigned to the individual plot point
 */
data class DataPoint(val x: Float, val y: Float,val calves:MutableList<FireBaseCalf>)