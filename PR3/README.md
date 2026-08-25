# Nebula Strike

## Descripción del proyecto

**Nebula Strike** es un videojuego tipo *space shooter* desarrollado en **Greenfoot** como parte de una actividad de la asignatura de Programación de Videojuegos.

La actividad original consistía en implementar un mini-shooter lateral utilizando distintos **patrones de diseño orientados a objetos**, integrando principalmente:

- **Singleton**
- **Observer**
- **Strategy**
- **Object Pool**
- **State**

En la versión base, el jugador controlaba una nave ubicada al lado izquierdo de la pantalla, podía desplazarse verticalmente y disparar contra enemigos que aparecían desde el lado derecho. Además, el juego incluía un sistema de puntuación, disparo simple y triple, reutilización de proyectiles mediante un pool de balas y enemigos con distintos estados de movimiento.

A partir de esa base se desarrollaron nuevas mecánicas, enemigos, sistemas de dificultad y mejoras visuales con el objetivo de convertir el ejercicio inicial en un videojuego más completo, dinámico y desafiante.

---

## Objetivo de la actividad

El objetivo principal del ejercicio fue aplicar patrones de diseño dentro de un videojuego funcional en Greenfoot, entendiendo cómo distintas clases pueden trabajar de forma desacoplada y reutilizable.

La actividad permitió trabajar conceptos como:

- Gestión global del estado del juego.
- Actualización automática del marcador.
- Intercambio de estrategias de disparo.
- Reutilización de proyectiles.
- Cambio dinámico de comportamiento de los enemigos.
- Separación de responsabilidades entre clases.
- Extensión del proyecto mediante nuevas mecánicas.

Además del ejercicio solicitado, el proyecto fue ampliado con funcionalidades propias para mejorar la jugabilidad y aumentar progresivamente su dificultad.

---

# Juego original

La versión inicial del ejercicio correspondía a un shooter lateral sencillo.

El jugador podía:

- Mover la nave hacia arriba y hacia abajo.
- Disparar utilizando la barra espaciadora.
- Cambiar entre disparo simple y disparo triple.
- Destruir enemigos para obtener puntos.

Los enemigos aparecían desde el lado derecho de la pantalla y cambiaban su comportamiento utilizando distintos estados de movimiento, principalmente:

- `Avanzar`
- `Zigzag`

También se utilizaba un `GameManager` para gestionar los puntos y un `Marcador` que se actualizaba automáticamente mediante el patrón Observer.

---

# Mejoras realizadas

A partir de la versión original se implementaron diversas mejoras para aumentar la dificultad, variedad y profundidad del videojuego.

## 1. Sistema limitado de disparo triple

En el ejercicio original era posible seleccionar libremente el disparo triple, lo que hacía demasiado sencillo eliminar grandes cantidades de enemigos.

Para equilibrar esta mecánica se implementó un sistema de **habilidad cargable**.

Ahora:

- El disparo triple comienza bloqueado.
- El jugador obtiene carga al destruir enemigos.
- Existe una barra visual que muestra el porcentaje de carga.
- Al llegar al 100 %, aparece el estado `READY`.
- El jugador debe presionar la tecla `2` para activar la habilidad.
- Al activarse aparece el mensaje `TRIPLE SHOT ON!`.
- La habilidad solamente permanece activa durante un tiempo limitado.
- Mientras está activa, la barra comienza a descargarse.
- Cuando termina el tiempo, el arma vuelve automáticamente al disparo simple.
- Si se intenta activar antes de alcanzar el 100 %, la habilidad no se ejecuta.

Esto permite conservar el patrón **Strategy**, pero añade una condición de disponibilidad para la estrategia de disparo triple.

---

## 2. Barra de carga del Triple Shot

Se agregó la clase:

```text
BarraTriple
```

Esta muestra visualmente el estado de la habilidad especial.

La barra puede indicar:

```text
TRIPLE  40%

TRIPLE  READY - PRESS 2

TRIPLE  ACTIVE
```

Además de servir como interfaz para el jugador, permite saber cuándo conviene utilizar el disparo triple.

---

## 3. Sistema de Game Over

Se añadió una condición real de derrota.

