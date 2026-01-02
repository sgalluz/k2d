# K2D - Kotlin 2D Engine

<p align="center">
  <pre align="center">
  ██╗  ██╗██████╗ ██████╗ 
  ██║ ██╔╝╚════██╗██╔══██╗
  █████╔╝  █████╔╝██║  ██║
  ██╔═██╗ ██╔═══╝ ██║  ██║
  ██║  ██╗███████╗██████╔╝
  ╚═╝  ╚═╝╚══════╝╚═════╝ 
  </pre>
  <p align="center"><b>A lightweight, high-performance 2D game engine built with Kotlin & Compose Desktop.</b></p>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9.21-purple?logo=kotlin" alt="Kotlin">
  <img src="https://img.shields.io/badge/Compose_Desktop-1.5.11-blue?logo=jetbrains" alt="Compose Desktop">
  <img src="https://img.shields.io/badge/Platform-Desktop-orange?logo=linux&logoColor=white" alt="Platform Desktop">
  <img src="https://img.shields.io/badge/License-Apache_2.0-green" alt="License Apache 2.0">
  <img src="https://img.shields.io/badge/Status-Pre--Alpha-red" alt="Status Pre-Alpha">
  <img src="https://codecov.io/github/sgalluz/K2D/graph/badge.svg?token=R00L4MD9N3" alt="Coverage"/>
</p>

---

## 🎯 About the Project

**K2D** is an experimental 2D game engine born to explore the potential of **Compose Multiplatform** in game development. The goal is to provide an idiomatic, lightweight, and easy-to-use library for prototyping indie games on Desktop, with an architecture designed to be ready for future Multiplatform support (Android, Web, iOS).

### Why K2D?
* **Declarative**: Leverages the power of Compose to manage rendering and game UI state.
* **Desktop-First**: Initially optimized for the JVM to ensure stability and rapid iteration.
* **Minimalist ECS**: An essential Entity Component System, keeping things simple and performant.
* **Coroutines-Ready**: Smooth handling of asynchronous inputs and parallel workloads.

---

## 🏗️ Architecture & Roadmap

The project follows a modular approach to decouple the engine logic from the actual game implementation.

### Development Status
- [x] **Core Loop**: Deterministic game loop based on `withFrameNanos`.
- [ ] **ECS Core**: World implementation and entity management.
- [ ] **Rendering Engine**: Support for Sprites, Primitives, and Text.
- [ ] **Input System**: Cross-platform Keyboard and Mouse handling via Flow.
- [ ] **Asset Manager**: Asynchronous loading of textures and sounds.
- [ ] **Physics**: Minimal AABB (Axis-Aligned Bounding Box) collisions.

---

## 👾 Quick Preview

```kotlin
fun main() = application {
    val game = K2DGame() // Initialize the engine

    Window(title = "K2D Sample Game", onCloseRequest = ::exitApplication) {
        GameCanvas(
            onUpdate = { delta -> 
                game.update(delta) 
            },
            onRender = { canvas -> 
                game.render(canvas) 
            }
        )
    }
}
```

---

## 📂 Repository Structure
- engine/: The core engine library (GameLoop, ECS, Rendering).
- sample/: A demo game used to test and showcase new features.
- docs/: Technical documentation and guides.

---

## 🤝 Contributing
K2D is an open-source project and we welcome contributors of all skill levels!
1. Fork the project.
2. Create a Branch for your feature (git checkout -b feature/AmazingFeature).
3. Commit your changes (git commit -m 'Add some AmazingFeature').
4. Push to the branch (git push origin feature/AmazingFeature).
5. Open a Pull Request.

Check our Issues tab to find "Good First Issues" to get started.

---

## 📄 License
Distributed under the Apache License 2.0. See [LICENSE](LICENSE) for more information.

---

<p align="center">Built with ❤️ for the Kotlin community</p>