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
import androidx.compose.ui.text.font.FontWeight
import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
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
@Composable
fun OutlinedButtonExample(onClick: () -> Unit, icon: @Composable () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Blue
        ),
        border = BorderStroke(2.dp, Color.White),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        icon()
    }
}


// Pantalla para configurar los temporizadores.
@Composable
fun ConfigScreen(modifier: Modifier = Modifier) {
    var mostrar by remember { mutableStateOf(true) }


    // Valores predeterminados de la configuración.
    var sets by remember { mutableIntStateOf(3) }
    var work by remember { mutableIntStateOf(30) }
    var rest by remember { mutableIntStateOf(10) }


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
                            .clickable { rest += 10 },
                        color = Color.White
                    )
                }


                Spacer(modifier = Modifier.padding(10.dp, 20.dp))
                OutlinedButtonExample(onClick = { mostrar = false }) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Iniciar",
                        tint = Color(0xFFADD8E6),
                        modifier = Modifier.size(40.dp)
                    )
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


// Pantalla para mostrar los temporizadores.
@Composable
fun CounterScreen(sets: Int, work: Int, rest: Int, volver: () -> Unit) {
    // Variable para la fase (prep, work, rest o finish)
    var fase by remember { mutableStateOf("PREP") }
    // Tiempo restante
    var restante by remember { mutableIntStateOf(5) }
    // Set en el que se encuentra
    var setActual by remember { mutableIntStateOf(sets) }
    // Verificar si el contador esta funcionando
    var funcionando by remember { mutableStateOf(false) }

    var enPausa by remember { mutableStateOf(false) }

    // Contador en el archivo CounterDown.kt
    var counter: CounterDown? by remember { mutableStateOf(null) }

    val context = LocalContext.current

    // Reproductor de audio
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // Funciones de iniciar y detener un audio
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

    // Función para iniciar un contador
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

    // Función para reiniciar tábata
    fun reiniciar() {
        funcionando = false
        counter?.cancel()
        detenerMusica()
        fase = "PREP"
        restante = 5
        setActual = sets
    }

    fun pausarCounter() {
        enPausa = true
        counter?.pause()
        detenerMusica()
    }

    fun reanudarCounter() {
        enPausa = false
        counter?.resume()
        iniciarMusica(R.raw.audiowork)
    }

    // Función para cambiar entre fases
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

    // Fondo para CounterScreen
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
            // Mientras fase no es finish, se ejecutan los contadores
            if (fase != "Finish") {
                Text(
                    text = if (fase == "PREP") "Prepárate" else fase,
                    fontSize = 70.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(10.dp, 10.dp)
                )
                Text(text = "$restante", fontSize = 100.sp, modifier = Modifier.padding(10.dp, 30.dp), color = Color.White)


                if (fase != "PREP") {
                    Text(text = "Sets: $setActual", fontSize = 50.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.padding(10.dp, 30.dp))
                }
                if (fase == "WORK") {
                    Row {
                        OutlinedButtonExample(onClick = {
                            if (enPausa) reanudarCounter() else pausarCounter()
                        }) {
                            if (enPausa) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Reanudar",
                                    tint = Color(0xFFADD8E6),
                                    modifier = Modifier.size(32.dp)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_pause),
                                    contentDescription = "Pausar",
                                    tint = Color(0xFFADD8E6),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        OutlinedButtonExample(onClick = { reiniciar() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reiniciar",
                                tint = Color(0xFFADD8E6),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp, 10.dp))

                OutlinedButtonExample(onClick = {
                    detenerMusica()
                    counter?.cancel()
                    volver()
                }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = Color(0xFFADD8E6),
                        modifier = Modifier.size(32.dp)
                    )
                }
            // Si fase ya es Finish, se muestra la pantalla final
            } else {
                if (mediaPlayer == null) {
                    iniciarMusica(R.raw.audiofinal)
                }
                Text(text = "¡Tabata completado!", fontSize = 40.sp,
                    modifier = Modifier.padding(10.dp, 40.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Row {
                    OutlinedButtonExample(onClick = { reiniciar() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reiniciar",
                            tint = Color(0xFFADD8E6),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    OutlinedButtonExample(onClick = {
                        detenerMusica()
                        counter?.cancel()
                        volver()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes",
                            tint = Color(0xFFADD8E6),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}