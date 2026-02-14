import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.geometry.*;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Toolkit;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public class TimerApp extends Application {
    // ── 친숙명 매핑 테이블 (150개) ──
    private static final Map<String,String> FRIENDLY_NAMES;
    static {
        Map<String,String> m = new HashMap<>();
        // 1–20
        m.put("hwp.exe",       "한글 2020");
        m.put("msedge.exe",    "Microsoft Edge");
        m.put("chrome.exe",    "구글 크롬");
        m.put("firefox.exe",   "파이어폭스");
        m.put("notepad.exe",   "메모장");
        m.put("calc.exe",      "계산기");
        m.put("winword.exe",   "MS 워드");
        m.put("excel.exe",     "MS 엑셀");
        m.put("powerpnt.exe",  "MS 파워포인트");
        m.put("outlook.exe",   "MS 아웃룩");
        m.put("teams.exe",     "MS 팀즈");
        m.put("discord.exe",   "Discord");
        m.put("zoom.exe",      "Zoom");
        m.put("spotify.exe",   "Spotify");
        m.put("vlc.exe",       "VLC 플레이어");
        m.put("mpv.exe",       "MPV 플레이어");
        m.put("photoshop.exe", "Adobe Photoshop");
        m.put("steam.exe",     "Steam");
        m.put("code.exe",      "VS Code");
        m.put("devenv.exe",    "Visual Studio");
        // 21–40
        m.put("skype.exe",     "Skype");
        m.put("teamspeak.exe", "TeamSpeak");
        m.put("slack.exe",     "Slack");
        m.put("telegram.exe",  "Telegram");
        m.put("whatsapp.exe",  "WhatsApp");
        m.put("onedrive.exe",  "OneDrive");
        m.put("dropbox.exe",   "Dropbox");
        m.put("reader.exe",    "Adobe Reader");
        m.put("acrobat.exe",   "Adobe Acrobat");
        m.put("edge.exe",      "Edge(레거시)");
        m.put("iexplore.exe",  "Internet Explorer");
        m.put("paint.exe",     "페인트");
        m.put("mspaint.exe",   "페인트(신 버전)");
        m.put("winver.exe",    "Windows 정보");
        m.put("control.exe",   "제어판");
        m.put("explorer.exe",  "파일 탐색기");
        m.put("taskmgr.exe",   "작업 관리자");
        m.put("cmd.exe",       "명령 프롬프트");
        m.put("powershell.exe","PowerShell");
        m.put("git.exe",       "Git");
        // 41–60
        m.put("node.exe",      "Node.js");
        m.put("python.exe",    "Python");
        m.put("java.exe",      "Java");
        m.put("mysql.exe",     "MySQL");
        m.put("mongod.exe",    "MongoDB");
        m.put("postgres.exe",  "PostgreSQL");
        m.put("docker.exe",    "Docker");
        m.put("kitematic.exe", "Kitematic");
        m.put("virtualbox.exe","VirtualBox");
        m.put("vmware.exe",    "VMware");
        m.put("wireshark.exe", "Wireshark");
        m.put("putty.exe",     "PuTTY");
        m.put("vmconnect.exe", "Hyper-V Manager");
        m.put("rdpclip.exe",   "RDP 클립보드");
        m.put("onenote.exe",   "OneNote");
        m.put("outlookcal.exe","Outlook 캘린더");
        m.put("msoffice.exe",  "MS Office");
        m.put("skypeforbusiness.exe","Skype for Business");
        m.put("teamsdesktop.exe","Teams(데스크톱)");
        m.put("zoomconference.exe","Zoom 회의");
        // 61–80
        m.put("brave.exe",     "Brave");
        m.put("opera.exe",     "Opera");
        m.put("tor.exe",       "Tor Browser");
        m.put("vivaldi.exe",   "Vivaldi");
        m.put("edgewebview2.exe","Edge WebView2");
        m.put("msiexec.exe",   "Windows Installer");
        m.put("wuauclt.exe",   "Windows 업데이트");
        m.put("dwm.exe",       "Desktop Window Manager");
        m.put("audiodg.exe",   "Audio Device Graph");
        m.put("explorerframe.exe","Explorer Frame Host");
        m.put("searchindexer.exe","검색 인덱서");
        m.put("wermgr.exe",    "오류 보고 관리자");
        m.put("spoolsv.exe",   "인쇄 스풀러");
        m.put("svchost.exe",   "서비스 호스트");
        m.put("lsass.exe",     "로컬 보안권한 관리자");
        m.put("services.exe",  "서비스 컨트롤 관리자");
        m.put("csrss.exe",     "클라이언트 서버 런타임");
        m.put("wininit.exe",   "Windows 초기화");
        m.put("smss.exe",      "세션 관리자");
        m.put("system.exe",    "시스템");
        // 81–100
        m.put("chrome_proxy.exe","Chrome 프록시");
        m.put("code_helper.exe","VS Code Helper");
        m.put("msmpeng.exe",   "Windows Defender");
        m.put("searchui.exe",  "Windows Search");
        m.put("settingsynchost.exe","설정 동기화");
        m.put("shellexperiencehost.exe","셸 경험");
        m.put("searchapp.exe", "검색 앱");
        m.put("winstore.app.exe","Microsoft Store");
        m.put("secpol.msc",    "로컬 보안 정책");
        m.put("gpedit.msc",    "그룹 정책 편집기");
        m.put("regedit.exe",   "레지스트리 편집기");
        m.put("msconfig.exe",  "시스템 구성");
        m.put("dxdiag.exe",    "DirectX 진단");
        m.put("logoff.exe",    "로그오프");
        m.put("shutdown.exe",  "시스템 종료");
        m.put("taskkill.exe",  "프로세스 종료");
        m.put("mshta.exe",     "HTML 애플리케이션 호스트");
        m.put("wlmail.exe",    "Windows Live Mail");
        m.put("movie.exe",     "영화 및 TV");
        // 101–120
        m.put("yourphone.exe", "Your Phone");
        m.put("people.exe",    "People");
        m.put("camera.exe",    "카메라");
        m.put("photos.exe",    "사진");
        m.put("maps.exe",      "지도");
        m.put("calculator.exe","계산기(새버전)");
        m.put("alarms.exe",    "알람 및 시계");
        m.put("voicerecorder.exe","음성 녹음기");
        m.put("msimn.exe",     "Outlook Express");
        m.put("wab.exe",       "주소록");
        m.put("mstsc.exe",     "원격 데스크톱");
        m.put("chrome_child.exe","Chrome Child");
        m.put("msteamshelper.exe","Teams Helper");
        m.put("searchprotocolhost.exe","검색 프로토콜 호스트");
        m.put("searchfilterhost.exe","검색 필터 호스트");
        m.put("backgroundtransferhost.exe","백그라운드 전송 호스트");
        m.put("runtimebroker.exe","런타임 브로커");
        m.put("ctfmon.exe","CTF 로더");
        m.put("dllhost.exe","COM Surrogate");
        m.put("userinit.exe","사용자 초기화");
        m.put("winlogon.exe","Windows 로그인");
        // 121–140
        m.put("trustinstaller.exe","신뢰할 수 있는 설치 관리자");
        m.put("wscript.exe","Windows 스크립트 호스트");
        m.put("cscript.exe","콘솔 스크립트 호스트");
        m.put("write.exe","워드패드");
        m.put("searchprotocolhost.exe","검색 프로토콜 호스트");
        m.put("searchapp.exe","검색 앱");
        m.put("settingSyncHost.exe","설정 동기화");
        m.put("conhost.exe","콘솔 호스트");
        m.put("runtimebroker.exe","런타임 브로커");
        m.put("textinputhost.exe","터치 키보드");
        m.put("searchapp.exe","검색 앱");
        m.put("yourphone.exe","Your Phone Companion");
        m.put("sihost.exe","Shell Infrastructure Host");
        m.put("backgroundbitransferhost.exe","백그라운드 비트 전송 호스트");
        m.put("graphicsdevicepolicy.exe","그래픽 디바이스 정책");
        m.put("shellexperiencehost.exe","Shell Experience Host");
        m.put("wininit.exe","Windows 초기화");
        m.put("winlogon.exe","Windows 로그인");
        m.put("taskhostw.exe","태스크 호스트");
        m.put("smartscreen.exe","Windows SmartScreen");
        // 141–150
        m.put("searchindexer.exe","검색 인덱서");
        m.put("compattelemetryrunner.exe","호환성 텔레메트리");
        m.put("settingsynhost.exe","설정 동기화 호스트");
        m.put("chromeupdater.exe","Chrome 업데이트");
        m.put("teamsupdate.exe","Teams 업데이트");
        m.put("onedrivesetup.exe","OneDrive 설치");
        m.put("vslauncher.exe","VS 런처");
        m.put("vscode.exe","Visual Studio Code(스토어)");
        m.put("edgehtml.exe","EdgeHTML 호스트");
        m.put("dxwebsetup.exe","DirectX 웹 설치");
        FRIENDLY_NAMES = Collections.unmodifiableMap(m);
    }

    private Timeline timeline;
    private Label lblGlobal;
    private long globalSec = 0;
    private boolean running = false;
    private Stage primaryStage;
    private TrayIcon trayIcon;

    private static final String DATA_FILE = "data.json";
    private final ObservableList<ProcessItem> processes = FXCollections.observableArrayList();
    private TableView<ProcessItem> table;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        loadProcesses();

        // 상단: 글로벌 타이머
        lblGlobal = new Label("00:00:00");
        lblGlobal.setTextFill(Color.CYAN);
        lblGlobal.setStyle("-fx-font-size:28px;");
        Button btnStart = new Button("▶ 시작");
        Button btnPause = new Button("⏸ 일시정지");
        Button btnReset = new Button("⟳ 리셋");
        HBox top = new HBox(5, lblGlobal, btnStart, btnPause, btnReset);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(4));

        // 중앙: TableView
        table = new TableView<>(processes);
        table.setFixedCellSize(24);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ProcessItem,String> colName  = new TableColumn<>("프로그램");
        colName.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        colName.setPrefWidth(250);

        TableColumn<ProcessItem,String> colTime  = new TableColumn<>("시간");
        colTime.setCellValueFactory(cell -> cell.getValue().timeProperty());
        colTime.setPrefWidth(80);
        colTime.setStyle("-fx-alignment:CENTER;");

        TableColumn<ProcessItem,String> colUsage = new TableColumn<>("비율");
        colUsage.setCellValueFactory(cell -> cell.getValue().usageProperty());
        colUsage.setPrefWidth(80);
        colUsage.setStyle("-fx-alignment:CENTER;");

        TableColumn<ProcessItem,Void> colRemove = new TableColumn<>("제거");
        colRemove.setPrefWidth(50);
        colRemove.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("X");
            {
                btn.setOnAction(e -> {
                    ProcessItem item = getTableView().getItems().get(getIndex());
                    processes.remove(item);
                    saveProcesses();
                });
                btn.setMaxWidth(Double.MAX_VALUE);
            }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().setAll(colName, colTime, colUsage, colRemove);

        // 하단: 등록/통계 버튼
        Button btnRegister = new Button("＋ 등록");
        Button btnStats    = new Button("📊 통계/리포트");
        HBox bottom = new HBox(5, btnRegister, btnStats);
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(4));

        VBox root = new VBox(4, top, new Separator(), table, bottom);
        root.setStyle("-fx-background-color:#222;");
        root.setPadding(new Insets(8));

        Scene scene = new Scene(root, 480, 300);
        stage.setScene(scene);
        stage.setTitle("Focus Timer");
        stage.show();

        setupTrayIcon();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);

        btnStart   .setOnAction(e -> { startTimer(); lblGlobal.setTextFill(Color.RED); });
        btnPause   .setOnAction(e -> { pauseTimer(); lblGlobal.setTextFill(Color.CYAN); });
        btnReset   .setOnAction(e -> { resetAll(); lblGlobal.setTextFill(Color.CYAN); });
        btnRegister.setOnAction(e -> registerProcess());
        btnStats   .setOnAction(e -> showStatsWindow());
    }

    private void startTimer()  { if (!running) { timeline.play();  running = true; } }
    private void pauseTimer()  { if (running)  { timeline.pause(); running = false; } }
    private void resetAll() {
        timeline.stop(); running = false; globalSec = 0;
        processes.forEach(ProcessItem::reset);
        lblGlobal.setText(format(0));
        saveProcesses();
    }

    private void tick() {
        globalSec++;
        lblGlobal.setText(format(globalSec));
        if (globalSec % 3600 == 0 && trayIcon != null) {
            trayIcon.displayMessage("알림", "1시간 경과", TrayIcon.MessageType.INFO);
        }
        String fg = getForegroundProcessName();
        processes.stream()
                .filter(p -> p.exeName.equalsIgnoreCase(fg))
                .findFirst()
                .ifPresent(ProcessItem::increment);

        long totalActive = processes.stream()
                .mapToLong(ProcessItem::getElapsedSec)
                .sum();
        if (totalActive > 0) {
            processes.forEach(p -> p.updateUsage(totalActive));
        }
    }

    private String format(long s) {
        long h = s/3600, m = (s%3600)/60, sec = s%60;
        return String.format("%02d:%02d:%02d", h, m, sec);
    }

    private String getForegroundProcessName() {
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        IntByReference pid = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(hwnd, pid);
        HANDLE pr = Kernel32.INSTANCE.OpenProcess(
                Kernel32.PROCESS_QUERY_INFORMATION | Kernel32.PROCESS_VM_READ,
                false, pid.getValue()
        );
        char[] buf = new char[512];
        IntByReference len = new IntByReference(buf.length);
        boolean ok = Kernel32.INSTANCE.QueryFullProcessImageName(pr, 0, buf, len);
        Kernel32.INSTANCE.CloseHandle(pr);
        if (!ok) return "";
        String full = new String(buf, 0, len.getValue());
        int i = full.lastIndexOf('\\');
        return (i >= 0 ? full.substring(i+1) : full);
    }

    private void setupTrayIcon() {
        try {
            if (!SystemTray.isSupported()) return;
            SystemTray t = SystemTray.getSystemTray();
            java.awt.Image img = Toolkit.getDefaultToolkit().createImage("icon.png");
            trayIcon = new TrayIcon(img, "Focus Timer");
            trayIcon.setImageAutoSize(true);
            t.add(trayIcon);
        } catch (Exception ignored) {}
    }

    private void registerProcess() {
        showAlert("등록", "확인을 누른 뒤 10초 안에 등록할 창을 활성화하세요.");

        Thread captureThread = new Thread(() -> {
            try {
                String appTitle = primaryStage.getTitle();
                HWND targetHwnd = null;
                long deadline = System.currentTimeMillis() + 10_000;

                while (System.currentTimeMillis() < deadline) {
                    HWND hwnd = User32.INSTANCE.GetForegroundWindow();
                    char[] titleBuf = new char[512];
                    User32.INSTANCE.GetWindowText(hwnd, titleBuf, titleBuf.length);
                    String title = Native.toString(titleBuf);

                    if (!title.isEmpty() && !title.contains(appTitle)) {
                        targetHwnd = hwnd;
                        break;
                    }
                    Thread.sleep(200);
                }

                if (targetHwnd == null) {
                    Platform.runLater(() -> showAlert("실패", "창 활성화를 감지하지 못했습니다. 다시 시도하세요."));
                    return;
                }

                IntByReference pidRef = new IntByReference();
                User32.INSTANCE.GetWindowThreadProcessId(targetHwnd, pidRef);
                HANDLE processHandle = Kernel32.INSTANCE.OpenProcess(
                        Kernel32.PROCESS_QUERY_INFORMATION | Kernel32.PROCESS_VM_READ,
                        false, pidRef.getValue()
                );

                if (processHandle == null) {
                    Platform.runLater(() -> showAlert("실패", "프로세스 정보를 읽을 수 없습니다."));
                    return;
                }

                char[] pathBuf = new char[512];
                IntByReference pathLen = new IntByReference(pathBuf.length);
                boolean resolved = Kernel32.INSTANCE.QueryFullProcessImageName(processHandle, 0, pathBuf, pathLen);
                Kernel32.INSTANCE.CloseHandle(processHandle);

                if (!resolved || pathLen.getValue() <= 0) {
                    Platform.runLater(() -> showAlert("실패", "실행 파일 경로를 읽지 못했습니다."));
                    return;
                }

                String fullPath = new String(pathBuf, 0, pathLen.getValue());
                String exe = fullPath.substring(fullPath.lastIndexOf('\\') + 1);

                Platform.runLater(() -> {
                    if (processes.size() >= 6) {
                        showAlert("실패", "최대 6개 등록 가능합니다.");
                    } else if (processes.stream().anyMatch(p -> p.exeName.equalsIgnoreCase(exe))) {
                        showAlert("실패", exe + " 이미 등록됨");
                    } else {
                        processes.add(new ProcessItem(exe));
                        saveProcesses();
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("실패", "등록 중 오류가 발생했습니다: " + e.getMessage()));
            }
        });
        captureThread.setDaemon(true);
        captureThread.start();
    }

    private void showStatsWindow() {
        TableView<ProcessItem> stats = new TableView<>(processes);
        stats.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ProcessItem,String> c1 = new TableColumn<>("프로그램");
        c1.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        TableColumn<ProcessItem,String> c2 = new TableColumn<>("시간");
        c2.setCellValueFactory(cell -> cell.getValue().timeProperty());
        c2.setStyle("-fx-alignment:CENTER;");
        TableColumn<ProcessItem,String> c3 = new TableColumn<>("비율");
        c3.setCellValueFactory(cell -> cell.getValue().usageProperty());
        c3.setStyle("-fx-alignment:CENTER;");

        stats.getColumns().addAll(c1, c2, c3);

        VBox box = new VBox(10, new Label("전체 통계"), stats);
        box.setPadding(new Insets(10));
        Scene s = new Scene(box, 500, 400);

        Stage st = new Stage();
        st.initOwner(primaryStage);
        st.initModality(Modality.APPLICATION_MODAL);
        st.setTitle("통계/리포트");
        st.setScene(s);
        st.showAndWait();
    }

    private void loadProcesses() {
        try (Reader r = new FileReader(DATA_FILE)) {
            Type t = new TypeToken<List<String>>(){}.getType();
            List<String> lst = new Gson().fromJson(r, t);
            if (lst != null) lst.forEach(e -> processes.add(new ProcessItem(e)));
        } catch (Exception ignored) {}
    }

    private void saveProcesses() {
        try (Writer w = new FileWriter(DATA_FILE)) {
            List<String> lst = processes.stream()
                    .map(p -> p.exeName)
                    .collect(Collectors.toList());
            new Gson().toJson(lst, w);
        } catch (Exception ignored) {}
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(AlertType.INFORMATION, msg);
        a.setHeaderText(null);
        a.setTitle(title);
        a.initOwner(primaryStage);
        a.showAndWait();
    }

    public class ProcessItem {
        public final String exeName;
        public final String displayName;
        private final LongProperty elapsedSec = new SimpleLongProperty(0);
        private final StringProperty timeStr    = new SimpleStringProperty(format(0));
        private final StringProperty usageStr   = new SimpleStringProperty("0.0%");

        public ProcessItem(String exe) {
            this.exeName = exe;
            this.displayName = FRIENDLY_NAMES.getOrDefault(exe.toLowerCase(), exe);
            elapsedSec.addListener((obs, oldV, newV) ->
                    timeStr.set(format(newV.longValue()))
            );
        }
        public void increment() {
            elapsedSec.set(elapsedSec.get() + 1);
        }
        public void reset() {
            elapsedSec.set(0);
        }
        public void updateUsage(long totalActive) {
            double pct = totalActive > 0
                    ? 100.0 * elapsedSec.get() / totalActive : 0;
            usageStr.set(String.format("%.1f%%", pct));
        }
        public long getElapsedSec() {
            return elapsedSec.get();
        }
        public StringProperty timeProperty()  { return timeStr; }
        public StringProperty usageProperty() { return usageStr; }
        public String getDisplayName()        { return displayName; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
