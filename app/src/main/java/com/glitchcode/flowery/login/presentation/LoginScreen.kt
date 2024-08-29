package com.glitchcode.flowery.login.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glitchcode.flowery.R
import com.glitchcode.flowery.core.domain.utils.MaskVisualTransformation
import com.glitchcode.flowery.core.presentation.components.FloweryButton
import com.glitchcode.flowery.core.presentation.components.FloweryFilledTextField
import com.glitchcode.flowery.core.presentation.components.FloweryTextButton
import com.glitchcode.flowery.core.presentation.components.FloweryTextButtonCompact
import com.glitchcode.flowery.core.presentation.components.notification.SwipeableNotification

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val notificationState = viewModel.notificationState
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        TopSection()
        Spacer(modifier = Modifier.weight(1f))
        AuthForm(viewModel)
        Spacer(modifier = Modifier.height(48.dp))
        CreateAccountSection()
    }

    SwipeableNotification(notificationState = notificationState)
}

@Composable
private fun CreateAccountSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.login_screen_no_account_yet_text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(4.dp))
        FloweryTextButtonCompact(
            onClick = { /*TODO*/ }
        ) {
            Text(text = stringResource(id = R.string.login_screen_register_button_text))
        }
    }
}

@Composable
private fun AuthForm(
    viewModel: LoginViewModel
) {
    val loginState = viewModel.loginState.collectAsState()
    val phoneNumber = viewModel.phoneNumber.collectAsState()
    val verificationCode = viewModel.phoneVerificationCode.collectAsState()
    val login = viewModel.userLogin.collectAsState()
    val password = viewModel.userPassword.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = loginState.value.loginType,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = spring(
                        stiffness = 1000f
                    )
                ) { it } togetherWith slideOutHorizontally(
                    animationSpec = spring(
                        stiffness = 1000f
                    )
                ) { -it }
            }
        ) { loginType ->
            when (loginType) {
                LoginType.PHONE -> {
                    AuthByPhoneFields(
                        phoneNumber = phoneNumber.value,
                        verificationCode = verificationCode.value,
                        onPhoneChanges = { viewModel.updatePhoneNumberText(it) },
                        onVerificationCodeChanges = { viewModel.updateVerificationCodeText(it) }
                    )
                }
                LoginType.PASSWORD -> {
                    AuthByPasswordFields(
                        login = login.value,
                        password = password.value,
                        onLoginChanges = { viewModel.updateLoginText(it) },
                        onPasswordChanges = { viewModel.updatePasswordText(it) }
                    )
                }
                LoginType.NEW_ACCOUNT -> TODO()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
        ) {
            FloweryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(text = stringResource(id = R.string.login_screen_login_button_text))
            }
            Spacer(modifier = Modifier.height(16.dp))
            LoginTypesDivider(
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedContent(
                targetState = loginState.value.loginType,
                transitionSpec = {
                    fadeIn(
                        animationSpec = spring(
                            stiffness = 1000f
                        )
                    ) togetherWith fadeOut(
                        animationSpec = spring(
                            stiffness = 1000f
                        )
                    )
                }
            ) { loginType ->
                when (loginType) {
                    LoginType.PHONE -> {
                        FloweryTextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            onClick = { viewModel.updateLoginType(LoginType.PASSWORD) }
                        ) {
                            Text(text = stringResource(id = R.string.login_screen_login_password_button_text))
                        }
                    }
                    LoginType.PASSWORD -> {
                        FloweryTextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            onClick = { viewModel.updateLoginType(LoginType.PHONE) }
                        ) {
                            Text(text = stringResource(id = R.string.login_screen_login_phone_button_text))
                        }
                    }
                    LoginType.NEW_ACCOUNT -> TODO()
                }
            }
        }
    }
}

@Composable
private fun AuthByPhoneFields(
    phoneNumber: String,
    verificationCode: String,
    onPhoneChanges: (String) -> Unit,
    onVerificationCodeChanges: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
    ) {
        FloweryFilledTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = phoneNumber,
            onValueChange = { onPhoneChanges.invoke(it) },
            label = {
                Text(text = stringResource(id = R.string.login_screen_phone_number_label))
            },
            prefix = {
                Text(text = "+7 ")
            },
            visualTransformation = MaskVisualTransformation("(###) ###-##-##")
        )
    }
}

@Composable
private fun AuthByPasswordFields(
    login: String,
    password: String,
    onLoginChanges: (String) -> Unit,
    onPasswordChanges: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
    ) {
        FloweryFilledTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = login,
            onValueChange = { onLoginChanges.invoke(it) },
            label = {
                Text(text = stringResource(id = R.string.login_screen_login_label))
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        FloweryFilledTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = password,
            onValueChange = { onPasswordChanges.invoke(it) },
            label = {
                Text(text = stringResource(id = R.string.login_screen_password_label))
            }
        )
    }

}

@Composable
private fun LoginTypesDivider(
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.reusable_text_or),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TopSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.ic_loginscreen_background),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .offset(y = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
//                        .align(Alignment.Center)
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxHeight(),
                    painter = painterResource(id = R.drawable.ic_flowery),
                    contentDescription = "Flowery icon",
                    contentScale = ContentScale.FillHeight,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(id = R.string.app_name)
                        .uppercase(),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(90.dp))
            Text(
                modifier = Modifier
//                        .align(Alignment.BottomCenter)
                    .padding(bottom = 60.dp),
                text = stringResource(id = R.string.login_screen_welcome_text)
                    .uppercase(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}