# Desarrollo-VideoJuegosDM

# Contexto

Durante la primera etapa de la asignatura de *Programación de Videojuegos*, llevamos a cabo la dinámica **DecidArch** con el fin de ejercitar la toma de decisiones técnicas y de diseño, balanceando las distintas necesidades presentes en un proyecto de software.

Nuestra propuesta consiste en un **RPG 2D táctico por turnos**, desarrollado en Unity 2D bajo una estética **Pixel Art**. El núcleo del juego combina la selección estratégica de cartas con la exploración de una campaña estructurada a través de diversos mundos.


# Carta de Proyecto

El proyecto plantea una experiencia RPG 2D basada en combates por turnos donde el uso de **cartas de habilidades** rige las acciones de los personajes. Cada carta no solo dictamina el efecto táctico en batalla, sino que también desencadena el comportamiento y las animaciones visuales correspondientes. 

El recorrido abarca una campaña dividida en **30 niveles** distribuidos en 3 mundos, incorporando batallas contra jefes (*Bosses*) en los niveles 10, 20 y 30, junto con la opción de añadir un enfrentamiento final tras completar el nivel 30.


# Stakeholders

Identificamos dos roles fundamentales en el desarrollo, cada uno con enfoques e intereses particulares sobre el producto.

## Jugador

**Objetivo:** Disfrutar de combates estratégicos donde la buena elección de cartas permita superar retos con una curva de dificultad progresiva a lo largo de los 30 niveles.

* **Usability (Prioridad 2):** Requiere una interfaz clara para entender fácilmente la función de cada carta y desplegar sus acciones sin confusión.
* **Performance (Prioridad 2):** Busca una ejecución fluida, sin interrupciones ni caídas de fotogramas durante las animaciones en Pixel Art.
* **Security (Prioridad 0):** Aunque es necesario resguardar los datos del usuario, representa un aspecto secundario en este tipo de experiencia.

## Desarrollador

**Objetivo:** (David Pareles y Martín Aranda) Diseñar una arquitectura sólida, modular y fácil de mantener que permita cumplir con las entregas académicas y añadir nuevo contenido sin rehacer los sistemas base.

* **Maintainability (Prioridad 2):** Indispensable para incorporar héroes, cartas y escenarios sin que el código pierda orden ni se vuelva frágil.
* **Performance (Prioridad 2):** Crucial para asegurar que el motor gestione adecuadamente los recursos visuales e interfaces.
* **Availability (Prioridad 2):** Necesaria para mantener la estabilidad del proyecto y facilitar la integración continua de características.
* **Security (Prioridad 1):** Importante para prevenir errores o corrupción en el almacenamiento del progreso.


# Event Cards

Representan imprevistos o situaciones hipotéticas durante el desarrollo que podrían alterar el peso de las prioridades y obligar a reconsiderar el camino elegido:

* **Expansión del elenco de personajes:** La necesidad de agregar una gran cantidad de héroes sin sobrecargar el flujo de trabajo manual.
* **Degradación del rendimiento:** Problemas de fluidez al reproducir simultáneamente múltiples animaciones, efectos o acciones de combate.
* **Ajuste en los tiempos de entrega:** Reducción del plazo disponible, exigiendo métodos más ágiles para estructurar los 30 niveles.
* **Adición obligatoria del jefe post-juego:** Convertir el *boss* final opcional en un requerimiento para poner a prueba la flexibilidad del sistema.
* **Adaptación a múltiples resoluciones:** Garantizar que los gráficos Pixel Art se escalen correctamente sin deformarse en distintos monitores.


# Concern Cards

Son los dilemas de arquitectura o diseño que debemos resolver. Cada punto plantea opciones válidas que deben ser evaluadas detenidamente:

* **Estructura y gestión de niveles:** Decidir entre crear una escena independiente por nivel, emplear plantillas configurables mediante archivos de datos o aplicar generación procedural.
* **Modelado de las cartas:** Definir si la lógica de las habilidades se programa dentro del código de cada personaje o si se independiza usando objetos de datos (ej. `ScriptableObjects`).
* **Mecánica del sistema de combate:** Elegir la modalidad de juego entre turnos secuenciales, acciones ejecutadas simultáneamente o combate en tiempo real.
* **Vínculo entre cartas y sistema de animación:** Evaluar si cada personaje invoca sus animaciones de forma directa o si la carta emite una señal hacia un controlador genérico de animaciones.
* **Persistencia del progreso:** Seleccionar el formato de guardado entre puntos de control (*checkpoints*), múltiples slots independientes o un archivo de estado unificado (JSON o binario).
* **Desplazamiento en Pixel Art:** Determinar si el movimiento se restringe de forma estricta a una cuadrícula, si se utiliza movimiento libre continuo en 2D o si se adopta un enfoque híbrido.


# Conclusión

Este documento resume los aspectos centrales planteados durante la actividad: la visión del proyecto RPG 2D de cartas y sus 30 niveles, los dos perfiles de *stakeholders* con sus respectivos objetivos, el orden de prioridad asignado a las propiedades del sistema, el conjunto de eventos que pueden cambiar las reglas del proyecto (*Event Cards*) y las principales encrucijadas técnicas a resolver (*Concern Cards*).

El trabajo servirá de pauta para guiar la toma de decisiones a medida que avance la programación del prototipo en Unity 2D.
