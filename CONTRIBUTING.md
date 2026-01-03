# Contributing to K2D

First of all, thank you for your interest in contributing to **K2D – Kotlin 2D Engine** ❤️

All contributions are welcome, from bug reports to documentation improvements.

> ⚠️ **Project Status**  
> K2D is currently in **Pre-Alpha**. APIs are unstable and may change frequently.

---

## 📌 Ways to Contribute

You can contribute by:
- Reporting bugs
- Proposing new features or improvements
- Improving documentation
- Refactoring or cleaning up code
- Implementing roadmap items

If you are new to the project, look for issues labeled **`good first issue`**.

---

## 🐞 Reporting Bugs

Before opening a bug report:
- Make sure the issue hasn’t already been reported
- Test against the latest `main` branch if possible

When opening an issue, please use the **Bug Report** template and provide:
- Clear reproduction steps
- Expected vs actual behavior
- K2D, Kotlin, and Compose versions
- OS information

---

## ✨ Requesting Features

Feature requests should:
- Be aligned with the project goals
- Prefer simple and composable solutions
- Consider the Pre-Alpha nature of the engine

Please use the **Feature Request** issue template.

---

## 🔧 Development Setup

Basic requirements:
- JDK 21+
- Kotlin 1.9.x
- Gradle (wrapper included)
- Compose Desktop compatible environment

Clone the repository:
```bash
git clone https://github.com/sgalluz/k2d.git
cd k2d
```

Run the sample project:
```bash
./gradlew :sample:run
```

---

## 🌿 Branching Strategy
* `main` → active development (no long-lived stable branch yet)
* Feature branches: `feature/short-description`
* Bug fix branches: `fix/short-description`

---

## ✅ Commit Guidelines
* Use clear and descriptive commit messages
* Prefer small, focused commits
* Reference issues when applicable
  Example:
  ```text
  Add basic sprite batching to renderer
  Fixes #42
  ```

---

## 🔁 Pull Request Process
1. Fork the repository
2. Create a branch from main
3. Make your changes
4. Ensure the project builds successfully
5. Open a Pull Request using the provided template
   
Your PR should:
* Solve one problem at a time
* Avoid unnecessary public API exposure
* Include documentation or comments when needed

---

## 🧪 Testing
Testing is still evolving in K2D.

When possible:
* Add unit tests for core logic
* Manually test rendering or input-related changes
* Clearly state how the change was tested in the PR

---

## 🏷️ Labels & Issue Management
This project uses labels to organize issues and PRs.
See the Label Strategy section in the repository for details.

---

## 📜 Code of Conduct
By participating in this project, you agree to follow a respectful and constructive behavior toward others.

---

Thanks again for contributing to K2D.<br><i>Built with ❤️ for the Kotlin community</i>.