El jugador pierde si:

- Un enemigo impacta directamente contra la nave.
- Una bala enemiga impacta contra la nave.
- Un láser enemigo alcanza al jugador.

Cuando esto ocurre se muestra:

```text
GAME OVER!
```

junto con información como:

- Puntaje final.
- Cantidad de enemigos eliminados.

---

## 4. Nave del jugador mejorada

La figura triangular utilizada originalmente fue reemplazada por una nave espacial dibujada directamente mediante `GreenfootImage`.

La nueva nave incorpora:

- Cuerpo principal.
- Cabina.
- Extensiones delanteras.
- Detalles visuales.

Esto permite darle una apariencia más cercana a un vehículo espacial manteniendo el requisito de no depender obligatoriamente de sprites externos.

---

# Nuevos tipos de enemigos

Una de las principales ampliaciones realizadas fue reemplazar el único enemigo original por diferentes tipos de amenazas.

## Asteroide

El enemigo básico fue transformado visualmente en un asteroide.

Características:

- 1 impacto para destruirlo.
- 10 puntos.
- 10 % de carga para el disparo triple.
- Puede utilizar los movimientos `Avanzar`, `Zigzag` y `Perseguir`.
- Si impacta con el jugador provoca Game Over.

Los asteroides son los enemigos más frecuentes.

---

## Caza enemigo de disparo simple

Se agregó una nave enemiga capaz de atacar al jugador.

Características:

- 2 impactos para destruirla.
- 20 puntos.
- 20 % de carga para el disparo triple.
- Se posiciona en el sector derecho de la pantalla.
- Se mueve verticalmente.
- Dispara proyectiles simples hacia el jugador.
- Sus disparos pueden eliminar al jugador.

---

## Caza enemigo de disparo triple

También se agregó un enemigo de mayor dificultad que utiliza un ataque similar al Triple Shot del jugador.

Características:

- 3 impactos para destruirlo.
- 30 puntos.
- 30 % de carga para el disparo triple.
- Movimiento vertical.
- Dispara tres proyectiles simultáneamente.
- Los proyectiles forman un abanico.
- Posee una frecuencia de disparo menor que el enemigo simple para mantener equilibrio.

El ataque utiliza tres ángulos distintos para generar el abanico de proyectiles.

---

# Enemigo especial de láser

Se implementó un enemigo especial diseñado para obligar al jugador a moverse constantemente.

Este enemigo posee varias fases:

```text
ENTRANDO
    ↓
CARGANDO
    ↓
DISPARANDO
    ↓
ESCAPANDO
```

## Fase de entrada

El enemigo aparece desde el sector derecho y se desplaza hasta alcanzar una posición de ataque.

## Fase de carga

Antes de disparar, el enemigo carga el láser durante aproximadamente 3 segundos.

Durante este tiempo aparece una línea de advertencia que muestra la trayectoria aproximada del ataque.

Esta línea parpadea para avisar al jugador que debe cambiar de posición.

## Fase de disparo

Después de finalizar la carga se genera un láser horizontal de gran tamaño.

Características:

- Permanece activo durante aproximadamente 4 segundos.
- Se desplaza verticalmente junto con la nave enemiga.
- Obliga al jugador a moverse para esquivarlo.
- El contacto con el láser provoca Game Over instantáneamente.

## Resistencia

El enemigo láser necesita:

```text
3 impactos
```

para ser destruido.

Si el jugador consigue eliminarlo antes de que termine su carga puede evitar completamente el ataque.

Recompensa:

- 50 puntos.
- 40 % de carga del Triple Shot.

---

# Sistema de vida para enemigos

La versión original destruía inmediatamente a cualquier enemigo después de recibir una bala.

La versión modificada incorpora resistencia individual.

| Enemigo | Impactos necesarios | Puntos | Carga Triple |
|---|---:|---:|---:|
| Asteroide | 1 | 10 | 10 % |
| Caza simple | 2 | 20 | 20 % |
| Caza triple | 3 | 30 | 30 % |
| Nave láser | 3 | 50 | 40 % |

De esta forma, los enemigos más peligrosos también entregan una recompensa mayor.

---

