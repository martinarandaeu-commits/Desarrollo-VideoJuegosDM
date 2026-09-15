# PR6 - Juego de Combate por Turnos

## Descripción
Juego 1 vs 1 desarrollado en Greenfoot para la asignatura Programación de Videojuegos. Un caballero azul (jugador) se enfrenta a un caballero rojo (enemigo controlado por el juego) en un combate por turnos hasta que uno de los dos llegue a 0 HP.

## Integrantes
- [Nombre integrante 1]
- [Nombre integrante 2]

## Mecánicas
- Combate por turnos
- Tres ataques del jugador (Ataque rápido, Ataque normal, Golpe fuerte)
- Daño y probabilidad de acierto
- Ataques automáticos del enemigo (Corte, Estocada)
- Barras de HP
- Animaciones (idle, ataque, daño, muerte)
- Feedback visual (texto flotante, indicador de turno, botones habilitados/deshabilitados)
- Victoria y derrota

## Controles
El jugador utiliza el mouse para seleccionar una acción haciendo clic en uno de los tres botones de ataque, disponibles únicamente durante su turno.

## Arquitectura
- **GestorCombate**: controlador central del combate; contiene la máquina de estados (`Estado`) y coordina turnos, resolución de ataques, daño y condición de victoria/derrota.
- **Unidad**: clase abstracta con HP y el sistema de animaciones común, heredada por `InfanteriaJugador` e `InfanteriaEnemiga`.
- **AccionCombate**: representa un ataque (nombre, daño, probabilidad de acierto).
- **Clases de interfaz**: `BotonAccion` (selección de acción), `BarraHP` (vida), `IndicadorTurno` (estado del combate) y `TextoFlotante` (feedback de daño/fallo).

## Ejecución
Abrir `project.greenfoot` y ejecutar el escenario.

## Recursos
Sprites del caballero azul (`player_*`) y del caballero rojo (`enemy_*`), y fondo del escenario (`scenario.jpeg`), provistos como parte de los materiales del curso.

Música de fondo: `musica_fondo.mp3`, adaptación al español de MEGALOVANIA (Undertale) por Riglock.
