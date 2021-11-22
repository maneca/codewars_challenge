package com.example.codewarschallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.codewarschallenge.R
import com.example.codewarschallenge.db.model.CompletedChallenge
import com.example.codewarschallenge.ui.widgets.TextBox
import com.example.codewarschallenge.viewmodels.CompletedChallengesViewModel
import com.example.codewarschallenge.viewmodels.PAGE_SIZE
import org.koin.android.viewmodel.ext.android.viewModel

class CompletedChallengesFragment : Fragment() {
    private val completedChallengesViewModel by viewModel<CompletedChallengesViewModel>()
    private var page: Int = 0
    private var loading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val completedChallenges = completedChallengesViewModel.completedChallenges.value
                loading = completedChallengesViewModel.showLoading.value
                val error = completedChallengesViewModel.showError.value
                page = completedChallengesViewModel.page.value

                when {
                    !error.isNullOrBlank() -> ErrorDialog(message = error)
                    else ->
                        Scaffold(topBar = {
                            TopAppBar(
                                title = { Text(text = stringResource(R.string.app_name)) }
                            )
                        }) {
                            if (loading) {
                                Dialog(
                                    onDismissRequest = { },
                                    DialogProperties(
                                        dismissOnBackPress = false,
                                        dismissOnClickOutside = false
                                    )
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .background(White, shape = RoundedCornerShape(8.dp))
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                            CompleteChallengesList(completedChallenges)
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
                completedChallengesViewModel.onChangeChallengesScrollPosition(index)

                if (index >= ((page * PAGE_SIZE) - 1) && !loading) {
                    completedChallengesViewModel.nextPage()
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
                    contentDescription = "Contact profile picture",
                    modifier = Modifier
                        // Set image size to 40 dp
                        .size(60.dp)
                        // Clip image to be shaped as a circle
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
    fun ErrorDialog(message: String) {
        val openDialog = remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = getString(R.string.error_title))
            },
            text = {
                Text(message)
            },
            confirmButton = {
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text(getString(R.string.dismiss_button))
                }
            }
        )
    }
}