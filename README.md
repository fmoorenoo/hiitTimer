<div align="center">

# HIIT Timer

### Temporizador de entrenamiento por intervalos para Android

**Proyecto Android · Entrenamientos HIIT · Kotlin + Jetpack Compose**

![Kotlin](https://img.shields.io/badge/Kotlin-1A1B26?style=for-the-badge&logo=kotlin&logoColor=E46B79)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-1A1B26?style=for-the-badge&logo=jetpackcompose&logoColor=E46B79)
![Android](https://img.shields.io/badge/Android-1A1B26?style=for-the-badge&logo=android&logoColor=E46B79)
![Material 3](https://img.shields.io/badge/Material_3-1A1B26?style=for-the-badge&logo=materialdesign&logoColor=E46B79)

[![Estado](https://img.shields.io/badge/ESTADO-Proyecto_académico-712B39?style=flat-square)](#estado-del-proyecto)
[![Plataforma](https://img.shields.io/badge/PLATAFORMA-Android-22252C?style=flat-square)](#tecnologías)
[![Código](https://img.shields.io/badge/REPOSITORIO-Código_fuente-22252C?style=flat-square)](https://github.com/fmoorenoo/hiitTimer)

</div>

---

## Sobre el proyecto

**HIIT Timer** es una aplicación Android para organizar entrenamientos de intervalos de alta intensidad (**HIIT**). Permite configurar el número de series y la duración de los periodos de actividad y descanso, y muestra una cuenta atrás con señales de audio para acompañar cada fase.

Se desarrolló como **proyecto académico colaborativo**, utilizando **Kotlin y Jetpack Compose** para construir una interfaz móvil declarativa y gestionar el estado de los temporizadores.

## Funcionalidades

- **Configuración de series:** elección del número de repeticiones del entrenamiento.
- **Intervalos personalizables:** ajuste del tiempo de actividad (*work*) y de recuperación (*rest*).
- **Cuenta atrás por fases:** preparación, ejercicio, descanso y finalización.
- **Controles de entrenamiento:** iniciar, pausar, reanudar y reiniciar el temporizador.
- **Audio integrado:** señales de sonido mediante recursos locales y un control de silencio para parte del audio.
- **Interfaz adaptada al entrenamiento:** visualización de la fase actual, tiempo restante y series.

> Estas funcionalidades se han identificado en el código. No se ha comprobado su comportamiento en un dispositivo durante esta revisión.

## Tecnologías

| Área | Tecnologías |
| --- | --- |
| **Lenguaje** | Kotlin |
| **Plataforma** | Android (minSdk 28; targetSdk 34) |
| **Interfaz** | Jetpack Compose · Material 3 |
| **Temporización** | Android `CountDownTimer` |
| **Audio** | Android `MediaPlayer` |
| **Estado de interfaz** | Compose `remember` y `mutableStateOf` |
| **Build** | Gradle Kotlin DSL |

## Arquitectura

```text
hiitTimer/
├── app/
│   └── src/main/
│       ├── java/org/iesharia/hiittimer/
│       │   ├── MainActivity.kt    # Configuración y pantalla del entrenamiento
│       │   ├── CounterDown.kt    # Temporizador con pausa y reanudación
│       │   └── ui/theme/         # Tema visual de la aplicación
│       └── res/
│           ├── drawable*/       # Recursos gráficos
│           └── raw/             # Efectos y pistas de audio
├── build.gradle.kts
└── settings.gradle.kts
```

La interfaz utiliza componentes **Compose** y la clase `CounterDown` encapsula la cuenta atrás basada en `CountDownTimer`. La reproducción se gestiona desde `MainActivity.kt`.

## Instalación y ejecución

1. Clona el proyecto:
   ```bash
   git clone https://github.com/fmoorenoo/hiitTimer.git
   cd hiitTimer
   ```
2. Abre la carpeta del proyecto con **Android Studio**.
3. Configura el SDK de Android y sincroniza Gradle.
4. Ejecuta el módulo `app` en un emulador o dispositivo Android compatible (API 28 o superior).

La compilación no se ha verificado en esta revisión.

## Capturas

No se incluyen capturas verificadas de la aplicación en este repositorio. Se podrán incorporar imágenes reales de las pantallas de configuración y entrenamiento en una próxima actualización.

## Estado del proyecto

**Proyecto académico.** El código incluye configuración de intervalos, gestión del temporizador y reproducción de sonidos.

El README anterior también recogía **persistencia de ajustes** y otras indicaciones de la práctica como requisitos. No se ha identificado una implementación de almacenamiento persistente en los archivos examinados, por lo que no se presenta como funcionalidad confirmada.

## Desarrollo y colaboración

Proyecto realizado por **Iriem** y **Fernando Montelongo Moreno**, manteniendo la atribución de ambos participantes indicada en la documentación original.

**Fernando Montelongo Moreno** · Full Stack Developer · DAM + DAW

[![GitHub](https://img.shields.io/badge/GitHub-fmoorenoo-22252C?style=flat-square&logo=github)](https://github.com/fmoorenoo)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Perfil_profesional-712B39?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/fernando-montelongo/)

---

<div align="center">
  <sub>HIIT Timer · Aplicación Android de entrenamiento por intervalos</sub>
</div>
