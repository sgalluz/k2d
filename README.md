# K2D - Kotlin 2D Engine

<div style="display: flex; flex-direction: column">
  <pre style="width: 100%; text-align: center">
  ██╗  ██╗██████╗ ██████╗ 
  ██║ ██╔╝╚════██╗██╔══██╗
  █████╔╝  █████╔╝██║  ██║
  ██╔═██╗ ██╔═══╝ ██║  ██║
  ██║  ██╗███████╗██████╔╝
  ╚═╝  ╚═╝╚══════╝╚═════╝ 
  </pre>
  <p style="text-align: center"><b>A lightweight, high-performance 2D game engine built with Kotlin & Compose Desktop.</b></p>
</div>

<div style="text-align: center;">
  <img src="https://img.shields.io/badge/Kotlin-1.9.21-purple?logo=kotlin" alt="Kotlin">
  <img src="https://img.shields.io/badge/Compose_Desktop-1.5.11-blue?logo=jetbrains" alt="Compose Desktop">
  <img src="https://img.shields.io/badge/Platform-Desktop-orange?logo=linux&logoColor=white" alt="Platform Desktop">
  <img src="https://img.shields.io/badge/License-Apache_2.0-green?logo=apache" alt="License Apache 2.0">
  <img src="https://img.shields.io/badge/Status-Pre--Alpha-red?logo=blueprint" alt="Status Pre-Alpha">
  <img src="https://codecov.io/github/sgalluz/k2d/graph/badge.svg" alt="Coverage"/>
</div>

---

## 🎯 About the Project
**K2D** is an experimental 2D game engine born to explore the potential of **Compose Multiplatform** in game development. The goal is to provide an idiomatic, lightweight, and easy-to-use library for prototyping indie games on Desktop, with an architecture designed to be ready for future Multiplatform support (Android, Web, iOS).

### Why K2D?
* **Declarative**: Leverages the power of Compose to manage rendering and game UI state.
* **Desktop-First**: Initially optimized for the JVM to ensure stability and rapid iteration.
* **Minimalist ECS**: An essential Entity Component System, keeping things simple and performant.
* **Coroutines-Ready**: Smooth handling of asynchronous inputs and parallel workloads.

The project is currently **desktop-first** and **pre-alpha**.  
Breaking changes are expected.

---

## 🏗️ Architecture & Roadmap
The project follows a modular approach to decouple the engine logic from the actual game implementation.

### Development Status
- [x] **Core Loop**: Deterministic game loop based on `withFrameNanos`.
- [x] **ECS Core**: World implementation and entity management.
- [ ] **Rendering Engine**: Support for Sprites, Primitives, and Text.
- [ ] **Input System**: Cross-platform Keyboard and Mouse handling via Flow.
- [ ] **Asset Manager**: Asynchronous loading of textures and sounds.
- [ ] **Physics**: Minimal AABB (Axis-Aligned Bounding Box) collisions.

---

## 📂 Repository Structure
K2D is structured around a clear separation of responsibilities:
```text
engine/         # The core engine library (GameLoop, ECS, Rendering).
├── core        # Core engine logic (time, loop, simulation)
├── ecs         # Entity Component System
├── rendering   # Rendering abstractions
├── runtime     # Platform-specific adapters
sample/         # A demo game used to test and showcase new features.
docs/           # Technical documentation and guides.
```

Compose Desktop is used exclusively inside the `runtime` layer and is treated as:
> a runtime and rendering adapter, not as engine logic.

This separation is intentional and enables:
- easier testing
- clearer boundaries
- future multiplatform support

---

## 🧪 Testing & Code Coverage
K2D follows a **Test-Driven Development (TDD)** approach, treating testing as a
foundational pillar of the project.

The testing strategy is **logic-first**:
- Core engine logic is developed and validated through unit tests and covered by JaCoCo
- Runtime and Compose-related code is verified via integration and behavioral tests
- Coverage metrics intentionally exclude Compose-specific code, as compiler rewriting
  makes line coverage unreliable

Code coverage is used as a **signal**, not as a hard requirement, and is always
interpreted in the context of test quality and intent.

---

## 👾 Quick Preview
```kotlin
fun main() = application {
    var pos by remember { mutableStateOf(Offset.Zero) }

    Window(onCloseRequest = ::exitApplication) {
        k2dCanvas(
            onUpdate = { dt -> pos += Offset(100f, 100f) * dt },
            onRender = { drawRect(Color.Cyan, pos, Size(50f, 50f)) }
        )
    }
}

```

---

## 🚧 Project Status & Roadmap
K2D is in **active development** and currently focuses on:
- core loop and timing 
- ECS foundations
- rendering primitives

Detailed technical documentation can be found in the docs/ folder (WIP).

---

## 🤝 Contributing
Contributions are welcome!  
Please read [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines and setup instructions.

---

## 📄 License
Distributed under the Apache License 2.0.  
See [LICENSE](LICENSE) for more information.

---

<p style="text-align: center; width: 100%">Built with ❤️ for the Kotlin community</p>