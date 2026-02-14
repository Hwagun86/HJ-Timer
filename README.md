# FocusTimer

예전에 만들다 실패한 타이머 요구사항을 기준으로 다시 정리한 **포커스 타이머**입니다.

## 이번 버전에 반영한 요구사항 분석
기존 코드/데이터를 기준으로 사용자가 원했던 흐름을 다음처럼 해석했습니다.

- 전체 작업 시간(글로벌 타이머)을 크게 표시
- 특정 프로그램들(최대 6개)만 추적
- 현재 활성 창 기준으로 프로그램별 사용 시간을 누적
- 각 프로그램별 점유 비율(%) 표시
- 등록된 프로그램 목록 저장(`data.json`)
- 통계/리포트 팝업으로 한 번에 확인
- 1시간마다 트레이 알림

## 실행

```bash
chmod +x gradlew
./gradlew run
```

## 빌드 (Fat JAR)

```bash
./gradlew shadowJar
```

생성물:

- `build/libs/FocusTimer-all.jar`

## 참고

- 앱은 Windows 활성 창 API(JNA User32/Kernel32)를 사용하므로, 실제 추적 기능은 Windows 환경에서 정상 동작합니다.
