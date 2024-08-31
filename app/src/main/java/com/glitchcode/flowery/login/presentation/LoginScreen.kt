package com.glitchcode.flowery.login.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToMainScreen: () -> Unit
) {
    val notificationState = viewModel.notificationState
    val loginState = viewModel.loginState.collectAsState()

    TopSectionImage(
        modifier = Modifier
            .fillMaxSize()
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .padding(bottom = 8.dp)
    ) {
        TopSection()
        Spacer(modifier = Modifier.weight(1f))
        AuthForm(
            viewModel,
            onNavigateToMainScreen
        )
        AnimatedVisibility(visible = loginState.value.loginType != LoginType.NEW_ACCOUNT) {
            Column {
                Spacer(modifier = Modifier.height(48.dp))
                CreateAccountSection(
                    onCreateNewAccount = { viewModel.updateLoginType(LoginType.NEW_ACCOUNT) }
                )
            }
        }
    }

    SwipeableNotification(notificationState = notificationState)
}

@Composable
private fun CreateAccountSection(
    onCreateNewAccount: () -> Unit
) {
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
            onClick = { onCreateNewAccount.invoke() }
        ) {
            Text(text = stringResource(id = R.string.login_screen_register_button_text))
        }
    }
}

@Composable
private fun AuthForm(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val loginState = viewModel.loginState.collectAsState()
    val phoneNumber = viewModel.phoneNumber.collectAsState()
    val verificationCode = viewModel.phoneVerificationCode.collectAsState()
    val login = viewModel.userLogin.collectAsState()
    val password = viewModel.userPassword.collectAsState()
    val newUserFirstName = viewModel.newUserFirstName.collectAsState()
    val newUserLastName = viewModel.newUserLastname.collectAsState()
    val newUserVerificationCode = viewModel.newUserPhoneVerificationCode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = loginState.value.loginType,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) { it } togetherWith slideOutHorizontally(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) { -it }
            }
        ) { loginType ->
            when (loginType) {
                LoginType.PHONE -> {
                    AuthByPhoneFields(
                        viewModel = viewModel,
                        phoneNumber = phoneNumber.value,
                        verificationCode = verificationCode.value,
                        isRequestingCode = loginState.value.isLoadingCode,
                        isWaitingCodeInput = loginState.value.isWaitingCodeInput,
                        onPhoneChanges = { viewModel.updatePhoneNumberText(it) },
                        onVerificationCodeChanges = { viewModel.updateVerificationCodeText(it) },
                        onRequestCodeClicked = { viewModel.requestVerificationCode() }
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
                LoginType.NEW_ACCOUNT -> {
                    AuthNewAccountFields(
                        name = newUserFirstName.value,
                        lastName = newUserLastName.value,
                        phoneNumber = phoneNumber.value,
                        isRequestingCode = loginState.value.isNewUserLoadingCode,
                        isWaitingCodeInput = loginState.value.isNewUserWaitingCodeInput,
                        verificationCode = newUserVerificationCode.value,
                        onNameChanges = { viewModel.updateNewUserFirstName(it) },
                        onLastNameChanges = { viewModel.updateNewUserLastName(it) },
                        onPhoneChanges = { viewModel.updatePhoneNumberText(it) },
                        onVerificationCodeChanges = { viewModel.updateNewUserVerificationCode(it) },
                        onRequestCodeClicked = {}
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
        ) {
            AnimatedContent(
                targetState = loginState.value.loginType,
                transitionSpec = {
                    fadeIn(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) togetherWith fadeOut(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                }
            ) { loginType ->
                when (loginType) {
                    LoginType.PHONE -> {
                        FloweryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            onClick = { viewModel.loginByPhone(onLoginSuccess) },
                            enabled = phoneNumber.value.isNotEmpty() && verificationCode.value.isNotEmpty()
                        ) {
                            Text(text = stringResource(id = R.string.login_screen_login_button_text))
                        }
                    }
                    LoginType.PASSWORD -> {
                        FloweryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            onClick = { viewModel.loginByPassword(onLoginSuccess) },
                            enabled = login.value.isNotEmpty() && password.value.isNotEmpty()
                        ) {
                            Text(text = stringResource(id = R.string.login_screen_login_button_text))
                        }
                    }
                    LoginType.NEW_ACCOUNT -> {
                        FloweryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            onClick = { /*TODO*/ },
                            enabled = true // TODO
                        ) {
                            Text(text = stringResource(id = R.string.login_screen_create_account_button_text))
                        }
                    }
                }
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
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) togetherWith fadeOut(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow
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
                    LoginType.NEW_ACCOUNT -> {
                        FloweryTextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            onClick = { viewModel.updateLoginType(LoginType.PHONE) }
                        ) {
                            Text(text = stringResource(id = R.string.reusable_text_cancel))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthByPhoneFields(
    viewModel: LoginViewModel,
    phoneNumber: String,
    isRequestingCode: Boolean,
    isWaitingCodeInput: Boolean,
    verificationCode: String,
    onPhoneChanges: (String) -> Unit,
    onVerificationCodeChanges: (String) -> Unit,
    onRequestCodeClicked: () -> Unit
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
            singleLine = true,
            prefix = {
                Text(text = "+7 ")
            },
            trailingIcon = {
                Row {
                    FloweryTextButton(
                        onClick = { onRequestCodeClicked.invoke() }
                    ) {
                        Text(text = stringResource(id = R.string.login_screen_send_code_button_text))
                        AnimatedVisibility(
                            visible = isRequestingCode,
                            enter = expandHorizontally(
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            ),
                            exit = shrinkHorizontally(
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            )
                        ) {
                            Row {
                                Spacer(modifier = Modifier.width(12.dp))
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 3.dp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            },
            visualTransformation = MaskVisualTransformation("(###) ###-##-##"),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = { onRequestCodeClicked.invoke() }
            )
        )
        AnimatedVisibility(visible = isWaitingCodeInput) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                FloweryFilledTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = verificationCode,
                    onValueChange = { onVerificationCodeChanges.invoke(it) },
                    label = {
                        Text(text = stringResource(id = R.string.login_screen_verification_code_label))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { /*TODO*/ }
                    )
                )
            }
        }
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
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /*TODO*/ }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        FloweryFilledTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = password,
            onValueChange = { onPasswordChanges.invoke(it) },
            label = {
                Text(text = stringResource(id = R.string.login_screen_password_label))
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { /*TODO*/ }
            )
        )
    }
}

@Composable
private fun AuthNewAccountFields(
    name: String,
    lastName: String,
    phoneNumber: String,
    isRequestingCode: Boolean,
    isWaitingCodeInput: Boolean,
    verificationCode: String,
    onNameChanges: (String) -> Unit,
    onLastNameChanges: (String) -> Unit,
    onPhoneChanges: (String) -> Unit,
    onVerificationCodeChanges: (String) -> Unit,
    onRequestCodeClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
    ) {
        FloweryFilledTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = name,
            onValueChange = { onNameChanges.invoke(it) },
            label = {
                Text(text = stringResource(id = R.string.login_screen_firstname_label))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /*TODO*/ }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        FloweryFilledTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = lastName,
            onValueChange = { onLastNameChanges.invoke(it) },
            label = {
                Text(text = stringResource(id = R.string.login_screen_lastname_label))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /*TODO*/ }
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        FloweryFilledTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = phoneNumber,
            onValueChange = { onPhoneChanges.invoke(it) },
            label = {
                Text(text = stringResource(id = R.string.login_screen_phone_number_label))
            },
            singleLine = true,
            prefix = {
                Text(text = "+7 ")
            },
            visualTransformation = MaskVisualTransformation("(###) ###-##-##"),
            trailingIcon = {
                Row {
                    FloweryTextButton(
                        onClick = { onRequestCodeClicked.invoke() }
                    ) {
                        Text(text = stringResource(id = R.string.login_screen_send_code_button_text))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = { onRequestCodeClicked.invoke() }
            )
        )
        AnimatedVisibility(visible = isWaitingCodeInput) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                FloweryFilledTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = verificationCode,
                    onValueChange = { onVerificationCodeChanges.invoke(it) },
                    label = {
                        Text(text = stringResource(id = R.string.login_screen_verification_code_label))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { /*TODO*/ }
                    )
                )
            }
        }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TopSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val isImeVisible = WindowInsets.isImeVisible
        val topSpacerHeight = animateDpAsState(
            targetValue = if (isImeVisible) 16.dp else 92.dp,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
        )
        val contentColor = animateColorAsState(
            targetValue = if (isImeVisible) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onPrimary,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(topSpacerHeight.value))
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
                    colorFilter = ColorFilter.tint(contentColor.value)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(id = R.string.app_name)
                        .uppercase(),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    color = contentColor.value
                )
            }
            AnimatedVisibility(
                visible = !isImeVisible,
                enter = expandVertically(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeIn(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeOut(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(90.dp))
                    Text(
                        modifier = Modifier
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
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TopSectionImage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        val isImeVisible = WindowInsets.isImeVisible
        AnimatedVisibility(
            visible = !isImeVisible,
            enter = fadeIn(
                animationSpec = spring(
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
            exit = fadeOut(
                animationSpec = spring(
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.ic_loginscreen_background),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
    }
}