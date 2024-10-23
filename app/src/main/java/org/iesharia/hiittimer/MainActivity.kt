package org.iesharia.hiittimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.iesharia.hiittimer.ui.theme.HiitTimerTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.ui.text.font.FontWeight
import android.media.MediaPlayer
import android.provider.MediaStore.Audio.Media
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.unpackInt1


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HiitTimerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConfigScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ConfigScreen(modifier: Modifier = Modifier) {
    var mostrar by remember { mutableStateOf(true) }
    var sets by remember { mutableStateOf(3) }
    var work by remember { mutableStateOf(60) }
    var rest by remember { mutableStateOf(10) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFe1dd60)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        if (mostrar) {
            Text(text = "SETS", fontSize = 30.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { if (sets > 1) sets-- }) {
                    Text(text = "-", fontSize = 25.sp)
                }
                Text(text = sets.toString(), fontSize = 30.sp, modifier = Modifier.padding(60.dp, 40.dp))
                Button(onClick = { sets++ }) {
                    Text(text = "+", fontSize = 25.sp)
                }
            }

            Text(text = "WORK", fontSize = 30.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { if (work >= 15) work -= 11 }) {
                    Text(text = "-", fontSize = 25.sp)
                }
                Text(text = work.toString(), fontSize = 30.sp, modifier = Modifier.padding(60.dp, 40.dp))
                Button(onClick = { work += 15 }) {
                    Text(text = "+", fontSize = 25.sp)
                }
            }

            Text(text = "REST", fontSize = 30.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { if (rest >= 10) rest -= 6 }) {
                    Text(text = "-", fontSize = 25.sp)
                }
                Text(text = rest.toString(), fontSize = 30.sp, modifier = Modifier.padding(60.dp, 40.dp))
                Button(onClick = { rest += 10 }) {
                    Text(text = "+", fontSize = 25.sp)
                }
            }

            Button(onClick = { mostrar = false }) {
                Text(text = "Start", fontSize = 50.sp)
            }
        } else {
            CounterScreen(
                sets = sets,
                work = work + 1,
                rest = rest + 1,
                volver = { mostrar = true }
            )
        }
    }
}

@Composable
fun CounterScreen(sets: Int, work: Int, rest: Int, volver: () -> Unit) {
    var fase by remember { mutableStateOf("PREP") }
    var restante by remember { mutableStateOf(5) }
    var setActual by remember { mutableStateOf(sets) }
    var funcionando by remember { mutableStateOf(false) }

    var counter: CounterDown? by remember { mutableStateOf<CounterDown?>(null) }

    fun iniciar(seconds: Int, onFinish: () -> Unit) {
        counter?.cancel()
        counter = CounterDown(seconds) { tiempoRestante ->
            restante = tiempoRestante.toInt()
            if (tiempoRestante <= 0) {
                onFinish()
            }
        }
        counter?.start()
    }

    fun reiniciar() {
        funcionando = false
        fase = "PREP"
        restante = 5
        setActual = sets
    }

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }


    fun detenerMusica() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun iniciarMusica() {
        detenerMusica()
        mediaPlayer = MediaPlayer.create(context, R.raw.audiofinalissimo)
        mediaPlayer?.start()
    }

    fun siguienteFase() {
        if (fase == "WORK") {
            fase = "REST"
            detenerMusica()
            iniciar(rest) {
                if (setActual > 1) {
                    setActual--
                    fase = "WORK"
                    iniciarMusica()
                    iniciar(work) { siguienteFase() }
                } else {
                    fase = "Finish"
                }
            }
        } else if (fase == "PREP") {
            fase = "WORK"
            iniciarMusica()
            iniciar(work) { siguienteFase() }
        }
    }

    LaunchedEffect(funcionando) {
        if (!funcionando) {
            funcionando = true
            iniciar(restante) { siguienteFase() }
        }
    }

    val backgroundColor = when (fase) {
        "WORK" -> Color(0xFF00E676)
        "REST" -> Color(0xFF2196F3)
        "PREP" -> Color(0xFFFFC107)
        else -> Color(0xFFFF8080)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (fase != "Finish") {
            Text(
                text = if (fase == "PREP") "Prepárate" else fase,
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(10.dp, 10.dp)
            )
            Text(text = "$restante", fontSize = 100.sp, modifier = Modifier.padding(10.dp, 30.dp))

            if (fase != "PREP") {
                Text(text = "Sets: $setActual", fontSize = 50.sp, fontWeight = FontWeight.Bold)
                Button(onClick = { reiniciar() }) {
                    Text(text = "Reiniciar", fontSize = 30.sp)
                }
            }

            Spacer(modifier = Modifier.padding(10.dp, 10.dp))

            Button(onClick = {
                detenerMusica()
                volver()
            }) {
                Text(text = "Ajustes", fontSize = 30.sp)
            }

        } else {
            Text(text = "¡Tabata completado!", fontSize = 40.sp, modifier = Modifier.padding(10.dp, 40.dp))

            Button(onClick = { reiniciar() }) {
                Text(text = "Reiniciar Tábata", fontSize = 25.sp)
            }

            Button(onClick = {
                detenerMusica()
                volver()
            }) {
                Text(text = "Ajustes", fontSize = 25.sp)
            }
        }
    }
}
