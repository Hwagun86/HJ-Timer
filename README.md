# FocusTimer

Windows에서 동작하는 JavaFX 기반 집중 타이머 앱입니다.

## 실행 파일

이 저장소에는 이미 빌드된 Windows 실행 파일이 포함되어 있습니다.

- `installer-output/FocusTimer/FocusTimer.exe`

## 로컬 빌드

### Fat JAR 생성

```bash
chmod +x gradlew
./gradlew shadowJar
```

생성 결과물:

- `build/libs/FocusTimer-all.jar`

### Windows EXE 패키징

Windows 환경에서 JDK 17+와 `jpackage`가 준비되어 있다면:

```powershell
./gradlew shadowJar
jpackage --type app-image `
  --name FocusTimer `
  --input build/libs `
  --main-jar FocusTimer-all.jar `
  --main-class TimerApp `
  --icon installer-output/FocusTimer/FocusTimer.ico
```

생성된 앱 이미지 내부의 `.exe`를 실행하면 됩니다.
