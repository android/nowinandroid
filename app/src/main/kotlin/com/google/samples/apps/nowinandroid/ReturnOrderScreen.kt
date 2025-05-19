package com.google.samples.apps.nowinandroid

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.samples.apps.nowinandroid.RefundMethodType.MEESHO_APP
import com.google.samples.apps.nowinandroid.SubmissionState.Error
import com.google.samples.apps.nowinandroid.SubmissionState.Idle
import com.google.samples.apps.nowinandroid.SubmissionState.Loading
import com.google.samples.apps.nowinandroid.SubmissionState.Success

data class OrderItem(
    val id: String,
    val name: String,
    val imageUrl: Int, // Using Drawable Res ID for simplicity
    val price: Int,
    val size: String,
    val quantity: Int
)

// Representing different refund methods
enum class RefundMethodType {
    MEESHO_APP, BANK_UPI
}

data class RefundOption(
    val id: RefundMethodType,
    val title: String,
    val subtitle: String? = null,
    val icon: Int, // Drawable Res ID
    val getAmount: Int,
    val extraAmount: Int? = null,
    val isSelected: Boolean = false
)

// --- State Holder (Can be hoisted to a ViewModel in a real app) ---
// This manages the state of the screen
class ReturnOrderState(
    initialOrderItem: OrderItem,
    initialRefundOptions: List<RefundOption>,
    initialSelectedOption: RefundMethodType? = null // Start with no selection or pre-select
) {
    var orderItem by mutableStateOf(initialOrderItem)
        private set // Can only be modified internally or via specific functions

    var refundOptions by mutableStateOf(initialRefundOptions)
        private set

    var selectedRefundMethod by mutableStateOf(initialSelectedOption)
        private set

    var submissionState by mutableStateOf<SubmissionState>(Idle)
        private set

    fun selectRefundMethod(type: RefundMethodType) {
        selectedRefundMethod = type
        refundOptions = refundOptions.map {
            it.copy(isSelected = it.id == type)
        }
        // Reset submission state if user changes selection after an error
        if (submissionState is Error) {
            submissionState = Idle
        }
    }

    fun submitRefund() {
        if (selectedRefundMethod == null) {
            submissionState = Error("Please select a refund method.")
            return
        }
        submissionState = Loading
        // --- Simulate Network Call ---
        // In a real app, launch a coroutine and call repository/usecase
        // viewModelScope.launch { ... }
        // For now, just simulate success/failure after a delay
        // Handler(Looper.getMainLooper()).postDelayed({
        //      submissionState = if (Random.nextBoolean()) SubmissionState.Success
        //                      else SubmissionState.Error("Refund submission failed. Please try again.")
        // }, 2000)
        println("Submitting refund for method: $selectedRefundMethod")
        // For preview, let's just set to Success immediately for simplicity
        submissionState = Success
    }

    fun resetSubmissionState() {
        submissionState = Idle
    }
}

@Composable
fun rememberReturnOrderState(
    initialOrderItem: OrderItem,
    initialRefundOptions: List<RefundOption>,
    initialSelectedOption: RefundMethodType? = MEESHO_APP // Pre-select Meesho
): ReturnOrderState {
    return remember {
        ReturnOrderState(
            initialOrderItem = initialOrderItem,
            initialRefundOptions = initialRefundOptions.map {
                it.copy(isSelected = it.id == initialSelectedOption) // Set initial selection state
            },
            initialSelectedOption = initialSelectedOption
        )
    }
}

// --- Sealed class for Submission State (more robust error/loading handling) ---
sealed class SubmissionState {
    object Idle : SubmissionState()
    object Loading : SubmissionState()
    object Success : SubmissionState()
    data class Error(val message: String) : SubmissionState()
}


// --- Main Screen Composable ---

