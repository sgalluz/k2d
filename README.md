# 🚀 K2D – Kotlin 2D Compose Engine

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.1-purple.svg?style=flat&logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](LICENSE)

**K2D** is a lightweight, cross-platform 2D game engine built with **Kotlin Multiplatform** and **Compose Multiplatform
**.

It aims to provide a declarative, idiomatic, and simple way to build 2D games for Desktop, Android, and Web.

By leveraging the power of **Skia** (via Skiko) and the modern UI paradigms of Compose, K2D allows developers to bridge
the gap between reactive UI and high-performance game logic.

---

## 🎯 Key Features

- 🌀 **Deterministic Game Loop**: Built-in support for fixed and variable time steps using Kotlin Coroutines.
- 🧩 **Lightweight ECS**: A simple yet powerful Entity Component System designed for performance and flexibility.
- 🎨 **Reactive Rendering**: Use the familiar Compose `Canvas` API to render sprites, shapes, and text.
- ⌨️ **Unified Input System**: Handle Keyboard, Mouse, and Touch inputs through a single, cross-platform Flow-based API.
- 📦 **Asset Management**: Asynchronous loading for textures, sounds (WIP), and data files.

---

## 🏗 Project Structure

```text
K2D/
├── engine/               # Core engine logic (Multiplatform)
│   ├── commonMain/       # ECS, Loop, and Math
│   ├── desktopMain/      # Desktop-specific optimizations
│   └── androidMain/      # Android lifecycle integration
├── k2d-samples/          # Example games and demos
└── docs/                 # Documentation and tutorials
```

---

## 🚀 Quick Start (Preview)

K2D is designed to be intuitive. Here is what a basic setup looks like:

```Kotlin
fun main() = K2DApplication {
  game {
    onStart {
      createEntity {
        add(Position(100f, 100f))
        add(Velocity(2f, 0f))
        add(Sprite("player.png"))
      }
    }

    onUpdate { deltaTime ->
      // Custom global logic here
    }
  }
}
```

---

## 🗺 Roadmap (MVP)

- [ ] Core: Game loop with stable FPS management.
- [ ] ECS: Entity creation, component attachment, and basic systems.
- [ ] Render: Support for static sprites and sprite-sheet animations.
- [ ] Physics: AABB Collision detection and basic resolution.
- [ ] Input: Desktop keys and Android touch gestures.

---

## 🤝 Contributing

We love contributors! K2D is in its early experimental stage.

Whether you want to fix a bug, improve documentation, or suggest a new feature, feel free to:

1. Open an Issue: Discuss ideas or report bugs.
2. Submit a PR: Check our "Good First Issues" to get started.

---

## 📜 License

K2D is distributed under the Apache License 2.0. See [LICENSE](LICENSE) for more information.

---

*Built with ❤️ for the Kotlin community.*
