# MJGame — Videojuego de plataformas en Greenfoot

Proyecto desarrollado en **Java con Greenfoot** para la asignatura de **Programación de Videojuegos**. La implementación toma como base la guía de sprites, estados y salto parabólico y la amplía con un escenario inspirado en *Michael Jackson's Moonwalker*.

## Jugabilidad

El juego utiliza un mapa único de **958 × 550 px** dividido en **4 pisos**, mostrado mediante una vista de **800 × 500 px** con cámara scroll. El jugador comienza en el piso 4 y debe avanzar utilizando las tres escaleras hasta alcanzar la puerta final del piso 1.

### Controles

| Tecla | Acción |
|---|---|
| ← / → | Caminar |
| Espacio | Saltar |
| ↑ / ↓ | Subir o bajar por una escalera cuando el jugador está en la zona correspondiente |
| K | Ataque de patada |
| D | Baile de giro |
| F | Inclinación (*lean*) |
| ↑ frente a la puerta del piso 1 | Completar el juego |

El jugador comienza con **3 vidas**. Los enemigos necesitan **2 golpes** para ser derrotados. Al recibir daño, el jugador obtiene un breve período de invulnerabilidad. Derrotar enemigos entrega puntos.

## Arquitectura

La versión final separa responsabilidades para mantener las clases principales comprensibles y extensibles:

```text
Mundo
├── Escalera
├── Jugador
│   ├── AnimadorJugador
│   ├── AtaqueJugador
│   ├── BaileJugador
│   ├── MovimientoEscaleraJugador
│   └── SaludJugador
├── Enemigo
│   └── AnimadorEnemigo
├── Marcador
└── Decoracion
```

### Clases principales

- **Mundo**: escenario, cámara, pisos, escaleras, enemigos, música y condiciones de término.
- **Jugador**: estado general, coordenadas de mundo, movimiento horizontal, salto parabólico, caída, física de suelo, puntuación y puerta final.
- **AnimadorJugador**: sprites y animaciones del jugador.
- **AtaqueJugador**: ataque, alcance, sonidos y golpes a enemigos.
- **BaileJugador**: baile con D/F y baile automático por inactividad.
- **MovimientoEscaleraJugador**: transición entre pisos y cooldown de escaleras.
- **SaludJugador**: vidas, daño, contacto, invulnerabilidad y muerte.
- **Enemigo**: estados, persecución/patrulla, golpes, tambaleo y derrota.
- **AnimadorEnemigo**: sprites y animaciones del enemigo.
- **Marcador**: HUD de vida y puntuación.
- **Escalera**: objeto de datos que representa la conexión entre dos pisos.
- **Decoracion**: actor visual para elementos decorativos del escenario.

## Salto parabólico

El movimiento vertical utiliza la ecuación:

```text
y(t) = y0 - v0·t + 0.5·g·t²
```

Los estados `SALTANDO` y `CAYENDO` permiten separar visualmente ambas fases del movimiento.

## Condiciones de término

- **GAME OVER**: el jugador pierde sus 3 vidas.
- **GAME COMPLETE**: el jugador llega a la puerta del piso 1 y presiona ↑.

## Cómo ejecutar

1. Abrir **Greenfoot**.
2. Seleccionar **Scenario / Open** y abrir la carpeta del proyecto.
3. Compilar el escenario.
4. Pulsar **Run**.

## Estructura del proyecto

```text
MJGame/
├── *.java
├── project.greenfoot
├── images/
├── sounds/
└── README.md
```

Los archivos compilados (`*.class`) y metadatos generados (`*.ctxt`) no forman parte del código fuente versionado en Git.

## Integrantes

- David Pareles
- Martín Aranda

## Nota sobre recursos

Proyecto de carácter académico. Los nombres, sprites, música y otros recursos asociados a *Michael Jackson's Moonwalker* y *Smooth Criminal* pertenecen a sus respectivos titulares de derechos. Su inclusión en este proyecto no implica propiedad ni licencia sobre dichos recursos.