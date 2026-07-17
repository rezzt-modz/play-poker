---
tags: [play-poker, descripcion, proyecto, obsidian]
fecha: 2026-07-17
---

# play-poker: descripcion general del proyecto

## vision e idea

**play-poker** es un mod de [[minecraft]] java edition pensado para la version **1.20.1**. su proposito es introducir minijuegos de casino dentro del mundo de minecraft de una forma inmersiva, funcional y visualmente coherente con el estilo del juego.

la idea principal es permitir a los jugadores construir y usar objetos de casino reales: mesas de blackjack, maquinas tragaperras y fichas de juego. cada elemento no es solo decorativo, sino que tiene reglas, estados y mecanicas propias que transforman una simple sala de bloques en una experiencia de juego completa.

## concepto central

el mod se inspira en los datapacks de casino tradicionales, especialmente en el datapack de referencia **777**, que implementa juegos de azar usando comandos, entidades y modelos personalizados. sin embargo, play-poker eleva ese concepto a un mod de java: los juegos pasan a estar gestionados por bloques, entidades de bloque y sistemas de interaccion propios, lo que ofrece mayor estabilidad, flexibilidad y facilidad de uso.

en lugar de depender exclusivamente de comandos y estructuras de datos del datapack, el mod encapsula la logica en codigo. esto permite un control mas preciso del estado de las partidas, la distribucion de cartas, el calculo de pagos y la representacion visual de cada elemento sobre la mesa.

## funcionalidades principales

### mesa de blackjack (`blackjack_table`)

es el primer contenido jugable implementado. se trata de un bloque colocable que simula una mesa de blackjack completa con crupier, cartas, apuestas e interacciones.

aspectos clave:

- se orienta hacia el jugador que la coloca usando la propiedad `facing`.
- el estado de la partida se guarda en una entidad de bloque (`blockentity`).
- no utiliza interfaz grafica: el juego se controla mediante entidades `interaction` invisibles.
- admite hasta **tres manos simultaneas** por mesa.
- el crupier sigue reglas automaticas: pide cartas hasta alcanzar **17 o mas**.
- los pagos se calculan automaticamente segun el resultado de cada mano.

### ficha de poker (`poker_chip`)

un item de ficha de casino que sirve como icono y base visual del mod. actualmente es un item de creativo que identifica la pestana personalizada del mod y establece la tematica general del proyecto.

aspectos clave:

- textura de **16x16** basada en `agent-resources/black-chip.png`.
- pestana creativa propia llamada **play poker**.
- disponible solo en el inventario creativo, sin crafteo ni recetas de intercambio.

## mecanicas del blackjack

### fases de una partida

una mesa de blackjack tiene dos fases claras: preparacion y juego.

#### fase de preparacion (idle)

en esta fase los jugadores ocupan sus asientos y preparan sus apuestas.

1. el jugador se acerca a un asiento libre.
2. hace clic derecho sobre la entidad de **apuesta** (`bet`) mientras sostiene un item para dejarlo como apuesta.
3. puede hacer clic izquierdo sobre la apuesta para recuperarla.
4. cuando este listo, hace clic derecho sobre la entidad de **listo** (`ready`) para marcar su asiento.
5. la ronda comienza cuando todos los asientos ocupados esten listos.

#### fase de juego (playing)

una vez iniciada la ronda, cada jugador activo recibe dos cartas y el crupier tambien recibe dos, una de ellas oculta.

1. el jugador decide si pide otra carta con **hit** o se planta con **stand**.
2. si la suma de sus cartas supera **21**, pierde automaticamente (`bust`).
3. cuando todos los jugadores terminan, el crupier revela su carta oculta.
4. el crupier pide cartas hasta llegar a **17 o mas**.
5. se comparan las manos y se resuelven las apuestas.

### reglas de pago

| resultado | pago |
|---|---|
| jugador obtiene 21 | **3x** la apuesta |
| victoria normal o bust del crupier | **2x** la apuesta |
| empate (`push`) | se devuelve la apuesta |
| derrota o bust del jugador | se pierde la apuesta |

### representacion visual

la mesa utiliza distintos tipos de entidades para mostrar la partida sin necesidad de abrir ningun menu:

- **itemdisplay**: muestra el modelo 3d de la mesa y los items apostados.
- **textdisplay**: muestra las cartas usando glifos unicode y los totales de cada mano con digitos matematicos en negrita.
- **villager**: actua como crupier frente a la mesa.
- **interaction**: zonas invisibles que el jugador activa para apostar, prepararse, pedir o plantarse.

## contenido actual

| elemento | estado | descripcion |
|---|---|---|
| mesa de blackjack | implementado | bloque, entidad de bloque, reglas, visuales e interacciones completas |
| ficha de poker | implementado | item creativo y pestana personalizada |
| maquina tragaperras | planeado | proximo contenido principal basado en el datapack 777 |

## experiencia de usuario

el mod esta disenado para que cualquier jugador pueda acercarse a una mesa e interactuar sin conocimientos tecnicos. no hay comandos que memorizar ni interfaces complejas. todo se resuelve a traves de clics directos sobre la mesa, de forma similar a como funcionan los datapacks de interaccion por entidades.

la experiencia busca ser:

- **inmediata**: colocar la mesa, apostar y jugar en pocos pasos.
- **visual**: las cartas, apuestas y resultados se ven directamente en el mundo.
- **cooperativa**: varios jugadores pueden participar en la misma mesa al mismo tiempo.
- **equilibrada**: las reglas de pago y el comportamiento del crupier son claros y predecibles.

## diseno y referencias

el proyecto toma como referencia principal el datapack **777** almacenado en `agent-resources/777_datapack/`. ese datapack incluye una mesa de blackjack y una maquina tragaperras completamente funcionales usando comandos de minecraft.

play-poker no copia el datapack tal cual, sino que reinterpreta sus mecanicas en codigo java. esto permite:

- evitar la dependencia de cientos de archivos `mcfunction`.
- centralizar el estado en entidades de bloque.
- reutilizar los assets visuales del datapack sin depender de su logica de comandos.
- abrir la puerta a futuras mejoras como configuracion, sonidos personalizados y soporte multijugador mas robusto.

## objetivos futuros

- implementar la maquina tragaperras con sus animaciones y premios.
- anadir sonidos y efectos visuales adicionales.
- mejorar la integracion con sistemas de economia de servidores.
- explorar soporte para otros loaders como fabric o neoforge.
- anadir mas juegos de mesa y fichas de distintos valores.

## notas de diseno

- el mod prioriza la jugabilidad sobre la configuracion avanzada en esta etapa inicial.
- todas las reglas de blackjack siguen una logica simplificada: el crupier no distingue entre soft 17 y hard 17.
- el estado del juego se mantiene en el servidor; no se usa red personalizada.
- los assets visuales importados del datapack 777 se ajustan al mod para mantener la coherencia estetica.

---

*documento de descripcion general para el vault de obsidian.*
*ultima actualizacion: 2026-07-17*
