package com.example.codewarschallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.Fragment
import com.example.codewarschallenge.R
import com.example.codewarschallenge.db.model.ChallengeDetails
import com.example.codewarschallenge.ui.widgets.TextBox
import com.example.codewarschallenge.viewmodels.ChallengeDetailsViewModel
import com.example.codewarschallenge.viewmodels.viewModelsFactory


class ChallengeDetailsFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(data: String) = ChallengeDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("id", data)
            }
        }
    }

    private val challengeDetailsViewModel by viewModelsFactory { ChallengeDetailsViewModel(arguments?.getString("id")!!) }
    private val PrimaryBlue = Color(0xFF9CCC65)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return ComposeView(requireContext()).apply {
            setContent {
                val challengeDetails = challengeDetailsViewModel.challengeDetails.value
                val loading = challengeDetailsViewModel.showLoading.value
                val error = challengeDetailsViewModel.showError.value

                when {
                    !error.isNullOrBlank() -> ErrorDialog(message = error)
                    else ->
                        Scaffold(topBar = {
                            TopAppBar(
                                title = { Text(text = stringResource(R.string.challenge_details)) },
                                navigationIcon = {
                                    IconButton(onClick = { }) {
                                        Icon(Icons.Filled.ArrowBack,"")
                                    }
                                }
                            )
                        }){
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
                                            .background(
                                                Color.White,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                            ChallengeDetails(challengeDetails)
                        }
                }
            }
        }

    }

    @Composable
    fun ChallengeDetails(challengeDetails: ChallengeDetails){
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .verticalScroll(scrollState, true)
        ) {
            challengeDetails.name.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.h5
                )
            }
            TextDetailWidget(
                detailName = stringResource(R.string.description),
                detailValue = challengeDetails.description
            )
            TextDetailWidget(
                detailName = stringResource(R.string.category),
                detailValue = challengeDetails.category
            )
            TextDetailWidget(
                detailName = stringResource(R.string.languages)+"\n",
                detailValue = ""
            )
            TextBox(challengeDetails.languages, Color.Blue, 10.dp)
            TextDetailWidget(
                detailName = stringResource(R.string.tags)+"\n",
                detailValue = ""
            )
            TextBox(challengeDetails.tags, PrimaryBlue, 10.dp)
       }
    }

    @Composable
    fun TextDetailWidget(detailName: String, detailValue: String) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                    append("$detailName ")
                }
                append(detailValue)
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = TextStyle(fontSize = 15.sp)
        )
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