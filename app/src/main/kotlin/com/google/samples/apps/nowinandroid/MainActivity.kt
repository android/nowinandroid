/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.metrics.performance.JankStats
import androidx.tracing.trace
import com.google.samples.apps.nowinandroid.MainActivityUiState.Loading
import com.google.samples.apps.nowinandroid.core.analytics.AnalyticsHelper
import com.google.samples.apps.nowinandroid.core.analytics.LocalAnalyticsHelper
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.data.util.TimeZoneMonitor
import com.google.samples.apps.nowinandroid.core.designsystem.theme.NiaTheme
import com.google.samples.apps.nowinandroid.core.ui.LocalTimeZone
import com.google.samples.apps.nowinandroid.ui.NiaApp
import com.google.samples.apps.nowinandroid.ui.rememberNiaAppState
import com.google.samples.apps.nowinandroid.util.isSystemInDarkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Lazily inject [JankStats], which is used to track jank throughout the app.
     */
    @Inject
    lateinit var lazyStats: dagger.Lazy<JankStats>

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var timeZoneMonitor: TimeZoneMonitor

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var userNewsResourceRepository: UserNewsResourceRepository

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // We keep this as a mutable state, so that we can track changes inside the composition.
        // This allows us to react to dark/light mode changes.
        var themeSettings by mutableStateOf(
            ThemeSettings(
                darkTheme = resources.configuration.isSystemInDarkTheme,
                androidTheme = Loading.shouldUseAndroidTheme,
                disableDynamicTheming = Loading.shouldDisableDynamicTheming,
            ),
        )

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkTheme(),
                    viewModel.uiState,
                ) { systemDark, uiState ->
                    ThemeSettings(
                        darkTheme = uiState.shouldUseDarkTheme(systemDark),
                        androidTheme = uiState.shouldUseAndroidTheme,
                        disableDynamicTheming = uiState.shouldDisableDynamicTheming,
                    )
                }
                    .onEach { themeSettings = it }
                    .map { it.darkTheme }
                    .distinctUntilChanged()
                    .collect { darkTheme ->
                        trace("niaEdgeToEdge") {
                            // Turn off the decor fitting system windows, which allows us to handle insets,
                            // including IME animations, and go edge-to-edge.
                            // This is the same parameters as the default enableEdgeToEdge call, but we manually
                            // resolve whether or not to show dark theme using uiState, since it can be different
                            // than the configuration's dark theme value based on the user preference.
                            enableEdgeToEdge(
                                statusBarStyle = SystemBarStyle.auto(
                                    lightScrim = android.graphics.Color.TRANSPARENT,
                                    darkScrim = android.graphics.Color.TRANSPARENT,
                                ) { darkTheme },
                                navigationBarStyle = SystemBarStyle.auto(
                                    lightScrim = lightScrim,
                                    darkScrim = darkScrim,
                                ) { darkTheme },
                            )
                        }
                    }
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition { viewModel.uiState.value.shouldKeepSplashScreen() }

        setContent {
            val appState = rememberNiaAppState(
                networkMonitor = networkMonitor,
                userNewsResourceRepository = userNewsResourceRepository,
                timeZoneMonitor = timeZoneMonitor,
            )

            val currentTimeZone by appState.currentTimeZone.collectAsStateWithLifecycle()

            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
                LocalTimeZone provides currentTimeZone,
            ) {
                NiaTheme(
                    darkTheme = themeSettings.darkTheme,
                    androidTheme = themeSettings.androidTheme,
                    disableDynamicTheming = themeSettings.disableDynamicTheming,
                ) {
                    NiaApp(appState)
                    MySootheAppPortrait()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lazyStats.get().isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        lazyStats.get().isTrackingEnabled = false
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

/**
 * Class for the system theme settings.
 * This wrapping class allows us to combine all the changes and prevent unnecessary recompositions.
 */
data class ThemeSettings(
    val darkTheme: Boolean,
    val androidTheme: Boolean,
    val disableDynamicTheming: Boolean,
)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        SearchBar(Modifier.padding(horizontal = 16.dp))
        HomeSection(title ="Align Your Body") {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // This is a simplified helper. For a production app,
                // you'd use a robust number-to-words utility or a pre-generated list.
                fun numberToWords(n: Int): String {
                    val units = listOf("", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")
                    val tens = listOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

                    return when {
                        n < 0 -> "Minus " + numberToWords(-n)
                        n == 0 -> "Zero" // Though the loop is 1..100
                        n < 20 -> units[n]
                        n < 100 -> {
                            val tenPart = tens[n / 10]
                            val unitPart = units[n % 10]
                            if (unitPart.isNotEmpty()) "$tenPart $unitPart" else tenPart
                        }
                        n == 100 -> "One Hundred"
                        else -> n.toString() // Fallback for numbers > 100, though not needed for this loop
                    }
                }

                for (i in 1..100) {
                    Text(
                        text = numberToWords(i),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
        HomeSection(title = "Favorite Collections") {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // This is a simplified helper. For a production app,
                // you'd use a robust number-to-words utility or a pre-generated list.
                fun numberToWords(n: Int): String {
                    val units = listOf("", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")
                    val tens = listOf("", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")

                    return when {
                        n < 0 -> "Minus " + numberToWords(-n)
                        n == 0 -> "Zero" // Though the loop is 1..100
                        n < 20 -> units[n]
                        n < 100 -> {
                            val tenPart = tens[n / 10]
                            val unitPart = units[n % 10]
                            if (unitPart.isNotEmpty()) "$tenPart $unitPart" else tenPart
                        }
                        n == 100 -> "One Hundred"
                        else -> n.toString() // Fallback for numbers > 100, though not needed for this loop
                    }
                }

                for (i in 1..100) {
                    Text(
                        text = numberToWords(i),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun HomeSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
        )
        content()
    }
}
@SuppressLint("DesignSystem")
@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text("Search")
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}
@SuppressLint("DesignSystem")
@Composable
private fun SootheBottomNavigation(modifier: Modifier = Modifier) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null
                )
            },
            label = {
                Text("Home")
            },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            label = {
                Text("Profile")
            },
            selected = false,
            onClick = {}
        )
    }
}


@SuppressLint("DesignSystem")
@Composable
fun MySootheAppPortrait() {
    NiaTheme {
        var tabIndex by remember { mutableStateOf(0) }
        val tabs = listOf(
            "HOME", "RETURN ORDER", "RETURN ORDER V2",
            "RETURN ORDER V3", "RETURN ORDER V4", "RETURN ORDER V5",
            "RETURN ORDER V6", "RETURN ORDER V7", "RETURN ORDER V8",
            "RETURN ORDER V9", "RETURN ORDER V10", "RETURN ORDER V11",
            "RETURN ORDER V12"
        )

        Scaffold(
            bottomBar = { SootheBottomNavigation() }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index }
                        )
                    }
                }
                when (tabIndex) {
                    0 -> HomeScreen()
                    1 -> {
                        val orderItem = OrderItem(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptions = listOf(
                            RefundOption(
                                id = RefundMethodType.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOption(
                                id = RefundMethodType.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val state = rememberReturnOrderState(
                            initialOrderItem = orderItem,
                            initialRefundOptions = refundOptions
                        )

                        ReturnOrderScreen(
                            state = state,
                            onNavigateBack = {}
                        )
                    }
                    2 -> {
                        val orderItemV2 = OrderItemV2(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV2 = listOf(
                            RefundOptionV2(
                                id = RefundMethodTypeV2.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV2(
                                id = RefundMethodTypeV2.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV2 = rememberReturnOrderStateV2(
                            initialOrderItem = orderItemV2,
                            initialRefundOptions = refundOptionsV2
                        )

                        ReturnOrderScreenV2(
                            state = stateV2,
                            onNavigateBack = {}
                        )
                    }
                    3 -> {
                        val orderItemV3 = OrderItemV3(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV3 = listOf(
                            RefundOptionV3(
                                id = RefundMethodTypeV3.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV3(
                                id = RefundMethodTypeV3.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV3 = rememberReturnOrderStateV3(
                            initialOrderItem = orderItemV3,
                            initialRefundOptions = refundOptionsV3
                        )

                        ReturnOrderScreenV3(
                            state = stateV3,
                            onNavigateBack = {}
                        )
                    }
                    4 -> {
                        val orderItemV4 = OrderItemV4(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV4 = listOf(
                            RefundOptionV4(
                                id = RefundMethodTypeV4.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV4(
                                id = RefundMethodTypeV4.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV4 = rememberReturnOrderStateV4(
                            initialOrderItem = orderItemV4,
                            initialRefundOptions = refundOptionsV4
                        )

                        ReturnOrderScreenV4(
                            state = stateV4,
                            onNavigateBack = {}
                        )
                    }
                    5 -> {
                        val orderItemV5 = OrderItemV5(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV5 = listOf(
                            RefundOptionV5(
                                id = RefundMethodTypeV5.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV5(
                                id = RefundMethodTypeV5.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV5 = rememberReturnOrderStateV5(
                            initialOrderItem = orderItemV5,
                            initialRefundOptions = refundOptionsV5
                        )

                        ReturnOrderScreenV5(
                            state = stateV5,
                            onNavigateBack = {}
                        )
                    }
                    6 -> {
                        val orderItemV6 = OrderItemV6(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV6 = listOf(
                            RefundOptionV6(
                                id = RefundMethodTypeV6.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV6(
                                id = RefundMethodTypeV6.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV6 = rememberReturnOrderStateV6(
                            initialOrderItem = orderItemV6,
                            initialRefundOptions = refundOptionsV6
                        )

                        ReturnOrderScreenV6(
                            state = stateV6,
                            onNavigateBack = {}
                        )
                    }
                    7 -> {
                        val orderItemV7 = OrderItemV7(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV7 = listOf(
                            RefundOptionV7(
                                id = RefundMethodTypeV7.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV7(
                                id = RefundMethodTypeV7.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV7 = rememberReturnOrderStateV7(
                            initialOrderItem = orderItemV7,
                            initialRefundOptions = refundOptionsV7
                        )

                        ReturnOrderScreenV7(
                            state = stateV7,
                            onNavigateBack = {}
                        )
                    }
                    8 -> {
                        val orderItemV8 = OrderItemV8(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV8 = listOf(
                            RefundOptionV8(
                                id = RefundMethodTypeV8.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV8(
                                id = RefundMethodTypeV8.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV8 = rememberReturnOrderStateV8(
                            initialOrderItem = orderItemV8,
                            initialRefundOptions = refundOptionsV8
                        )

                        ReturnOrderScreenV8(
                            state = stateV8,
                            onNavigateBack = {}
                        )
                    }
                    9 -> {
                        val orderItemV9 = OrderItemV9(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV9 = listOf(
                            RefundOptionV9(
                                id = RefundMethodTypeV9.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV9(
                                id = RefundMethodTypeV9.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV9 = rememberReturnOrderStateV9(
                            initialOrderItem = orderItemV9,
                            initialRefundOptions = refundOptionsV9
                        )

                        ReturnOrderScreenV9(
                            state = stateV9,
                            onNavigateBack = {}
                        )
                    }
                    10 -> {
                        val orderItemV10 = OrderItemV10(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV10 = listOf(
                            RefundOptionV10(
                                id = RefundMethodTypeV10.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV10(
                                id = RefundMethodTypeV10.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV10 = rememberReturnOrderStateV10(
                            initialOrderItem = orderItemV10,
                            initialRefundOptions = refundOptionsV10
                        )

                        ReturnOrderScreenV10(
                            state = stateV10,
                            onNavigateBack = {}
                        )
                    }
                    11 -> {
                        val orderItemV11 = OrderItemV11(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV11 = listOf(
                            RefundOptionV11(
                                id = RefundMethodTypeV11.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV11(
                                id = RefundMethodTypeV11.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV11 = rememberReturnOrderStateV11(
                            initialOrderItem = orderItemV11,
                            initialRefundOptions = refundOptionsV11
                        )

                        ReturnOrderScreenV11(
                            state = stateV11,
                            onNavigateBack = {}
                        )
                    }
                    12 -> {
                        val orderItemV12 = OrderItemV12(
                            id = "123",
                            name = "Floral Print Dress",
                            imageUrl = R.drawable.ic_card_icon,
                            price = 599,
                            size = "M",
                            quantity = 1
                        )

                        val refundOptionsV12 = listOf(
                            RefundOptionV12(
                                id = RefundMethodTypeV12.MEESHO_APP,
                                title = "Meesho App",
                                subtitle = "Instant refund",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                extraAmount = 50,
                                isSelected = true
                            ),
                            RefundOptionV12(
                                id = RefundMethodTypeV12.BANK_UPI,
                                title = "Bank/UPI",
                                subtitle = "3-5 business days",
                                icon = R.drawable.ic_card_icon,
                                getAmount = 599,
                                isSelected = false
                            )
                        )

                        val stateV12 = rememberReturnOrderStateV12(
                            initialOrderItem = orderItemV12,
                            initialRefundOptions = refundOptionsV12
                        )

                        ReturnOrderScreenV12(
                            state = stateV12,
                            onNavigateBack = {}
                        )
                    }
                }
            }
        }
    }
}
