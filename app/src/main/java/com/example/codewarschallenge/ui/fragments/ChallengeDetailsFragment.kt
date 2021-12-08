package com.example.codewarschallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.codewarschallenge.R
import com.example.codewarschallenge.db.model.ChallengeDetails
import com.example.codewarschallenge.ui.widgets.ErrorDialog
import com.example.codewarschallenge.ui.widgets.LoadingView
import com.example.codewarschallenge.ui.widgets.TextBox
import com.example.codewarschallenge.viewmodels.ChallengeDetailsViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class ChallengeDetailsFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(data: String) = ChallengeDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("id", data)
            }
        }
    }

    private val challengeDetailsViewModel by viewModel<ChallengeDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (arguments?.getString("id") != null) {
            challengeDetailsViewModel.getChallengeDetails(arguments?.getString("id")!!)
        }

        return ComposeView(requireContext()).apply {
            setContent {
                val challengeDetails = challengeDetailsViewModel.challengeDetails
                val loading = challengeDetailsViewModel.showLoading
                val error = challengeDetailsViewModel.showError

                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.challenge_details)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                findNavController().popBackStack()
                            }) {
                                Icon(Icons.Filled.ArrowBack, "")
                            }
                        }
                    )
                }) {
                    if (loading) {
                        LoadingView()
                    }
                    if (error != null) {
                        ErrorDialog(
                            message = getString(error),
                            title = getString(R.string.error_title),
                            dismissText = getString(R.string.dismiss_button)
                        )
                    }

                    if (challengeDetails.id == "") {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.nothing_to_show),
                            )
                        }
                    } else {
                        ChallengeDetails(challengeDetails)
                    }

                }

            }
        }

    }

    @Composable
    fun ChallengeDetails(challengeDetails: ChallengeDetails) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .verticalScroll(scrollState, true)
        ) {
            Text(
                text = challengeDetails.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.h5
            )
            TextDetailWidget(
                detailName = stringResource(R.string.description) + "\n",
                detailValue = challengeDetails.description
            )
            TextDetailWidget(
                detailName = stringResource(R.string.category),
                detailValue = challengeDetails.category.replaceFirstChar { it.uppercase() }
            )
            TextDetailWidget(
                detailName = stringResource(R.string.languages) + "\n",
                detailValue = ""
            )
            TextBox(challengeDetails.languages, Color.Blue, 10.dp)
            Spacer(modifier = Modifier.size(4.dp))
            TextDetailWidget(
                detailName = stringResource(R.string.tags) + "\n",
                detailValue = ""
            )
            TextBox(challengeDetails.tags, Color(0xFF9CCC65), 10.dp)

            Spacer(modifier = Modifier.size(6.dp))
            challengeDetails.createdBy?.let {
                TextHyperlink(
                    detailName = stringResource(R.string.created_by) + "\n",
                    detailValue = it.username,
                    hyperlink = it.url
                )
            }

            Spacer(modifier = Modifier.size(4.dp))
            challengeDetails.approvedBy?.let {
                TextHyperlink(
                    detailName = stringResource(R.string.approved_by) + "\n",
                    detailValue = it.username,
                    hyperlink = it.url
                )
            }
        }
    }

    @Composable
    fun TextHyperlink(detailName: String, detailValue: String, hyperlink: String){
        val annotatedLinkString: AnnotatedString = buildAnnotatedString {
            append(detailValue)
            addStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontSize = 15.sp,
                    textDecoration = TextDecoration.Underline
                ), start = 0, end = detailValue.length
            )

            addStringAnnotation(
                tag = "URL",
                annotation = hyperlink,
                start = 0,
                end = detailValue.length
            )

        }
        val uriHandler = LocalUriHandler.current

        Row(Modifier.fillMaxWidth()) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                        append("$detailName ")
                    }
                },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = TextStyle(fontSize = 15.sp)
            )
            ClickableText(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = TextStyle(fontSize = 15.sp),
                text = annotatedLinkString,
                onClick = {
                    annotatedLinkString
                        .getStringAnnotations("URL", it, it)
                        .firstOrNull()?.let { stringAnnotation ->
                            uriHandler.openUri(stringAnnotation.item)
                        }
                }
            )
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
}