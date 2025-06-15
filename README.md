# FocusTimer

This repository contains the FocusTimer application built with JavaFX.

## Building

To build the fat JAR, run:

```bash
chmod +x gradlew
./gradlew shadowJar
```

The resulting `build/libs/FocusTimer-all.jar` can be packaged with `jpackage`.

## GitHub integration

To push your changes, first create a GitHub repository and add it as a remote:

```bash
git remote add origin https://github.com/USER/REPO.git
```

Then push the current branch:

```bash
git push -u origin work
```

Replace `USER/REPO` with your repository path.