# Proyectiles enemigos

Se agregó una clase específica:

```text
BalaEnemiga
```

Los proyectiles enemigos:

- Se desplazan hacia el jugador.
- Poseen una apariencia distinta a las balas del usuario.
- Detectan colisión con la nave.
- Provocan Game Over al impactar.

Para los enemigos también se implementó un sistema basado en **Strategy**.

```text
EstrategiaAtaqueEnemigo
├── AtaqueSimpleEnemigo
└── AtaqueTripleEnemigo
```

De esta forma, una nave enemiga puede utilizar diferentes estrategias de ataque sin modificar directamente toda su lógica.

---

# Factory de enemigos

Se implementó:

```text
FabricaEnemigos
```

Su responsabilidad es decidir qué tipo de enemigo debe aparecer dependiendo del nivel actual de la partida.

Esto permite centralizar la creación de enemigos y mantener la lógica del mundo más organizada.

Ejemplo conceptual:

```text
Nivel 1
├── Principalmente asteroides
├── Cazas simples
└── Algunos cazas triples

Nivel 2
├── Asteroides
├── Cazas simples
├── Cazas triples
└── Primeros enemigos láser

Nivel 3+
├── Menos asteroides
├── Más cazas
└── Mayor probabilidad de enemigos láser
```

---

# Dificultad progresiva

La dificultad del juego aumenta automáticamente según el tiempo de supervivencia.

La partida se divide en distintos niveles.

### Nivel 1

- Mayor presencia de asteroides.
- Algunos cazas enemigos.
- Ritmo de aparición relativamente bajo.

### Nivel 2

- Mayor cantidad de cazas.
- Aparecen enemigos láser.
- Los enemigos aparecen con mayor frecuencia.

### Nivel 3

- Menor proporción de enemigos básicos.
- Mayor cantidad de enemigos armados.
- Más ataques triples.
- Mayor presencia del enemigo láser.

### Nivel 4

Representa la etapa de mayor dificultad.

- Los enemigos aparecen con mayor frecuencia.
- Aumenta la probabilidad de cazas y enemigos láser.
- El jugador debe administrar mejor su Triple Shot y mantenerse constantemente en movimiento.

---

# Nuevo escenario espacial

También se modificó la presentación visual del escenario.

El fondo blanco original fue reemplazado por un espacio oscuro.

Además se agregó la clase:

```text
Estrella
```

Las estrellas:

- Aparecen distribuidas aleatoriamente.
- Se desplazan hacia la izquierda.
- Poseen distintas velocidades.
- Al salir de la pantalla reaparecen en el extremo derecho.

Esto crea un efecto de movimiento y profundidad similar a un **parallax scrolling** sencillo.

---

# Patrones de diseño utilizados

## Singleton

Clase principal:

```text
GameManager
```

Se utiliza una única instancia global para administrar:

- Puntuación.
- Enemigos eliminados.
- Estado de Game Over.
- Carga del Triple Shot.
- Duración de la habilidad.

---

## Observer

Clases principales:

```text
Observador
Marcador
GameManager
```

El marcador se suscribe al `GameManager`.

Cuando cambia la puntuación, el GameManager notifica a sus observadores y el marcador se actualiza automáticamente.

---

## Strategy

Se utiliza en dos sistemas.

### Disparo del jugador

```text
EstrategiaDisparo
├── DisparoSimple
└── DisparoTriple
```

### Ataques enemigos

```text
EstrategiaAtaqueEnemigo
├── AtaqueSimpleEnemigo
└── AtaqueTripleEnemigo
```

Esto permite cambiar el comportamiento del disparo sin modificar la clase principal que ejecuta el ataque.

---

## Object Pool

Clases:

```text
PoolDeBalas
Bala
```

Las balas del jugador se crean previamente y son reutilizadas.

Cuando una bala deja de utilizarse:

- Se elimina temporalmente del mundo.
- No se destruye como objeto.
- Vuelve a quedar disponible dentro del pool.

Esto evita crear constantemente nuevos objetos durante la partida.

---

## State

Clases:

```text
Estado
├── Avanzar
├── Zigzag
└── Perseguir
```

