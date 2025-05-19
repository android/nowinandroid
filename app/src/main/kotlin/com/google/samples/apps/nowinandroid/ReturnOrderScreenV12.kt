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
import com.google.samples.apps.nowinandroid.RefundMethodTypeV12.MEESHO_APP
import com.google.samples.apps.nowinandroid.SubmissionStateV12.Error
import com.google.samples.apps.nowinandroid.SubmissionStateV12.Idle
import com.google.samples.apps.nowinandroid.SubmissionStateV12.Loading
import com.google.samples.apps.nowinandroid.SubmissionStateV12.Success

data class OrderItemV12(
    val id: String,
    val name: String,
    val imageUrl: Int,
    val price: Int,
    val size: String,
    val quantity: Int
)

enum class RefundMethodTypeV12 {
    MEESHO_APP, BANK_UPI
}

data class RefundOptionV12(
    val id: RefundMethodTypeV12,
    val title: String,
    val subtitle: String? = null,
    val icon: Int,
    val getAmount: Int,
    val extraAmount: Int? = null,
    val isSelected: Boolean = false
)

class ReturnOrderStateV12(
    initialOrderItem: OrderItemV12,
    initialRefundOptions: List<RefundOptionV12>,
    initialSelectedOption: RefundMethodTypeV12? = null
) {
    var orderItem by mutableStateOf(initialOrderItem)
        private set

    var refundOptions by mutableStateOf(initialRefundOptions)
        private set

    var selectedRefundMethod by mutableStateOf(initialSelectedOption)
        private set

    var submissionState by mutableStateOf<SubmissionStateV12>(Idle)
        private set

    fun selectRefundMethod(type: RefundMethodTypeV12) {
        selectedRefundMethod = type
        refundOptions = refundOptions.map {
            it.copy(isSelected = it.id == type)
        }
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
        println("Submitting refund for method: $selectedRefundMethod")
        submissionState = Success
    }

    fun resetSubmissionState() {
        submissionState = Idle
    }
}

@Composable
fun rememberReturnOrderStateV12(
    initialOrderItem: OrderItemV12,
    initialRefundOptions: List<RefundOptionV12>,
    initialSelectedOption: RefundMethodTypeV12? = MEESHO_APP
): ReturnOrderStateV12 {
    return remember {
        ReturnOrderStateV12(
            initialOrderItem = initialOrderItem,
            initialRefundOptions = initialRefundOptions.map {
                it.copy(isSelected = it.id == initialSelectedOption)
            },
            initialSelectedOption = initialSelectedOption
        )
    }
}

sealed class SubmissionStateV12 {
    object Idle : SubmissionStateV12()
    object Loading : SubmissionStateV12()
    object Success : SubmissionStateV12()
    data class Error(val message: String) : SubmissionStateV12()
}

@Composable
fun ReturnOrderScreenV12(
    state: ReturnOrderStateV12,
    onNavigateBack: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.submissionState) {
        when (val submission = state.submissionState) {
            is Error -> {
                snackbarHostState.showSnackbar(
                    message = submission.message,
                    duration = SnackbarDuration.Short
                )
            }
            is Success -> {
                snackbarHostState.showSnackbar(
                    message = "Refund submitted successfully!",
                    duration = SnackbarDuration.Short
                )
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            SubmitButtonV12(
                isLoading = state.submissionState == Loading,
                isEnabled = state.selectedRefundMethod != null && state.submissionState != Loading,
                onClick = { state.submitRefund() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            OrderItemSummaryV12(item = state.orderItem)
            Spacer(modifier = Modifier.height(24.dp))
            RefundMethodSelectionV12(
                options = state.refundOptions,
                selectedMethod = state.selectedRefundMethod,
                onOptionSelected = { state.selectRefundMethod(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (state.selectedRefundMethod == MEESHO_APP) {
                MeeshoInfoBoxV12(onLearnMoreClick = { /* TODO */ })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun OrderItemSummaryV12(item: OrderItemV12) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.imageUrl),
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
fun RefundMethodSelectionV12(
    options: List<RefundOptionV12>,
    selectedMethod: RefundMethodTypeV12?,
    onOptionSelected: (RefundMethodTypeV12) -> Unit
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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            options.forEach { option ->
                RefundOptionCardV12(
                    option = option.copy(isSelected = option.id == selectedMethod),
                    onClick = { onOptionSelected(option.id) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@SuppressLint("DesignSystem")
@Composable
fun RefundOptionCardV12(
    option: RefundOptionV12,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (option.isSelected) Color(0xFF8A2BE2) else Color.LightGray
    val borderWidth = if (option.isSelected) 1.5.dp else 1.dp

    Card(
        modifier = modifier
            .border(borderWidth, borderColor, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
    ) {
        Box {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Icon(
                    painter = painterResource(id = option.icon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Unspecified
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
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                Text(
                    "Get ₹${option.getAmount}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                option.extraAmount?.let { extra ->
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .background(Color(0xFFE6F9E6), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "Extra ₹$extra",
                            color = Color(0xFF006400),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                if (option.extraAmount == null) {
                    Spacer(modifier = Modifier.height(28.dp))
                }
            }

            if (option.isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color(0xFF8A2BE2),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(20.dp)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}

@Composable
fun MeeshoInfoBoxV12(onLearnMoreClick: () -> Unit) {
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
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            textDecoration = TextDecoration.Underline
                        )
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
fun SubmitButtonV12(
    isLoading: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(48.dp),
        enabled = isEnabled && !isLoading,
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