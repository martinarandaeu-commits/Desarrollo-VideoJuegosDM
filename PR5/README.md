# Granja Zombie - Rogue-like en Greenfoot

Continuacion de la guia "Rogue-like Simple en Greenfoot": el mismo diseno
(oleadas con limites de memoria/rendimiento) pero con el mapa y los
personajes de tu propio tileset, y con rotacion del sprite segun la
direccion de movimiento (WASD).

## Como abrirlo

1. Abre Greenfoot.
2. `Escenario -> Abrir` y selecciona esta carpeta (`Rougelike GAME`).
   - Si tu version de Greenfoot no reconoce el `project.greenfoot`
     incluido, crea un `Nuevo escenario` vacio en esta misma carpeta:
     Greenfoot respetara los `.java` y la carpeta `images` que ya
     existen y solo necesitaras volver a agregar las clases al
     diagrama (Greenfoot las detecta automaticamente al compilar).
3. Compila (`Compilar`) y presiona el boton de ejecutar con el mundo
   `JuegoWorld`.

## Controles

| Tecla | Accion |
|---|---|
| W A S D | Moverse (el sprite rota hacia donde caminas) |
| Espacio / clic | Disparar con el arma equipada |
| 1 | Cambiar a pistola |
| 2 | Cambiar a escopeta (mas dano, dispara en abanico, gasta mas municion) |

La municion se regenera lentamente sola. El dinero se gana matando
zombis. Al llegar a 0 de vida termina la partida.

## Estructura del codigo

| Clase | Responsabilidad |
|---|---|
| `JuegoWorld` | Arma el escenario, guarda los obstaculos del mapa, el dinero y decide el fin del juego. |
| `Soldado` | Clase base abstracta: vida, danio, rotacion hacia la direccion de movimiento, y el chequeo de obstaculos al moverse. |
| `Jugador` | Lee teclado, mueve, dispara, administra municion y arma equipada (`enum Arma`). |
| `SoldadoEnemigo` | Persigue al jugador y lo ataca al tocarlo. No sabe nada de oleadas. |
| `Bala` | Proyectil con ciclo de vida propio (se crea, viaja, impacta o expira). |
| `GestorOleadas` | Calcula cuantos zombis existen, con que vida/velocidad/dano, y cuando empieza la siguiente oleada. |
| `HUD` | Dibuja vida, municion, dinero, oleada y arma equipada en su propia franja debajo del mapa. |
| `FabricaImagenes` | Carga cada imagen una sola vez desde `images/` y la reutiliza entre todos los actores del mismo tipo. |
| `Obstaculo` | Rectangulo solido simple usado para las colisiones contra el mapa. |

La carpeta `images/` contiene `mapa.png` (el escenario ya compuesto a
partir de tu tileset), `jugador.png`, `zombie.png`, `bala.png` y los
iconos de arma (`slot_pistola.png`, `slot_escopeta.png`).

## Formulas de dificultad (identicas a la guia original)

```
enemigos totales de la oleada = min(5 + 3*oleada, 80)
enemigos simultaneos          = min(6 + 2*oleada, 35)   <-- nunca crece sin limite
vida del zombi                = 20 + 4*oleada
dano del zombi                = min(4 + oleada, 15)
intervalo de aparicion        = max(12, 50 - 3*oleada)
```

## Tabla driver/concern -> decision -> codigo

Esta es la tabla de cierre de ambas guias, ya completada para este
proyecto (sirve como ejemplo o como base para tu propia entrega):

| Driver o concern | Decision tomada | Elemento de codigo |
|---|---|---|
| Respuesta visual | Un solo sprite por personaje, rotado (no cambiado) segun la direccion de movimiento | `Soldado.mirarHacia(dx, dy)` |
| Mantenibilidad | Comportamiento comun (vida, morir, rotar, chocar con obstaculos) vive una sola vez en la clase base | `Soldado` (heredada por `Jugador` y `SoldadoEnemigo`) |
| Movimiento natural | Movimiento por vector normalizado, para que la diagonal no sea mas rapida que caminar recto | `Jugador.controlarMovimiento()` |
| Fluidez / Rendimiento | El HUD no se redibuja en cada ciclo, solo cada 6 | `HUD.redibujar()` + `contador` |
| Extensibilidad | Arma representada con `enum` en vez de numeros magicos; agregar una tercera arma no rompe el resto | `Jugador.Arma` |
| Consistencia | El estado logico (vida, municion, oleada) y lo que muestra el HUD siempre se leen desde los mismos metodos (`getVida()`, `getMunicion()`, `getOleada()`) | `HUD.redibujar()` |
| Memoria | Las imagenes se cargan una unica vez y se comparten entre todos los actores del mismo tipo | `FabricaImagenes` |
| Escalabilidad de dificultad | Enemigos simultaneos limitados a 35 aunque la oleada tenga 80 enemigos en total | `GestorOleadas.maxSimultaneos()` |

## Posibles extensiones

- Subclases de `SoldadoEnemigo` (`EnemigoRapido`, `EnemigoPesado`) que
  cambien solo los parametros del constructor.
- Un segundo estado visual (por ejemplo un breve destello al recibir
  dano) usando el mismo patron de contador que usa el HUD.
- Recoger objetos del mapa (botiquin, municion) como nuevos `Actor`
  que se eliminan al tocarlos, similar a como `Bala` se elimina al
  impactar.