Los asteroides pueden cambiar dinámicamente su comportamiento durante la partida.

El enemigo delega su movimiento al estado actual en lugar de concentrar todos los movimientos en una sola clase.

---

## Factory

Clase:

```text
FabricaEnemigos
```

Centraliza la creación de los distintos tipos de enemigos.

La fábrica permite generar:

- `Asteroide`
- `CazaSimple`
- `CazaTriple`
- `EnemigoLaser`

dependiendo del nivel y de una probabilidad aleatoria.

---

# Controles

| Tecla | Acción |
|---|---|
| `↑` | Mover nave hacia arriba |
| `↓` | Mover nave hacia abajo |
| `Espacio` | Disparar |
| `1` | Seleccionar disparo simple |
| `2` | Activar Triple Shot cuando esté completamente cargado |

---

# Estructura general del proyecto

```text
World
└── Espacio

Actor
├── Nave
├── Bala
├── BalaEnemiga
├── Marcador
├── BarraTriple
├── Estrella
├── AlertaLaser
├── RayoLaser
└── Enemigo
    ├── Asteroide
    ├── NaveEnemiga
    │   ├── CazaSimple
    │   └── CazaTriple
    └── EnemigoLaser
```

Clases e interfaces adicionales:

```text
GameManager
PoolDeBalas
FabricaEnemigos

Observador

Estado
Avanzar
Zigzag
Perseguir

EstrategiaDisparo
DisparoSimple
DisparoTriple

EstrategiaAtaqueEnemigo
AtaqueSimpleEnemigo
AtaqueTripleEnemigo
```

---

# Flujo general del juego

```text
Inicio
  ↓
Se crea Espacio
  ↓
Se inicializa GameManager
  ↓
Se crea el PoolDeBalas
  ↓
Se agrega Nave + Marcador + BarraTriple
  ↓
FabricaEnemigos comienza a generar amenazas
  ↓
El jugador destruye enemigos
  ↓
Obtiene puntos + carga de Triple Shot
  ↓
La dificultad aumenta con el tiempo
  ↓
Aparecen cazas y enemigos láser
  ↓
Colisión / proyectil / láser
  ↓
GAME OVER
```

---

# Principales diferencias respecto del ejercicio original

| Versión original | Versión mejorada |
|---|---|
| Un único tipo de enemigo | Cuatro tipos principales de enemigos |
| Enemigos representados como círculos rojos | Asteroides y naves enemigas |
| Todos los enemigos mueren de un impacto | Sistema de vida según enemigo |
| Los enemigos no disparan | Cazas con disparo simple y triple |
| Sin ataques especiales enemigos | Nave con láser cargable |
| Triple Shot disponible libremente | Triple Shot como habilidad cargable |
| Sin barra de habilidad | Barra visual de carga y duración |
| Sin condición de derrota completa | Sistema de Game Over |
| Fondo blanco | Escenario espacial |
| Sin sensación de desplazamiento | Estrellas con movimiento tipo parallax |
| Dificultad prácticamente constante | Dificultad progresiva por niveles |
| Creación directa de enemigos | `FabricaEnemigos` |
| State con Avanzar y Zigzag | Se añadió el estado Perseguir |
| Strategy solo para el jugador | Strategy también para ataques enemigos |

---

# Tecnologías

- **Java**
- **Greenfoot**
- **Programación Orientada a Objetos**
- **Patrones de diseño**
- **Git**
- **GitHub**

---

# Autores

Proyecto desarrollado como parte de los avances semanales de la asignatura de **Programación de Videojuegos**.

**Integrantes:**

- David Pareles
- Martín Aranda

---

# Estado del proyecto

El proyecto corresponde a una versión en desarrollo del videojuego.

Las funcionalidades implementadas actualmente permiten:

- Jugar una partida completa.
- Obtener puntos.
- Cargar y utilizar Triple Shot.
- Combatir diferentes tipos de enemigos.
- Recibir ataques enemigos.
- Enfrentar enemigos especiales de láser.
- Aumentar progresivamente la dificultad.
- Finalizar la partida mediante Game Over.

El proyecto continuará siendo ampliado mediante futuros avances y Pull Requests.