@Composable
fun ReturnOrderScreen(
    state: ReturnOrderState,
    onNavigateBack: () -> Unit,
    // In a real app, submission would likely trigger a ViewModel function
    // onSubmit: (RefundMethodType) -> Unit
) {
    val snackbarHostState = SnackbarHostState()

    // Handle submission state changes (e.g., show Snackbar for errors)
    LaunchedEffect(state.submissionState) {
        when (val submission = state.submissionState) {
            is Error -> {
                snackbarHostState.showSnackbar(
                    message = submission.message,
                    duration = SnackbarDuration.Short
                )
                // Optionally reset state after showing error
                // state.resetSubmissionState()
            }
            is Success -> {
                snackbarHostState.showSnackbar(
                    message = "Refund submitted successfully!",
                    duration = SnackbarDuration.Short
                )
                // Potentially navigate away or show success screen
                // onNavigateBack() // Example: Navigate back on success
            }
            else -> {} // Idle or Loading
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            SubmitButton(
                isLoading = state.submissionState == Loading,
                isEnabled = state.selectedRefundMethod != null && state.submissionState != Loading,
                onClick = { state.submitRefund() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply scaffold padding
                .verticalScroll(rememberScrollState()) // Make content scrollable
                .padding(bottom = 80.dp) // Add padding to avoid overlap with bottom bar
        ) {
            OrderItemSummary(item = state.orderItem)
            Spacer(modifier = Modifier.height(24.dp))
            RefundMethodSelection(
                options = state.refundOptions,
                selectedMethod = state.selectedRefundMethod,
                onOptionSelected = { state.selectRefundMethod(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Show info box only when Meesho App refund is selected
            if (state.selectedRefundMethod == MEESHO_APP) {
                MeeshoInfoBox(onLearnMoreClick = { /* TODO: Handle Learn More click */ })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun OrderItemSummary(item: OrderItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.imageUrl), // Replace with actual image loading
            contentDescription = item.name,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    "₹${item.price}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("•", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Size: ${item.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("•", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Qty: ${item.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun RefundMethodSelection(
    options: List<RefundOption>,
    selectedMethod: RefundMethodType?,
    onOptionSelected: (RefundMethodType) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Choose your refund method",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Spacing between cards
        ) {
            options.forEach { option ->
                RefundOptionCard(
                    option = option.copy(isSelected = option.id == selectedMethod), // Ensure isSelected reflects state
                    onClick = { onOptionSelected(option.id) },
                    modifier = Modifier.weight(1f) // Make cards share width equally
                )
            }
        }
    }
}

@SuppressLint("DesignSystem")
@Composable
fun RefundOptionCard(
    option: RefundOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (option.isSelected) Color(0xFF8A2BE2) else Color.LightGray // Purple when selected
    val borderWidth = if (option.isSelected) 1.5.dp else 1.dp
    val backgroundColor = Color.White // Or MaterialTheme.colors.surface

    Card(
        modifier = modifier
            .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)) // Clip needed for click effect ripple
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box { // Use Box to overlay the checkmark
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), // Ensure column takes width for alignment
                horizontalAlignment = Alignment.Start // Align content to the start
            ) {
                Icon(
                    painter = painterResource(id = option.icon),
                    contentDescription = null, // Decorative icon
                    modifier = Modifier.size(28.dp), // Adjust size as needed
                    tint = Color.Unspecified // Use original icon colors if applicable
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    option.title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                option.subtitle?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.5f)) // Subtle divider
                Text(
                    "Get ₹${option.getAmount}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                option.extraAmount?.let { extra ->
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .background(
                                Color(0xFFE6F9E6),
                                RoundedCornerShape(4.dp)
                            ) // Light green background
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "Extra ₹$extra",
                            color = Color(0xFF006400), // Dark green text
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                // Add Spacer at the bottom if extraAmount is null to maintain some height consistency
                if (option.extraAmount == null) {
                    Spacer(modifier = Modifier.height(28.dp)) // Adjust height to match the 'Extra' box approx
                }
            }

            // Selection Indicator (Checkmark)
            if (option.isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color(0xFF8A2BE2), // Purple checkmark
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(20.dp)
                        .background(Color.White, CircleShape) // White background behind check
                )
            }
        }
    }
}

@Composable
fun MeeshoInfoBox(onLearnMoreClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Use it on your next order",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = Color.White,
                        textDecoration = TextDecoration.Underline)
                    ) {
                        append("Learn more")
                    }
                },
                modifier = Modifier.clickable(onClick = onLearnMoreClick),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@SuppressLint("DesignSystem")
@Composable
fun SubmitButton(
    isLoading: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Padding around the button
            .height(48.dp), // Standard button height
        enabled = isEnabled && !isLoading, // Disable if not selected OR loading
        shape = RoundedCornerShape(8.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                "Submit",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
