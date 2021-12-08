package com.example.codewarschallenge.ui.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.codewarschallenge.R
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.ui.widgets.ErrorDialog
import com.example.codewarschallenge.ui.widgets.LoadingView
import com.example.codewarschallenge.ui.widgets.TextBox
import com.example.codewarschallenge.viewmodels.CompletedChallengesViewModel
import com.example.codewarschallenge.viewmodels.PAGE_SIZE
import org.koin.android.viewmodel.ext.android.viewModel

class CompletedChallengesFragment : Fragment() {
    private val completedChallengesViewModel by viewModel<CompletedChallengesViewModel>()
    private var page: Int = 0
    private var loading: Boolean = false
    private var canLoad: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val completedChallenges = completedChallengesViewModel.completedChallenges
                loading = completedChallengesViewModel.showLoading
                val error = completedChallengesViewModel.showError
                page = completedChallengesViewModel.page
                val warning = completedChallengesViewModel.showWarning

                SystemBroadcastReceiver(WifiManager.WIFI_STATE_CHANGED_ACTION) { wifi_state ->
                    when (wifi_state?.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN
                    )) {
                        WifiManager.WIFI_STATE_ENABLED -> {
                            canLoad = true
                        }
                        WifiManager.WIFI_STATE_DISABLED -> {
                            canLoad = false
                        }
                    }
                }

                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.app_name)) }
                    )
                }) {
                    if (loading) {
                        LoadingView()
                    }

                    if (error != null) {
                        ErrorDialog(
                            message = stringResource(error),
                            title = getString(R.string.error_title),
                            dismissText = getString(R.string.dismiss_button)
                        )
                    }
                    if (completedChallenges.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.nothing_to_show),
                            )
                        }
                    } else {
                        CompleteChallengesList(completedChallenges)
                    }
                    if (warning != null) {
                        Toast.makeText(
                            context,
                            warning,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }
    }

    @Composable
    fun CompleteChallengesList(challenges: List<CompletedChallenge>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            itemsIndexed(items = challenges) { index, challenge ->

                if (index > 0 && page > 0 && index >= ((page * PAGE_SIZE) - 1) && !loading) {
                    if (canLoad) {
                        completedChallengesViewModel.nextPage()
                    }
                }
                Challenge(challenge = challenge)
            }
        }
    }

    @Composable
    fun Challenge(challenge: CompletedChallenge) {
        Card(
            modifier = Modifier
                .background(shape = RoundedCornerShape(10.dp), color = Color.LightGray)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(4.dp)
                .clickable {
                    val bundle = Bundle()
                    bundle.putString("id", challenge.id)
                    findNavController().navigate(R.id.challengeDetailsFragment, bundle)
                },
            elevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.champion),
                    contentDescription = "",
                    modifier = Modifier
                        // Set image size to 40 dp
                        .size(60.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    Text(text = challenge.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.size(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar),
                            modifier = Modifier.size(20.dp),
                            contentDescription = null // decorative element
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(challenge.completedAt.split('T')[0])
                    }
                    Spacer(modifier = Modifier.size(6.dp))
                    TextBox(challenge.completedLanguages, Color.Blue)
                }
            }
        }
    }

    @Composable
    fun SystemBroadcastReceiver(
        systemAction: String,
        onSystemEvent: (intent: Intent?) -> Unit
    ) {
        // Grab the current context in this part of the UI tree
        val context = LocalContext.current

        // Safely use the latest onSystemEvent lambda passed to the function
        val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

        // If either context or systemAction changes, unregister and register again
        DisposableEffect(context, systemAction) {
            val intentFilter = IntentFilter(systemAction)
            val broadcast = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    onSystemEvent(intent)
                }
            }

            context.registerReceiver(broadcast, intentFilter)

            // When the effect leaves the Composition, remove the callback
            onDispose {
                context.unregisterReceiver(broadcast)
            }
        }
    }
}