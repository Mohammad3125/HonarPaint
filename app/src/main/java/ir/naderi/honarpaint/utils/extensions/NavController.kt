package ir.naderi.honarpaint.utils.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import kotlin.let

fun NavController.checkDuplicateAndNavigate(direction: NavDirections, destinationId: Int) {
    currentDestination?.let { dst ->
        if (destinationId == dst.id) {
            navigate(direction)
        }
        return
    }
    navigate(direction)
}
