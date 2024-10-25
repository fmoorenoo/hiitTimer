package org.iesharia.hiittimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale


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

// Pantalla para configurar los temporizadores.
@Composable
fun ConfigScreen(modifier: Modifier = Modifier) {
    var mostrar by remember { mutableStateOf(true) }

    // Valores predeterminados de la configuración.
    var sets by remember { mutableStateOf(3) }
    var work by remember { mutableStateOf(30) }
    var rest by remember { mutableStateOf(10) }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Imagen de fondo ocupando la pantalla completa.
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Condición para mostrar u ocultar la pantalla de configuración.
            if (mostrar) {
                // Configurar SETS
                Text(text = "SETS",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "<",
                        fontSize = 40.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { if (sets > 1) sets-- },
                        color = Color.White
                    )
                    Text(text = sets.toString(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(60.dp, 0.dp),
                        color = Color.White
                    )
                    Text(
                        text = ">",
                        fontSize = 40.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { sets++ },
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp, 20.dp))

                // Configurar WORK
                Text(text = "WORK",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "-",
                        fontSize = 65.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { if (work > 10) work -= 10 },
                        color = Color.White
                    )
                    Text(text = work.toString(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(60.dp, 0.dp),
                        color = Color.White
                    )
                    Text(
                        text = "+",
                        fontSize = 40.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { work += 10 },
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp, 20.dp))

                // Configurar REST
                Text(text = "REST",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "-",
                        fontSize = 65.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { if (rest > 10) rest -= 10 },
                        color = Color.White
                    )
                    Text(text = rest.toString(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(60.dp, 0.dp),
                        color = Color.White
                    )
                    Text(
                        text = "+",
                        fontSize = 40.sp,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { rest += 10 },
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp, 20.dp))

                Button(onClick = { mostrar = false }) {
                    Text(text = "Start", fontSize = 50.sp)
                }
            // Si mostrar es falso, se llama a la pantalla de contadores con los ajustes establecidos.
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
}


@Composable
fun CounterScreen(sets: Int, work: Int, rest: Int, volver: () -> Unit) {
    var fase by remember { mutableStateOf("PREP") }
    var restante by remember { mutableStateOf(5) }
    var setActual by remember { mutableStateOf(sets) }
    var funcionando by remember { mutableStateOf(false) }

    var counter: CounterDown? by remember { mutableStateOf<CounterDown?>(null) }

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    fun detenerMusica() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun iniciarMusica(audioId: Int) {
        detenerMusica()
        mediaPlayer = MediaPlayer.create(context, audioId)
        mediaPlayer?.start()
    }

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
        counter?.cancel()
        detenerMusica()
        fase = "PREP"
        restante = 5
        setActual = sets
    }

    fun siguienteFase() {
        if (fase == "WORK") {
            if (setActual > 1) {
                fase = "REST"
                detenerMusica()
                iniciarMusica(R.raw.audiofinalwork)
                iniciar(rest) {
                    setActual--
                    fase = "WORK"
                    iniciarMusica(R.raw.audiowork)
                    iniciar(work) { siguienteFase() }
                }
            } else {
                detenerMusica()
                fase = "Finish"
            }
        } else if (fase == "PREP") {
            fase = "WORK"
            iniciarMusica(R.raw.audiowork)
            iniciar(work) { siguienteFase() }
        }
    }

    LaunchedEffect(funcionando) {
        if (!funcionando) {
            funcionando = true
            iniciar(restante) { siguienteFase() }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
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
                Text(text = "$restante", fontSize = 100.sp, modifier = Modifier.padding(10.dp, 30.dp), color = Color.White,)

                if (fase != "PREP") {
                    Text(text = "Sets: $setActual", fontSize = 50.sp, fontWeight = FontWeight.Bold, color = Color.White,)
                    Spacer(modifier = Modifier.padding(10.dp, 30.dp))
                    Button(onClick = { reiniciar() }) {
                        Text(text = "Reiniciar", fontSize = 30.sp)
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp, 10.dp))

                Button(onClick = {
                    detenerMusica()
                    counter?.cancel()
                    volver()
                }) {
                    Text(text = "Ajustes", fontSize = 30.sp)
                }
            } else {
                if (mediaPlayer == null) {
                    iniciarMusica(R.raw.audiofinal)
                }
                Text(text = "¡Tabata completado!", fontSize = 40.sp, modifier = Modifier.padding(10.dp, 40.dp))

                Button(onClick = { reiniciar() }) {
                    Text(text = "Reiniciar Tábata", fontSize = 25.sp)
                }

                Button(onClick = {
                    counter?.cancel()
                    volver()
                }) {
                    Text(text = "Ajustes", fontSize = 25.sp)
                }
            }
        }
    }
}