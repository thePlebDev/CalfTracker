package com.elliottsoftware.calftracker.presentation.components.login

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.sp

@ExperimentalMaterialApi
enum class ModalSideSheetValue {
    /**
     * The bottom sheet is not visible.
     */
    Hidden,

    /**
     * The bottom sheet is visible at full height.
     */
    Expanded,

    /**
     * The bottom sheet is partially visible at 50% of the screen height. This state is only
     * enabled if the height of the bottom sheet is more than 50% of the screen height.
     */
    HalfExpanded
}


@Composable
@ExperimentalMaterialApi
fun ModalSideSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier:Modifier = Modifier,
    isVisible: Boolean,
    sheetState: ModalSideSheetState = rememberModalSideSheetState(initialValue = ModalSideSheetValue.Expanded),
    content: @Composable () -> Unit
){
    val scope = rememberCoroutineScope()
    BoxWithConstraints(modifier) {
        val fullHeight = constraints.maxHeight.toFloat()
        val fullWidth = constraints.maxWidth.toFloat()
        val sheetHeightState = remember { mutableStateOf<Float?>(null) }
        val squareSize = 48.dp
        val sizePx = with(LocalDensity.current) { squareSize.toPx() }

        Box(Modifier.fillMaxSize()) {
            content()

            Scrim(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.32f),
                onDismiss = {
//                    if (sheetState.confirmStateChange(Hidden)) {
//                        scope.launch { sheetState.hide() }
//                    }
                },
                visible = isVisible
            )
        }
        Surface(
            Modifier
                .fillMaxWidth()
                //todo: TO MAKE THIS MOVE, I JUST MIGHT HAVE TO CHANGE THE X AND THE Y VALUES
                    //sheetState.offset.value.roundToInt()
                .offset {
                    IntOffset(sheetState.offset.value.roundToInt(), 0)

                }
                .bottomSheetSwipeable(sheetState, fullWidth, sheetHeightState)
                .onGloballyPositioned {
                    sheetHeightState.value = it.size.height.toFloat()
                }
                .semantics {

                },

        ) {
            Column(Modifier.fillMaxSize()) {
                sheetContent()
            }

        }
    }
}

@Suppress("ModifierInspectorInfo")
@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.bottomSheetSwipeable(
    sheetState: ModalSideSheetState,
    fullHeight: Float,
    sheetHeightState: State<Float?>,
): Modifier {
    val sheetHeight = sheetHeightState.value

    val modifier = if (sheetHeight != null) {
//        val anchors = if (sheetHeight < fullHeight / 2) {
//            mapOf(
//                fullHeight to ModalSideSheetValue.Hidden,
//                fullHeight - sheetHeight to ModalSideSheetValue.Expanded
//            )
//        } else {
        val anchors =  mapOf(
                fullHeight to ModalSideSheetValue.Hidden,
                fullHeight / 2 to ModalSideSheetValue.HalfExpanded,
                max(0f, fullHeight - sheetHeight) to ModalSideSheetValue.Expanded
            )
       // }
        Modifier.swipeable(
            state = sheetState,
            anchors = anchors,
            orientation = Orientation.Horizontal,
            enabled = sheetState.currentValue != ModalSideSheetValue.Hidden,
            resistance = null
        )
    } else {
        Modifier
    }

    return this.then(modifier)
}

/**
 * Holds the state of our [ModalSideSheetLayout][ModalSideSheetLayout]
 */
@ExperimentalMaterialApi
class ModalSideSheetState(
    initialValue: ModalSideSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (ModalSideSheetValue) -> Boolean = { true }

): SwipeableState<ModalSideSheetValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
){
    /**
     * Show the bottom sheet with animation and suspend until it's shown. If half expand is
     * enabled, the bottom sheet will be half expanded. Otherwise it will be fully expanded.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun show() {
        val targetValue = ModalSideSheetValue.Expanded
        animateTo(targetValue = targetValue)
    }
    /**
     * Hide the bottom sheet with animation and suspend until it if fully hidden or animation has
     * been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun hide() = animateTo(ModalSideSheetValue.Hidden)

    companion object {
        /**
         * The default [Saver] implementation for [ModalBottomSheetState].
         */
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (ModalSideSheetValue) -> Boolean
        ): Saver<ModalSideSheetState, *> = Saver(
            save = { it.currentValue },
            restore = {
                ModalSideSheetState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    confirmStateChange = confirmStateChange
                )
            }
        )
    }

}

@Composable
@ExperimentalMaterialApi
fun rememberModalSideSheetState(
    initialValue: ModalSideSheetValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (ModalSideSheetValue) -> Boolean = { true }
): ModalSideSheetState {
    return rememberSaveable(
        saver = ModalSideSheetState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    ) {
        ModalSideSheetState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    }
}





@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        val dismissModifier = if (visible) {
            Modifier
                .pointerInput(onDismiss) { detectTapGestures { onDismiss() } }
                .semantics(mergeDescendants = true) {
                    contentDescription = "closeSheet"
                    onClick { onDismiss(); true }
                }
        } else {
            Modifier
        }

        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}
