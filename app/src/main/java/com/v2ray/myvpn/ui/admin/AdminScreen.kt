package com.v2ray.myvpn.ui.admin

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.v2ray.myvpn.data.Profile
import com.v2ray.myvpn.security.AdminSession
import com.v2ray.myvpn.ui.theme.*
import com.v2ray.myvpn.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val profiles by viewModel.profiles.collectAsState()

    // وضعیت‌ها
    var showAddDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var selectedProfile by remember { mutableStateOf<Profile?>(null) }

    // QR Scanner
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel", color = WhiteText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(androidx.compose.material.icons.Icons.Default.ArrowBack, contentDescription = "Back", tint = WhiteText)
                    }
                },
                actions = {
                    IconButton(onClick = { showChangePasswordDialog = true }) {
                        Icon(androidx.compose.material.icons.Icons.Default.Lock, contentDescription = "Change Password", tint = WhiteText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryBlue
            ) {
                Icon(androidx.compose.material.icons.Icons.Default.Add, contentDescription = "Add Config", tint = WhiteText)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(profiles.size) { index ->
                val profile = profiles[index]
                AdminProfileCard(
                    profile = profile,
                    onEdit = { selectedProfile = profile },
                    onDelete = { viewModel.deleteProfile(profile.id) }
                )
            }
        }
    }

    // دیالوگ افزودن کانفیگ
    if (showAddDialog) {
        AddConfigDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { newProfile ->
                viewModel.addProfile(newProfile)
                showAddDialog = false
            },
            hasCameraPermission = hasCameraPermission,
            onRequestPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) }
        )
    }

    // دیالوگ ویرایش
    selectedProfile?.let { profile ->
        EditProfileDialog(
            profile = profile,
            onDismiss = { selectedProfile = null },
            onSave = { updatedProfile ->
                viewModel.updateProfile(updatedProfile)
                selectedProfile = null
            }
        )
    }

    // دیالوگ تغییر رمز
    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onSuccess = { showChangePasswordDialog = false }
        )
    }
}

@Composable
fun AdminProfileCard(
    profile: Profile,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = profile.name.ifEmpty { "Unnamed" },
                        color = WhiteText,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${profile.type} • ${profile.address}:${profile.port}",
                        color = CyanAccent,
                        fontSize = 12.sp
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(androidx.compose.material.icons.Icons.Default.Edit, contentDescription = "Edit", tint = CyanAccent)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(androidx.compose.material.icons.Icons.Default.Delete, contentDescription = "Delete", tint = RedError)
                    }
                }
            }
        }
    }
}
