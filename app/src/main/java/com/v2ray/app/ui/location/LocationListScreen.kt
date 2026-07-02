package com.v2ray.app.ui.location

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.app.data.Profile
import com.v2ray.app.ui.theme.*
import com.v2ray.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationListScreen(vm: MainViewModel, onBack: () -> Unit) {
    val profiles by vm.profiles.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = { Text("Location List", color = WhiteText) }, navigationIcon = {
            IconButton(onBack) { Icon(Icons.Default.ArrowBack, tint = WhiteText, contentDescription = "Back") }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground))
    }) { pad ->
        LazyColumn(Modifier.fillMaxSize().background(DarkBackground).padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(profiles.size) {
                val p = profiles[it]
                LocationCard(p, p.selected) { vm.select(p.id) }
            }
        }
    }
}

@Composable fun LocationCard(profile: Profile, selected: Boolean, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = if (selected) PrimaryBlue.copy(0.2f) else DarkSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(profile.name.ifEmpty { "Unnamed" }, color = WhiteText, fontWeight = FontWeight.Bold)
                Text("${profile.type} • ${profile.address}:${profile.port}", color = CyanAccent, fontSize = 12.sp)
                Text("${profile.ping} ms", color = GreenAccent, fontSize = 12.sp)
            }
            Icon(if (selected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                tint = if (selected) GreenAccent else Color.Gray, contentDescription = null)
        }
    }
}
